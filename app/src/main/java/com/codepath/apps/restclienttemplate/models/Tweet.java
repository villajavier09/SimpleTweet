package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public static final String TAG = "Tweet";
    public long id;
    public String body;
    public String createdAt;
    public User user;
    public String imageUrl;


    public Tweet() {
    }

    //Create a Tweet object from the Json data and assign the corresponding information.
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = TwitterClient.TimeFormatter.getTimeDifference(jsonObject.getString("created_at"));
        tweet.id = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        try {
            tweet.imageUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
            Log.d(TAG, "imageurl is: " + tweet.imageUrl);
        } catch (Exception e) {
            Log.e(TAG, "no image found");
        }

        return tweet;
    }

    //Create a list with all the tweets in the json request
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public long getId() {
        return id;
    }
}
