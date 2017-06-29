package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        pb = (ProgressBar) findViewById(R.id.pbLoading);
    }

    public void post(View view) {
        pb.setVisibility(ProgressBar.VISIBLE);
        TwitterClient client = new TwitterClient(this);
        final EditText newTweet = (EditText) findViewById(R.id.etNewTweet);
        String newTweetString = newTweet.getText().toString();
        client.sendTweet(newTweetString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    // Prepare data intent
                    Intent data = new Intent();
                    // Pass relevant data back as a result
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    finish(); // closes the activity, pass data to parent
                } catch (JSONException e) {
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }
}
