package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;       // Change this inside apikey.properties
	public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET; // Change this inside apikey.properties

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	//Get information for home timeline
	public void getHomeTimeline(JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id",1);
		client.get(apiUrl, params, handler);
	}

	public void getNextPageOfTweets(JsonHttpResponseHandler handler, long maxId) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id",maxId);
		client.get(apiUrl, params, handler);
	}
	//Make request to Post user tweet
	public void postTweet(String tweetContent, JsonHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",tweetContent);
		client.post(apiUrl,params,"", handler);
	}


	/**
	 * Given a date String of the format given by the Twitter API, returns a display-formatted
	 * String representing the relative time difference, e.g. "2m", "6d", "23 May", "1 Jan 14"
	 * depending on how great the time difference between now and the given date is.
	 * This, as of 2016-06-29, matches the behavior of the official Twitter app.
	 */
	public static class TimeFormatter {
		public static String getTimeDifference(String rawJsonDate) {
			String time = "";
			String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
			SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
			format.setLenient(true);
			try {
				long diff = (System.currentTimeMillis() - format.parse(rawJsonDate).getTime()) / 1000;
				if (diff < 5)
					time = "Just now";
				else if (diff < 60)
					time = String.format(Locale.ENGLISH, "%ds",diff);
				else if (diff < 60 * 60)
					time = String.format(Locale.ENGLISH, "%dm", diff / 60);
				else if (diff < 60 * 60 * 24)
					time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60));
				else if (diff < 60 * 60 * 24 * 30)
					time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24));
				else {
					Calendar now = Calendar.getInstance();
					Calendar then = Calendar.getInstance();
					then.setTime(format.parse(rawJsonDate));
					if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
						time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
								+ then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
					} else {
						time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
								+ then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
								+ " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
					}
				}
			}  catch (ParseException e) {
				e.printStackTrace();
			}
			return time;
		}

		/**
		 * Given a date String of the format given by the Twitter API, returns a display-formatted
		 * String of the absolute date of the form "30 Jun 16".
		 * This, as of 2016-06-30, matches the behavior of the official Twitter app.
		 */
		public static String getTimeStamp(String rawJsonDate) {
			String time = "";
			String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
			SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
			format.setLenient(true);
			try {
				Calendar then = Calendar.getInstance();
				then.setTime(format.parse(rawJsonDate));
				Date date = then.getTime();

				SimpleDateFormat format1 = new SimpleDateFormat("h:mm a \u00b7 dd MMM yy");

				time = format1.format(date);

			}  catch (ParseException e) {
				e.printStackTrace();
			}
			return time;
		}
	}
}
