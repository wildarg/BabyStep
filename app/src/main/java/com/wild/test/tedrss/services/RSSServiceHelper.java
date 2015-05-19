package com.wild.test.tedrss.services;
/*
 * Created by Wild on 17.05.2015.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class RSSServiceHelper {

    public static final String BROADCAST_REQUEST_RESULT = "broadcast_request_result";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_MESSAGE = "extra_message";
    private static RSSServiceHelper instance;
    private final Context context;

    public static RSSServiceHelper getInstance(Context context) {
        if (instance == null)
            instance = new RSSServiceHelper(context);
        return instance;
    }

    public RSSServiceHelper(Context context) {
        this.context = context;
    }

    public void refreshRSS() {
        Intent srv = new Intent(context, RSSService.class);
        srv.putExtra(RSSService.EXTRA_CALLBACK, createServiceCallback());
        context.startService(srv);
    }

    private ResultReceiver createServiceCallback() {
        ResultReceiver callback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Log.d("#ServiceHelper", "done loading");
                sendBroadcast(resultCode, resultData);
            }
        };
        return callback;
    }

    private void sendBroadcast(int resultCode, Bundle resultData) {
        Intent result = new Intent(BROADCAST_REQUEST_RESULT);
        result.putExtra(EXTRA_RESULT, resultCode);
        if (resultData != null)
            result.putExtra(EXTRA_MESSAGE, resultData.getString(RSSService.EXTRA_MESSAGE));
        context.sendBroadcast(result);
    }
}
