package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by danahe97 on 6/26/17.
 */

@Parcel
public class Tweet {

    // List out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public long replyUser;
    public String replyScreenName;
    public Boolean favorited;
    public String imageUrl;

    public Tweet() {
    }

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.replyScreenName = jsonObject.getString("in_reply_to_screen_name");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.imageUrl = null;
        if (jsonObject.getJSONObject("entities").has("media")) {
            tweet.imageUrl = jsonObject.getJSONObject("entities").getJSONArray("media")
                    .optJSONObject(0).getString("media_url");
            //tweet.imageUrl += ":small";
        }
        return tweet;
    }
}