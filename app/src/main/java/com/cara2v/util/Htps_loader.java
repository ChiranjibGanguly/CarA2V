package com.cara2v.util;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;


import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.Collections;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * Created by chiranjib on 15/1/18.
 */

public class Htps_loader {
    static Picasso picasso;
    /***
     *
     * @param context
     * @return Picasso https laoder
     */
    static Picasso getPicasso(Context context){
        final OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .cache(new Cache(context.getCacheDir(), 25 * 1024 * 1024))
                .build();

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .memoryCache(new LruCache(context)).build();

        return picasso;
    }

    public static Picasso get(Context context){

        if(picasso==null) picasso = getPicasso( context);
        return picasso;


    }
}
