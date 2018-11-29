package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>>, SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String LOG_TAG = NewsActivity.class.getName();
    private static final String REQUEST_URL = "https://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 1;
    private ProgressBar loading;
    private NewsAdapter mNewsAdapter;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        ListView NewsListView = (ListView) findViewById(R.id.article_list_item);
        loading = findViewById(R.id.loading_indicator);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        NewsListView.setEmptyView(mEmptyStateTextView);
        mNewsAdapter = new NewsAdapter(this, new ArrayList<NewsStory>());

        NewsListView.setAdapter(mNewsAdapter);
        NewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsStory currentArticle = mNewsAdapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getmUrl());
                Intent openWebSite = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(openWebSite);
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_connection);
        }

    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String useDate = sharedPrefs.getString(
                getString(R.string.usedate),
                "none");

        String orderBy = sharedPrefs.getString(
                getString(R.string.orderby),
                "none"
        );
        String section = sharedPrefs.getString(
                getString(R.string.section),
                "politics"
        );
        String pageSize = sharedPrefs.getString(
                getString(R.string.pagesize),
                "10"
        );

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        if (!orderBy.equals("none")) {
            uriBuilder.appendQueryParameter("order-by", orderBy);
        }
        if (!useDate.equals("none")) {
            uriBuilder.appendQueryParameter("use-date", useDate);
        }
        uriBuilder.appendQueryParameter("q", "debates");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");
        return new NewsLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> news) {

        loading.setVisibility(View.GONE);
        if (news == null) {
            mEmptyStateTextView.setText(R.string.no_articles);
        } else {
            if (news.size() == 0)
                mEmptyStateTextView.setText(R.string.no_articles);
            else
                mEmptyStateTextView.setVisibility(View.GONE);
            mNewsAdapter.addAll(news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        mNewsAdapter.addAll(new ArrayList<NewsStory>());
        loading.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadNews() {
        if (isNetworkAvailable()) {
            mNewsAdapter.clear();
            mEmptyStateTextView.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        } else {
            loading.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                loadNews();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.usedate)) ||
                key.equals(getString(R.string.section)) ||
                key.equals(getString(R.string.pagesize)) ||
                key.equals(getString(R.string.orderby))) {
            mNewsAdapter.clear();
            mEmptyStateTextView.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
