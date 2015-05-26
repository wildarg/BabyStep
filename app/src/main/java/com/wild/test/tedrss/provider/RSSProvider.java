package com.wild.test.tedrss.provider;
/*
 * Created by Wild on 17.05.2015.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class RSSProvider extends ContentProvider{

    private DBOpenHelper dbHelper;
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int RSS_MATCH = 0;

    static {
        matcher.addURI(RSSContract.AUTHORITY, RSSContract.RSS, RSS_MATCH);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (matcher.match(uri)) {
            case RSS_MATCH:
                c = db.query(RSSContract.RSS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:throw new IllegalArgumentException("Unknown uri " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case RSS_MATCH: return "vnd.android.cursor.dir/vnd." + RSSContract.AUTHORITY + "." +
                    RSSContract.RSS;
            default: throw new IllegalArgumentException("Unknown uri " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new IllegalArgumentException("Insert not supported");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = matcher.match(uri);
        if (match != RSS_MATCH)
            throw new IllegalArgumentException("Unknown uri " + uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int cnt = db.delete(RSSContract.RSS, null, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new IllegalArgumentException("Update not supported");
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int counter = 0;
        db.beginTransaction();
        try {
            for (ContentValues val: values) {
                db.insert(RSSContract.RSS, null, val);
                counter++;
            }
            db.setTransactionSuccessful();
            if (counter > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return 0;
        } finally {
            db.endTransaction();
        }
    }



    private class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "rss.db";
        private static final int DB_VERSION = 2;

        public DBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = createTableSQL(RSSContract.RSS, RSSContract.PROJECTION);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + RSSContract.RSS);
            onCreate(db);
        }

        private String createTableSQL(String name, String[] fields) {
            StringBuilder sb = new StringBuilder();
            sb.append("create table ").append(name).append("(");
            sb.append(BaseColumns._ID).append(" integer primary key autoincrement ");
            for (String field: fields) {
                sb.append(",").append(field).append(" text");
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
