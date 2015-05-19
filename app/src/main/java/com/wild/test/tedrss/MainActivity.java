package com.wild.test.tedrss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.wild.test.tedrss.services.RSSServiceHelper;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver receiver;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progress = (ProgressBar) findViewById(R.id.progress);
        findViewById(R.id.refreshButton).setOnClickListener(new RefreshButtonClickListener());
        createReceiver();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new RSSFragment())
                    .commit();
            refreshRss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(RSSServiceHelper.BROADCAST_REQUEST_RESULT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            hideProgress();
        }
    }

    private void createReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                hideProgress();
            }
        };
    }

    private void refreshRss() {
        showProgress();
        RSSServiceHelper.getInstance(this).refreshRSS();
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    private class RefreshButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            refreshRss();
        }
    }
}
