package com.bucketsoft.user.project6newsfeedappstep1;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<GuardianItem>>, GuardianAdapter.OnItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search";


    private static final int GUARDIAN_LOADER_ID = 1;

    private GuardianAdapter adapter;
    private RecyclerView recyclerView;
    private TextView mEmptyStateTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.guardian_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEmptyStateTextView = findViewById(R.id.empty_view);

        adapter = new GuardianAdapter();
        adapter.setListener(this);
        adapter.setGuardianList(new ArrayList<GuardianItem>());
        recyclerView.setAdapter(adapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(GUARDIAN_LOADER_ID, null, this);

        } else {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<GuardianItem>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = sharedPrefs.getString(getString(R.string.settings_page_size_key), getString(R.string.settings_page_size_default));
        String pageNumber = sharedPrefs.getString(getString(R.string.settings_page_number_key), getString(R.string.settings_page_number_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("tag", "technology/technology");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("page", pageNumber);
        uriBuilder.appendQueryParameter("order_by", "newest");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "17e7216f-032d-4ef0-8a76-e157b8e37a23");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<GuardianItem>> loader, List<GuardianItem> guardianItems) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        adapter.clear();


        if (guardianItems != null && !guardianItems.isEmpty()) {
            adapter.addAll(guardianItems);

        } else {
            mEmptyStateTextView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<GuardianItem>> loader) {
        adapter.clear();
    }

    @Override
    public void onItemClick(GuardianItem selectedItem) {

        Uri guardianUri = Uri.parse(selectedItem.getWebURL());
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, guardianUri);
        startActivity(websiteIntent);
    }
}
