package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp.NewsActivity.LOG_TAG;


public final class QueryUtils {

    /**
     * Helper method relato requesting and reciving data from the guardian.
     */

    private static List<News> extractFeatureFromJson(String newsJason) {
        // if json is empty or null, exit early.
        if (TextUtils.isEmpty(newsJason)) {
            return null;

        }


        // creates and empty arraylist that take the article data
        List<News> news = new ArrayList<>();


        try {

            //create JSON object from the JSON response
            JSONObject guardianJsonResponse = new JSONObject(newsJason);

            JSONArray newsArray = guardianJsonResponse.getJSONArray("response");

            // loop through json array to get data to create news objects

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentArticle = newsArray.getJSONObject(i);

                JSONObject articleData = currentArticle.getJSONObject("results");

                String articleName = articleData.getString("webTitle");
                String authorName = articleData.getString("sectionName");
                long date = articleData.getLong("webPublicationDate");
                String url = articleData.getString("webUrl");


                // passes information from json object article
                News article = new News(articleName, authorName, date, url);

                // adds news article infromation to list of articles.

                news.add(article);

            }

            // Get data for a single articles information and loop through the data until array is empty


        } catch (JSONException e) {

            Log.e("query utils", "Problem Parsing JSON");


        }

        // return the list of articles
        return news;

    }

    /**
     * Returns new URL object from string
     */

    private static URL createURL(String StringUrl){

        URL url = null;
        try { url = new URL(StringUrl);


        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL ", e);

        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    public static List<News> fetchNewsData (String requestUrl){

        // create URL object
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);

        }catch (IOException e){

            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news =extractFeatureFromJson(jsonResponse);

        return news;
    }


}
