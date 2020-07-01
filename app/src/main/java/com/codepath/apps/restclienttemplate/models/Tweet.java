package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public String imageUrl;
    public static final String TAG = "Tweet";


    public Tweet(){}

    //Create a Tweet object from the Json data and assign the corresponding information.
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet= new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        try{
            tweet.imageUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
            Log.d(TAG, "imageurl is: "+tweet.imageUrl);
        }
        catch (Exception e){
            Log.e(TAG,"no image found");
        }

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

    //Format time stamp
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
