package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

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

    // Various fields of a Twitter tweet
    public String body;
    public String createdAt;
    public User user;
    public long id;
    public String media;

    // empty constructor needed by the Parceler library
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        // Set all the fields of each tweet based on JSON response
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");

        // Display media (photos) on a timeline view
        JSONObject entities = jsonObject.getJSONObject("entities");
        if (entities.has("media"))  {
            JSONArray medias = entities.getJSONArray("media");
            JSONObject media_obj = (JSONObject) medias.get(0);
            String media_type = media_obj.getString("type");
            if (media_type.equals("photo")){
                tweet.media = media_obj.getString("media_url_https");
            }
        }

        return tweet;
    }

    // Setup a list of tweets to display on RecyclerView
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int x = 0; x < jsonArray.length(); x++){
            tweets.add(fromJson(jsonArray.getJSONObject(x)));
        }
        return tweets;
    }

    // This allows us to get the date and time of the tweet/when it was posted
    public static String getRelativeTimeAgo(String rawJsonDate) {
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
