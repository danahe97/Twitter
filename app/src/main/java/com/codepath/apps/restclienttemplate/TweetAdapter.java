package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by danahe97 on 6/26/17.
 */

public class TweetAdapter extends  RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;

    // define an interface required by the viewholder
    public interface TweetAdapterListener {
        public void onItemSelected (View view, int position);
    }

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(getRelativeTimeAgo(tweet.createdAt));
        if (tweet.replyScreenName != "null"){
            holder.tvReplyUser.setText(tweet.replyScreenName);
            holder.tvReply.setVisibility(View.VISIBLE);
            holder.tvReplyUser.setVisibility(View.VISIBLE);
        }
        if (tweet.favorited) {
            holder.ivFavorited.setImageResource(R.drawable.ic_heart_filled);
        }
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public ImageButton ibReply;
        public TextView tvReply;
        public TextView tvReplyUser;
        public ImageView ivFavorited;

        public ViewHolder(View itemView) {
            super(itemView);

            // performs findViewById lookups
            final int REQUEST_CODE = 20;
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // gets item position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the tweet at the position, this won't work if the class is static
                        Tweet tweet = mTweets.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("screen_name", tweet.user.screenName);
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            ivFavorited = (ImageView) itemView.findViewById(R.id.ivFavorited);
            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // gets item position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the tweet at the position, this won't work if the class is static
                        Tweet tweet = mTweets.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, ComposeActivity.class);
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        // show the activity
                        ((TimelineActivity) context).startActivityForResult(intent, REQUEST_CODE);
                    }
                }
            });
            tvReply = (TextView) itemView.findViewById(R.id.tvReply);
            tvReplyUser = (TextView) itemView.findViewById(R.id.tvReplyUser);

            // handle row click events
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        // get the position of row element
                        int position = getAdapterPosition();
                        // fire the listener callback
                        mListener.onItemSelected(view, position);
                    }
                }
            });
        }
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

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }
}
