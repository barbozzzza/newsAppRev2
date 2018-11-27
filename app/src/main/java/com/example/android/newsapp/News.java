package com.example.android.newsapp;

public class News {

    private String mAuthor;
    private String mArticleTitle;
    private Long mDate;
    private String mUrl;


    public News(String Author, String ArticleTitle, Long Date, String Url){

        mAuthor = Author;
        mArticleTitle = ArticleTitle;
        mDate = Date;
        mUrl = Url;

    }

    public String getmAuthor() {return mAuthor;}

    public String getmArticleTitle() {return mArticleTitle;}

    public long getmDate() {return mDate;}

    public String getmUrl() {return mUrl;}



}
