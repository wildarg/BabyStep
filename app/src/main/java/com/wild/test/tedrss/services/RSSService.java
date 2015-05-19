package com.wild.test.tedrss.services;
/*
 * Created by Wild on 17.05.2015.
 */

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.wild.test.tedrss.domain.RSS;
import com.wild.test.tedrss.domain.RssItem;
import com.wild.test.tedrss.provider.RSSContract;
import com.wild.test.tedrss.rest.RSSApiController;
import com.wild.test.tedrss.rest.TEDRssApi;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RSSService extends IntentService {

    private static final String LOG_TAG = "#RSSService";
    public static final String EXTRA_CALLBACK = "extra_callback";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final int RESULT_OK = 1;
    public static final int RESULT_ERROR = 2;

    private ResultReceiver callback;

    public RSSService() {
        super("RSSService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        callback = intent.getParcelableExtra(EXTRA_CALLBACK);

        TEDRssApi api = RSSApiController.getApi();
        api.getRss(new Callback<RSS>() {
            @Override
            public void success(RSS rss, Response response) {
                refreshProvider(rss);
                Log.d("#Service", "callback send ok");
                callback.send(RESULT_OK, null);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "Retrofit error: " + error.getMessage());
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_MESSAGE, error.getMessage());
                Log.d("#Service", "callback send error");
                callback.send(RESULT_ERROR, bundle);
            }
        });
    }

    private void refreshProvider(RSS rss) {
        int size = rss.channel.itemList.size();
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            values[i] = rss.channel.itemList.get(i).getValues();
        }
        getContentResolver().bulkInsert(RSSContract.CONTENT_URI, values);
        Log.d("#RSSService", "send to bulkInsert " + size + " items");
    }
}
