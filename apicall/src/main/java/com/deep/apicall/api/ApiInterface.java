package com.deep.apicall.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public interface ApiInterface {
    void onSuccess(JSONArray jsonArray);

    void onSuccess(JSONObject jsonObject);

    void onSuccess(InputStream inputStream);

    void onSuccess(String response);

    void onFailed(int code, String exception);
}
