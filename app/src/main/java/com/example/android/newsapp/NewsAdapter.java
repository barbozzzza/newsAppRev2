package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Activity context, ArrayList<News> newslist){
        super(context,0,newslist);

    }

    @Override
    public View getView (int position, View convertView, @NonNull ViewGroup parent){

        View articleList = convertView;
        if(articleList == null) {

            articleList = LayoutInflater.from(getContext()).inflate(R.layout.news_list, parent, false);

        }

        News currentArticle = getItem(position);

        assert currentArticle != null;
        String currentAuthor = (currentArticle.getmAuthor());

        TextView author = (TextView) articleList.findViewById(R.id.article_author);
        author.setText(currentAuthor);

        String currentTitle = currentArticle.getmArticleTitle();

        TextView Title = (TextView) articleList.findViewById(R.id.article_title);
        Title.setText(currentTitle);

        TextView date = (TextView) articleList.findViewById(R.id.article_date);
        date.setText((CharSequence) date);


        return articleList;


    }














}
