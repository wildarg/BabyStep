package com.wild.test.tedrss;
/*
 * Created by Wild on 17.05.2015.
 */

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wild.test.tedrss.adapters.RSSAdapter;
import com.wild.test.tedrss.provider.RSSContract;

public class RSSFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private RSSAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new RSSAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("#Fragment", "create loader");
        return new CursorLoader(getActivity(), RSSContract.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("#Fragment", "load finished. size: " + data.getCount());
        adapter.swapCursor(data);
        //recyclerView.setAdapter(new RSSAdapter(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("#Fragment", "loader reset");
        adapter.swapCursor(null);
    }
}
