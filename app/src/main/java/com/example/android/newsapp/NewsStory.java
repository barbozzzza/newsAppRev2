package com.example.android.newsapp;

public class NewsStory {

    private String mAuthor;
    private String mArticleTitle;
    private String mDate;
    private String mUrl;
    private String mSectionName;


    public NewsStory(String ArticleTitle, String Author,  String Date, String Url,String SectionName){

        mAuthor = Author;
        mArticleTitle = ArticleTitle;
        mDate = Date;
        mUrl = Url;
        mSectionName = SectionName;

    }

    public String getmAuthor() {return mAuthor;}

    public String getmArticleTitle() {return mArticleTitle;}

    public String getmDate() {return mDate;}

    public String getmUrl() {return mUrl;}

    public String getmSectionName() {return mSectionName;}



}
