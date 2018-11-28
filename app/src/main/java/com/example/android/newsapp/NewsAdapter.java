package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<NewsStory> {


    public NewsAdapter(Activity context, ArrayList<NewsStory> newslist){
        super(context,0,newslist);

    }

    @Override
    public View getView (int position, View convertView, @NonNull ViewGroup parent){

        View articleList = convertView;
        if(articleList == null) {

            articleList = LayoutInflater.from(getContext()).inflate(R.layout.activity_news, parent, false);

        }

        NewsStory currentArticle = getItem(position);


        String currentAuthor = (currentArticle.getmAuthor());

        TextView author = (TextView) articleList.findViewById(R.id.article_author);
        author.setText(currentAuthor);


        String currentArticleTitle = (currentArticle.getmArticleTitle());

        TextView title = (TextView) articleList.findViewById(R.id.article_title);
        title.setText(currentArticleTitle);




        String currentDate = (currentArticle.getmDate());



        TextView date = (TextView) articleList.findViewById(R.id.article_date);
        date.setText(currentDate);

        String currentSection = (currentArticle.getmSectionName());


        TextView section = (TextView) articleList.findViewById(R.id.article_Section);
        section.setText(currentSection);









        return articleList;


    }














}
