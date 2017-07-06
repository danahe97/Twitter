package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent getI = getIntent();
        final Tweet tweet = Parcels.unwrap(getI.getParcelableExtra("tweet"));
        String username = tweet.user.name;
        String screenName = tweet.user.screenName;
        String body = tweet.body;
        String createdAt = tweet.createdAt;
        String timestamp = getRelativeTimeAgo(createdAt);
        String profUrl = tweet.user.profileImageUrl;
        final long id = tweet.uid;

        TextView tvUsername = (TextView) findViewById(R.id.tvUsernameDetails);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenNameDetails);
        TextView tvBody = (TextView) findViewById(R.id.tvBodyDetails);
        TextView tvTimestamp = (TextView) findViewById(R.id.tvTimestampDetails);
        ImageView ivProfPicDetails = (ImageView) findViewById(R.id.ivProfPicDetails);
        final ImageButton ibFavorited = (ImageButton) findViewById(R.id.ibFavorited);
        if (tweet.favorited) {
            ibFavorited.setImageResource(R.drawable.ic_heart_filled);
        }

        tvUsername.setText(username);
        tvScreenName.setText("@" + screenName);
        tvBody.setText(body);
        tvTimestamp.setText(timestamp);
        Glide.with(this).load(profUrl).into(ivProfPicDetails);

        ibFavorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient client = TwitterApp.getRestClient();
                if (tweet.favorited) {
                    client.unfavorite(id, new JsonHttpResponseHandler());
                    ibFavorited.setImageResource(R.drawable.ic_heart);
                } else {
                    client.favorite(id, new JsonHttpResponseHandler());
                    ibFavorited.setImageResource(R.drawable.ic_heart_filled);
                }
            }
        });

        ImageButton ibClose = (ImageButton) findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent for the new activity
                Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                // show the activity
                getApplicationContext().startActivity(intent);
            }
        });
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
