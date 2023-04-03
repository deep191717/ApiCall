package com.deep.apicall.api;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;

public abstract class Response<T> implements ApiInterface<T> {

    Context context;

    private final Class<T> type;

    public Response(Class<T> type) {
        this.type = type;
    }

    public T getData(String data) {
        try {
            return new Gson().fromJson(data, getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Class<T> getType() {
        return type;
    }

    public Response<T> with(Context context){
        this.context = context;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public Response(Class<T> type,Context context) {
        this.type = type;
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
    public void onSuccessInType(T t) {

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
            if (context==null){
                return;
            }
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

