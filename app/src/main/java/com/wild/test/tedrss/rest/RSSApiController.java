package com.wild.test.tedrss.rest;
/*
 * Created by Wild on 15.05.2015.
 */

import com.wild.test.tedrss.rest.TEDRssApi;

import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

public class RSSApiController {

    public static TEDRssApi getApi() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://www.ted.com/themes")
                .setConverter(new SimpleXMLConverter())
                .build();
        return adapter.create(TEDRssApi.class);
    }
}
