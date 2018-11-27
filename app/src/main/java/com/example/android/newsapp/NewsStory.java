package com.example.android.newsapp;

public class NewsStory {

    private String mAuthor;
    private String mArticleTitle;
    private String mDate;
    private String mUrl;


    public NewsStory(String Author, String ArticleTitle, String Date, String Url){

        mAuthor = Author;
        mArticleTitle = ArticleTitle;
        mDate = Date;
        mUrl = Url;

    }

    public String getmAuthor() {return mAuthor;}

    public String getmArticleTitle() {return mArticleTitle;}

    public String getmDate() {return mDate;}

    public String getmUrl() {return mUrl;}



}
