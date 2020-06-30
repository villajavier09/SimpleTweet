package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String body;
    public String createdAt;
    public User user;

    //Create a Tweet object from the Json data and assign the corresponding information.
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet= new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }
    //Create a list with all the tweets in the json request
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
         List<Tweet> tweets = new ArrayList<>();
         for(int i = 0; i < jsonArray.length();i++){
              tweets.add(fromJson(jsonArray.getJSONObject(i)));
         }
         return tweets;
    }
}
