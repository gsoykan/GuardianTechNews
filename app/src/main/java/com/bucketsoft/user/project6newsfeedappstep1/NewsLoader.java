package com.bucketsoft.user.project6newsfeedappstep1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<GuardianItem>> {

    private static final String LOG_TAG = NewsLoader.class.getName();
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<GuardianItem> loadInBackground() {
        if (url == null) {
            return null;
        }

        List<GuardianItem> news = GuardianUtils.fetchNewsData(url);
        return news;
    }
}
