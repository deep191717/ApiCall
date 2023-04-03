package com.deep.apicall.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public interface ApiInterface<T> {
    void onSuccess(JSONArray jsonArray);

    void onSuccess(JSONObject jsonObject);

    void onSuccess(InputStream inputStream);

    void onSuccess(String response);

    void onSuccessInType(T t);

    void onFailed(int code, String exception);
}
