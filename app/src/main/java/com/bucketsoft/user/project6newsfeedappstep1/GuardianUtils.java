package com.bucketsoft.user.project6newsfeedappstep1;

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

public final class GuardianUtils {


    public static final String LOG_TAG = GuardianUtils.class.getSimpleName();

    private GuardianUtils() {
    }

    public static List<GuardianItem> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<GuardianItem> newsFeed = extractFeatureFromJson(jsonResponse);


        return newsFeed;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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
                inputStream.close();
            }
        }
        return jsonResponse;
    }

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

    private static List<GuardianItem> extractFeatureFromJson(String guardianJSON) {

        if (TextUtils.isEmpty(guardianJSON)) {
            return null;
        }


        List<GuardianItem> newsFeed = new ArrayList<>();


        try {


            JSONObject baseJsonResponse = new JSONObject(guardianJSON);


            JSONObject initialResponseCascade = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = initialResponseCascade.getJSONArray("results");


            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNews = newsArray.getJSONObject(i);

                String sectionName = currentNews.getString("sectionName");
                String webPublicationDate = currentNews.getString("webPublicationDate");
                String webTitle = currentNews.getString("webTitle");
                String webUrl = currentNews.getString("webUrl");

                String contributorName = "None";

                JSONArray tagsArray = currentNews.getJSONArray("tags");
                if (tagsArray.length() >= 1) {
                    JSONObject tagContributor = tagsArray.getJSONObject(0);
                    contributorName = tagContributor.getString("webTitle");

                }

                GuardianItem newsItem = new GuardianItem(sectionName, webPublicationDate, webTitle, webUrl);
                newsItem.setContributorName(contributorName);


                newsFeed.add(newsItem);
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }


        return newsFeed;
    }

}
