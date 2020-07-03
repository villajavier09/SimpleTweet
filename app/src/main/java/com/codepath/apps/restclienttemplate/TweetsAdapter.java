package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {


    public static final String TAG = "TweetsAdapter";
    Context context;
    List<Tweet> tweets;
    TwitterClient client;
    //Pass in context and list of tweets

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTweetBinding binding = ItemTweetBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    //Bind values based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data the position
        Tweet tweet = tweets.get(position);
        //Bind to the viewholder
        holder.bind(tweet);
    }

    //Define a viewholder
    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    //Clean elements of recycler view upon refresh
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    //Add new list of elements to recycler view
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView tweetBody;
        TextView twitterUsername;
        TextView timeStamp;
        TextView profileName;
        ImageView tweetImage;
        ImageView like;
        ImageView retweet;
        ItemTweetBinding view;

        public ViewHolder(@NonNull ItemTweetBinding itemView) {
            super(itemView.getRoot());
            view = itemView;
            profileImage = view.profileImage;
            tweetBody = view.tweetBody;
            twitterUsername = view.twitterUsername;
            timeStamp = view.timeStamp;
            profileName = view.profileName;
            tweetImage = view.tweetImage;
            like = view.like;
            retweet = view.retweet;
            client = TwitterApp.getRestClient(context);
        }

        public void bind(final Tweet tweet) {
            tweetBody.setText(tweet.body);
            profileName.setText(tweet.user.name);
            twitterUsername.setText("@" + tweet.user.screenName);
            timeStamp.setText(tweet.createdAt);
            final long id =tweet.id;
            tweetImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(tweet.user.profileImageUrl).circleCrop().into(profileImage);
            if (tweet.imageUrl != null) {
                Glide.with(context).load(tweet.imageUrl).transform(new RoundedCorners(50)).into(tweetImage);
            } else {
                tweetImage.setVisibility(View.GONE);
            }

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    client.likeTweet(new JsonHttpResponseHandler() {
                                         @Override
                                         public void onSuccess(int statusCode, Headers headers, JSON json) {
                                             Log.i(TAG, "onSuccess");
                                             Glide.with(context).load(R.drawable.filled_heart).into(like);
                                         }
                                         @Override
                                         public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                             Log.e(TAG, "onFailure" + response);
                                         }
                                     },
                            id);
                }
            });
            retweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    client.postRetweet(new JsonHttpResponseHandler() {
                                         @Override
                                         public void onSuccess(int statusCode, Headers headers, JSON json) {
                                             Log.i(TAG, "onSuccess");
                                             Glide.with(context).load(R.drawable.filled_retweet).into(retweet);
                                         }
                                         @Override
                                         public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                             Log.e(TAG, "onFailure" + response);
                                         }
                                     },
                            id);
                }
            });
        }
    }
}
