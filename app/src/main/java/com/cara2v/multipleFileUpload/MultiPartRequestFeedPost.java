package com.cara2v.multipleFileUpload;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.cara2v.ui.activity.consumer.AddVehicleInfoActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;

/**
 * Created by Chiranjib Ganguly on 12-March-18.
 */
public class MultiPartRequestFeedPost extends Request<String> {

    private Response.Listener<String> mListener;
    private HttpEntity mHttpEntity;
    private Map<String, String> mParams;
    private Context mContext;


    public MultiPartRequestFeedPost(Response.ErrorListener errorListener, Response.Listener listener, ArrayList<File> file, int numberOfFiles, Map<String, String> params, Context context) {
        super(Method.POST, Constant.BASE_URL+Constant.addSocialPostUrl, errorListener);
        mListener = listener;
        mParams=params;
        mHttpEntity = buildMultipartEntity(file, numberOfFiles);
        mContext=context;
    }

    private HttpEntity buildMultipartEntity(ArrayList<File> file, int numberOffiles) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for(int i=0; i < file.size();i++){
            FileBody fileBody = new FileBody(file.get(i));
            builder.addPart("postImage", fileBody);
        }

      //  builder.addTextBody(Template.Query.KEY_DIRECTORY, Template.Query.VALUE_DIRECTORY);
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addTextBody(key, value);
        }

        return builder.build();
    }




    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> header = new HashMap<>();
        header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
        return header;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            mHttpEntity.writeTo(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            VolleyLog.e("" + e);
            return null;
        } catch (OutOfMemoryError e){
            VolleyLog.e("" + e);
            return null;
        }

    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.success(new String(response.data),
                    getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

}