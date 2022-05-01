package com.deep.apicall.api;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public abstract class Response implements ApiInterface {

    Context context;

    public Response() {
    }

    public Response with(Context context){
        this.context = context;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public Response(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {

    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onSuccess(InputStream inputStream) {

    }

    @Override
    public void onSuccess(String response) {
        Log.d("Api Response :", response);
    }

    @Override
    public void onFailed(int code, String exception) {
        String ex = "Api Response is Failed: "+code+"\nreason: "+exception;
        Log.e("Api Response", "onFailed: "+code+"\nreason: "+exception );
        if (code != 200) {
            try {
                AlertDialog.Builder noInternetBuilder = new AlertDialog.Builder(context);
                noInternetBuilder.setMessage(ex);
                noInternetBuilder.create().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

