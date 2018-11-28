package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>>{


    public static final String LOG_TAG = NewsActivity.class.getName();
    private static final String REQUEST_URL =
            " http://content.guardianapis.com/search?&show-tags=contributor&q=%27tech%27&api-key=2bbbc59c-5b48-46a5-83d3-8435d3136348";

    private static final int NEWS_LOADER_ID= 1;

    private NewsAdapter mNewsAdapter;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        ListView NewsListView = (ListView)findViewById(R.id.article_list_item);

         mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

                NewsListView.setEmptyView(mEmptyStateTextView);



        mNewsAdapter = new NewsAdapter(this,new ArrayList<NewsStory>());


        NewsListView.setAdapter(mNewsAdapter);


        NewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsStory currentArticle = mNewsAdapter.getItem(position);

                    Uri articleUri = Uri.parse(currentArticle.getmUrl());

                    Intent openWebSite = new Intent(Intent.ACTION_VIEW,articleUri);

                    startActivity(openWebSite);
                }
            });

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID,null,this);
        }else{
            View loadingIndicator =findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_connection);
        }


        }

        @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {

        Log.i(LOG_TAG, "news activity called");
        return new NewsLoader(this,REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> news) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_articles);

        mNewsAdapter.clear();

        if (news != null && !news.isEmpty()){
            mNewsAdapter.addAll(news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {

        mNewsAdapter.clear();

    }
}
