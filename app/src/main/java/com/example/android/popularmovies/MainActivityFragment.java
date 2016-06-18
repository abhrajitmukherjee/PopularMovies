package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter mImageAdapter;
    public ArrayList<String> mThumbIds;
    private GridView gridview;

    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("Initiate","Starts here-------------------");
        gridview = (GridView) getActivity().findViewById(R.id.gridview);
        mImageAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(mImageAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_toprated) {
            updateMovieThumbs(getString(R.string.base_uri_toprated));


        } else if (id == R.id.action_popular) {
            updateMovieThumbs(getString(R.string.base_uri_popular));
        }



        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mThumbIds = new ArrayList<String>();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieThumbs(getString(R.string.base_uri_popular));
        updateMovieThumbs(getString(R.string.base_uri_popular));

    }


    private void updateMovieThumbs(String sortType) {
        FetchMovieDatabase movieTask = new FetchMovieDatabase();
        movieTask.execute(sortType);
        Log.v("getCount", "Get Main count=" + Integer.toString(mThumbIds.size()));


    }


    public class FetchMovieDatabase extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchMovieDatabase.class.getSimpleName();


        private ArrayList<String> getMovieDataJson(String movieJsonStr)
                throws JSONException {

            final String RESULTS = "results";
            ArrayList<String> newThumbids=new ArrayList<String>();

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject results = movieArray.getJSONObject(i);
                String posterPath = "http://image.tmdb.org/t/p/w185"+results.getString("poster_path");
                Log.v(LOG_TAG, posterPath);

                newThumbids.add(posterPath);
                Log.v(LOG_TAG, Integer.toString(newThumbids.size()));

            }
            return newThumbids;


        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }
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

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {

                mThumbIds.clear();
                mThumbIds = (ArrayList<String>) result.clone();
                Log.v(LOG_TAG, "Postexecute count:"+Integer.toString( mThumbIds.size()));
                mImageAdapter.notifyDataSetChanged();

            }
        }
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {

            Log.v("getCount", "Get Count=" + Integer.toString(mThumbIds.size()));
           // return 5;
            return mThumbIds.size();

        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            } else {
                imageView = (ImageView) convertView;
            }

            Picasso.with(getActivity()).load(mThumbIds.get(position)).into(imageView);
            return imageView;
        }

    }


}

