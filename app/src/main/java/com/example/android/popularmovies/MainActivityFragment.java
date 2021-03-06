package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    public ArrayList<String[]> mThumbIds;
    boolean recallFlag = true;
    String sortType;
    private ImageAdapter mImageAdapter;
    private GridView gridview;

    public MainActivityFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mThumbIds = new ArrayList<>();
        //Initialized with dummy values to prevent on load freeze
        String[] arr = {"https://s5.postimg.org/b3evudzxz/blank.png", "", "", "", "", ""};
        mThumbIds.add(arr);

        if (savedInstanceState != null) {
            sortType = savedInstanceState.getString("SORT_TYPE");
            recallFlag = false;
        } else {
            sortType = getString(R.string.base_uri_popular);
            recallFlag = true;
        }

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_array, R.layout.spinner_layout);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int selection;
        if (sortType.equals(getString(R.string.base_uri_toprated))) {
            selection = 1;
        } else if (sortType.equals(getString(R.string.base_uri_favorites))) {
            selection = 2;
        } else {
            selection = 0;
        }

        spinner.setSelection(selection);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String headerText;
                if (position == 0) {
                    sortType = getString(R.string.base_uri_popular);
                    headerText = getString(R.string.popularHeader);
                } else if (position == 1) {
                    sortType = getString(R.string.base_uri_toprated);
                    headerText = getString(R.string.topHeader);
                } else {
                    sortType = getString(R.string.base_uri_favorites);
                    headerText = getString(R.string.topFavorites);
                }

                TextView mainHeader = (TextView) getActivity().findViewById(R.id.mainHeader);
                mainHeader.setText(headerText);
                updateMovieThumbs(sortType);
                if (gridview != null) {
                    gridview.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner.setPopupBackgroundResource(R.color.colorAccent);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (recallFlag || sortType == getString(R.string.base_uri_favorites)) {
            updateMovieThumbs(sortType);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("SORT_TYPE", sortType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return true;
    }

    private void updateMovieThumbs(String sortType) {

        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected || sortType == getString(R.string.base_uri_favorites)) {

            FetchMovieDatabase movieTask = new FetchMovieDatabase();
            movieTask.execute(sortType);

        } else {
            if (gridview != null) {
                gridview.setAdapter(null);
                Toast.makeText(getActivity(), "No Internet Connection",
                        Toast.LENGTH_SHORT).show();

            }


        }


    }


    public interface Callback {
        public void onItemSelected(ArrayList<String[]> mThumbIds, int position);
    }

    public class FetchMovieDatabase extends AsyncTask<String, Void, ArrayList<String[]>> {

        private final String LOG_TAG = FetchMovieDatabase.class.getSimpleName();


        private ArrayList<String[]> getMovieDataJson(String movieJsonStr)
                throws JSONException {

            final String RESULTS = "results";
            final String ORIGINAL_TITLE = "original_title";
            final String POSTER = "poster_path";
            final String OVERVIEW = "overview";
            final String VOTES = "vote_average";
            final String RELEASE = "release_date";
            final String ID = "id";
            ArrayList<String[]> newThumbids = new ArrayList<String[]>();

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject results = movieArray.getJSONObject(i);

                String posterPath = getString(R.string.api_image_base_path) + results.getString(POSTER);
                String title = results.getString(ORIGINAL_TITLE);
                String overview = results.getString(OVERVIEW);
                String voteAvg = results.getString(VOTES);
                String releaseDate = results.getString(RELEASE);
                String id = results.getString(ID);
                String[] outputAttr = {posterPath, title, overview, voteAvg, releaseDate, id};

                newThumbids.add(outputAttr);

            }
            return newThumbids;


        }

        private ArrayList<String[]> getFavorites() {

            ArrayList<String[]> newThumbids = new ArrayList<>();


            ContentResolver cr = getActivity().getContentResolver();
            Cursor c = cr.query(MoviesContract.MovieEntry.CONTENT_URI, null,
                    null, null, null);

            if (c.moveToFirst()) {
                do {
                    String[] outputAttr = {c.getString(2), c.getString(3), c.getString(4),
                            c.getString(6), c.getString(5), c.getString(1)};
                    newThumbids.add(outputAttr);
                } while (c.moveToNext());
            }
            c.close();
            return newThumbids;


        }

        @Override
        protected ArrayList<String[]> doInBackground(String... params) {

            if (params.length == 0) {

                return null;
            }

            if (params[0] == getString(R.string.base_uri_favorites)) {
                return getFavorites();

            } else {


                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String movieJsonStr = null;


                try {

                    final String BASE_URL = params[0];
                    final String API_KEY = "api_key";

                    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendQueryParameter(API_KEY, BuildConfig.MOVIES_API_KEY)
                            .build();


                    URL url = new URL(builtUri.toString());

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {

                        return null;
                    }
                    movieJsonStr = buffer.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);

                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }

                try {
                    return getMovieDataJson(movieJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }
        }


        @Override
        protected void onPostExecute(ArrayList<String[]> result) {
            if (result != null) {
                if (mThumbIds.size() > 0) {
                    mThumbIds.clear();
                }

                mThumbIds = (ArrayList<String[]>) result.clone();
                if (gridview == null) {
                    gridview = (GridView) getActivity().findViewById(R.id.gridview);
                    mImageAdapter = new ImageAdapter(getActivity());
                    gridview.setAdapter(mImageAdapter);


                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {

                            ((Callback) getActivity()).onItemSelected(mThumbIds, position);


                        }
                    });
                }
                mImageAdapter.notifyDataSetChanged();


            }
        }
    }

    static class ViewHolder{

        ImageView iv;
        TextView titleText;
        TextView releaseDate;
        TextView voteAvg;

    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {

            return mThumbIds.size();

        }


        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (inflater == null)
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null){
                convertView = inflater.inflate(R.layout.card_layout, null);
                ViewHolder holder=new ViewHolder();
                holder.titleText = (TextView) convertView.findViewById(R.id.cardMovieTitle);
                holder.releaseDate = (TextView) convertView.findViewById(R.id.cardMovieDate);
                holder.voteAvg = (TextView) convertView.findViewById(R.id.cardMovieRating);
                holder.iv = (ImageView) convertView.findViewById(R.id.cardImagePoster);
                convertView.setTag(holder);
            }

            viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.titleText.setText(mThumbIds.get(position)[1].trim());
            viewHolder.releaseDate.setText(mThumbIds.get(position)[4].trim());
            viewHolder.voteAvg.setText(mThumbIds.get(position)[3].trim());
            Picasso.with(mContext).load(mThumbIds.get(position)[0]).into(viewHolder.iv);
            return convertView;
        }

    }


}

