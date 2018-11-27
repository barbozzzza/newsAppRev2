package com.example.android.newsapp;

import android.content.Context;

import android.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader  extends AsyncTaskLoader<List<NewsStory>> {

    // Log message
    private static final String LOG_TAG = NewsLoader.class.getName();

    // URL Query

    private String mUrl;


    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<NewsStory> loadInBackground() {
        if (mUrl == null) {


            return null;
        }
        List<NewsStory> news = QueryUtils.fetchNewsStoryData(mUrl);
        return news;
    }
}