package com.android.incred.musicplayer.repository;

import android.webkit.URLUtil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by incred on 8/12/17.
 */

public class RetrofitInstance {

    public static WebInterface getInterface(String url) throws IllegalArgumentException {
        //here return a retrofit with web interface
        if (!URLUtil.isValidUrl(url)) {
            throw new IllegalArgumentException("The url provided is not valid");
        }
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit.create(WebInterface.class);
    }
}
