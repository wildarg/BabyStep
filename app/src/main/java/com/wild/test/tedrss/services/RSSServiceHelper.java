package com.wild.test.tedrss.services;
/*
 * Created by Wild on 17.05.2015.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RSSServiceHelper {

    public static final String BROADCAST_REQUEST_RESULT = "broadcast_request_result";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_MESSAGE = "extra_message";
    private static RSSServiceHelper instance;
    private final Context context;
    private static Object lock = new Object();
    private List<Long> pendingRequests = new ArrayList<>();

    public static RSSServiceHelper getInstance(Context context) {
        synchronized (lock) {
            if (instance == null)
                instance = new RSSServiceHelper(context);
        }
        return instance;
    }

    private RSSServiceHelper(Context context) {
        this.context = context;
    }

    public long refreshRSS() {
        long requestId = generateRequestID();
        pendingRequests.add(requestId);

        Intent srv = new Intent(context, RSSService.class);
        srv.putExtra(RSSService.EXTRA_CALLBACK, createServiceCallback(requestId));
        context.startService(srv);

        return requestId;
    }

    private ResultReceiver createServiceCallback(final long requestId) {
        ResultReceiver callback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                pendingRequests.remove(requestId);
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

    private long generateRequestID() {
        long requestId = UUID.randomUUID().getLeastSignificantBits();
        return requestId;
    }

    public boolean isRequestPending(long requestId) {
        return pendingRequests.contains(requestId);
    }

}
