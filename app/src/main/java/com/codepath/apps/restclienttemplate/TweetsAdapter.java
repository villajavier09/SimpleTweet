package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter  extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {


    Context context;
    List<Tweet> tweets;
    //Pass in context and list of tweets


    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);
        return new ViewHolder(view);
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

    //Clean elements of recycler view upon refresh
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    //Add new list of elements to recycler view
    public void addAll(List<Tweet> tweetList ){
        tweets.addAll(tweetList);
        notifyDataSetChanged();


    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView timeStamp;
        ImageView tweetImage;


        //For each row inflate a layout


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            tweetImage = itemView.findViewById(R.id.tweetImage);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            timeStamp.setText(tweet.createdAt);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            if(tweet.imageUrl != null) {
                Glide.with(context).load(tweet.imageUrl).into(tweetImage);
            }
            else{
                tweetImage.setVisibility(View.GONE);
            }
        }
    }
}
