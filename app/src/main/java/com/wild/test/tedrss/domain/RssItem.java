package com.wild.test.tedrss.domain;
/*
 * Created by Wild on 15.05.2015.
 */

import android.content.ContentValues;
import android.util.Log;

import com.wild.test.tedrss.provider.RSSContract;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Root(name="item", strict = false)
public class RssItem {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    @Element
    public String title;
    @Element
    public String link;
    //example: Mon, 18 May 2015 08:56:50 +0000
    //mask: EEE, dd MMM yyyy HH:mm:ss Z
    @Element
    public String pubDate;
    @Element(data = true)
    public String description;
    @Namespace(prefix = "itunes")
    @Element
    public Image image;
    @Namespace(prefix = "itunes")
    @Element
    public String duration;
    @Element
    public Enclosure enclosure;

    private Long getPubDateTime() {
        try {
            return sdf.parse(pubDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("#RssItem", "error date parse " + pubDate + ": " + e.getMessage());
            Log.e("#RssItem", "now date: " + sdf.format(new Date()));
            return Long.valueOf(0);
        }
    }

    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(RSSContract.TITLE, title);
        values.put(RSSContract.LINK, link);
        values.put(RSSContract.PUBDATE, getPubDateTime());
        values.put(RSSContract.DESCRIPTION, description);
        if (image != null)
            values.put(RSSContract.IMAGE_URL, image.url);
        values.put(RSSContract.DURATION, duration);
        if (enclosure != null) {
            values.put(RSSContract.URL, enclosure.url);
            values.put(RSSContract.LENGTH, enclosure.length);
            values.put(RSSContract.TYPE, enclosure.type);
        }
        return values;
    }
}
