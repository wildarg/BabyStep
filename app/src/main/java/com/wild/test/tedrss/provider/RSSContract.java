package com.wild.test.tedrss.provider;
/*
 * Created by Wild on 17.05.2015.
 */

import android.net.Uri;

public class RSSContract {

    public static final String AUTHORITY = "com.wild.test.tedrss.provider.RSSProvider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String RSS = "rss";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, RSS);

    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String PUBDATE = "pubDate";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_URL = "image_url";
    public static final String DURATION = "duration";
    public static final String URL = "url";
    public static final String LENGTH = "length";
    public static final String TYPE = "type";

    public static final String[] PROJECTION = {TITLE, LINK, PUBDATE, DESCRIPTION, IMAGE_URL,
            DURATION, URL, LENGTH, TYPE};

}
