package com.wild.test.tedrss.rest;
/*
 * Created by Wild on 15.05.2015.
 */

import com.wild.test.tedrss.domain.RSS;

import retrofit.Callback;
import retrofit.http.GET;

public interface TEDRssApi {

    @GET("/rss/id/6")
    void getRss(Callback<RSS> callback);

}
