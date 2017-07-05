package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent getI = getIntent();
        String username = getI.getStringExtra("username");
        String screenName = getI.getStringExtra("screenName");
        String body = getI.getStringExtra("body");
        String createdAt = getI.getStringExtra("createdAt");
        String timestamp = getRelativeTimeAgo(createdAt);
        String profUrl = getI.getStringExtra("profUrl");

        TextView tvUsername = (TextView) findViewById(R.id.tvUsernameDetails);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenNameDetails);
        TextView tvBody = (TextView) findViewById(R.id.tvBodyDetails);
        TextView tvTimestamp = (TextView) findViewById(R.id.tvTimestampDetails);
        ImageView ivProfPicDetails = (ImageView) findViewById(R.id.ivProfPicDetails);

        tvUsername.setText(username);
        tvScreenName.setText("@" + screenName);
        tvBody.setText(body);
        tvTimestamp.setText(timestamp);
        Glide.with(this).load(profUrl).into(ivProfPicDetails);
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
