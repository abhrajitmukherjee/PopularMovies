package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textView = (TextView) findViewById(R.id.textTitle1);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "Tweet me  <a href='http://twitter.com/abhrajit'>@abhrajit</a>";
        textView.setText(Html.fromHtml(text));

    }
}
