package com.deep.apicall.api;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@SuppressWarnings("unused")
public class Api {

    private String BASE_URL = "";
    private String method;
    private Headers headers;


    public Api setRequestMethod(RequestMethod requestMethod) {
        if (requestMethod == RequestMethod.GET) {
            method = "GET";
        } else if (requestMethod == RequestMethod.POST) {
            method = "POST";
        }
        return this;
    }

    private final Activity activity;
    private final String TAG;

    public static Api with(Activity activity) {
        return new Api(activity, activity.getClass().getSimpleName());
    }

    public static Api with(Activity activity, String baseUrl) {
        return new Api(activity, activity.getClass().getSimpleName(), baseUrl);
    }

    private RequestBody body;
    private HashMap<String, String> perms = null;
    private HashMap<String, String> headerPerms = null;

    public Api(Activity activity, String TAG) {
        this.activity = activity;
        this.TAG = TAG;
    }

    public Api(Activity activity, String TAG, String baseUrl) {
        this.activity = activity;
        this.TAG = TAG;
        this.BASE_URL = baseUrl;
    }

    public Api setPerms(String key, String value) {
        if (perms == null) {
            perms = new HashMap<>();
        }
        perms.put(key, value);
        return this;
    }

    public Api setHeader(String key, String value) {
        if (headerPerms == null) {
            headerPerms = new HashMap<>();
        }
        headerPerms.put(key, value);
        return this;
    }

    private MediaType type = MultipartBody.FORM;

    public Api setPermsType(MediaType type) {
        this.type = type;
        return this;
    }


    private void setPerms() {
        if (body == null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(type);
            for (Map.Entry<String, String> entry : perms.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
                Log.e(TAG, "setPerms: " + entry.getKey() + " = " + entry.getValue());
            }
            body = builder.build();
        }
    }

    private void setHeaders() {
        if (headers == null) {
            for (Map.Entry<String, String> entry : headerPerms.entrySet()) {
                Log.e(TAG, "setHeaders: " + entry.getKey() + " = " + entry.getValue());
            }
            headers = Headers.of(headerPerms);
        }
    }

    public Api withNoPerms() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body = builder.build();
        return this;
    }


    public void call(String url, @NonNull Response response) {

        if (response.getContext() == null) {
            response.with(activity);
        }

        if ("GET".equals(method)) {
            url = embedUrl(url);
        } else {
            setPerms();
        }
        setHeaders();

        if (BASE_URL == null || BASE_URL.trim().isEmpty()) {
            activity.runOnUiThread(() -> response.onFailed(500, "BASE URL not found"));
        }

        if (method == null || method.trim().isEmpty()) {
            activity.runOnUiThread(() -> response.onFailed(500, "Request Methode not found, example 'POST','GET'."));
        }

        if (url == null || url.trim().isEmpty()) {
            Log.e(TAG, "call: " + BASE_URL + url);
            activity.runOnUiThread(() -> response.onFailed(500, "URL invalid"));
        }

        String finalUrl = url;
        new Thread(() -> {
            try {

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                //MediaType mediaType = MediaType.parse("text/plain");


                Log.e(TAG, "call: " + BASE_URL + finalUrl);

                Request request = new Request.Builder()
                        .url(BASE_URL + finalUrl)
                        .method(method, body)
                        .headers(headers)
                        .build();
                okhttp3.Response res = client.newCall(request).execute();
                Log.e(TAG, "call: " + res.headers());

                int responseCode = res.code();
                StringBuilder sBuilder1 = new StringBuilder();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    ResponseBody responseBody = res.body();

                    if (responseBody == null) {
                        activity.runOnUiThread(() -> response.onFailed(responseCode, "Response not found."));
                        return;
                    }

                    try {
                        InputStream inputStream = responseBody.byteStream();
                        activity.runOnUiThread(() -> response.onSuccess(inputStream));
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        while ((line = br.readLine()) != null) {
                            sBuilder1.append(line).append("\n");
                        }
                        Log.e(TAG, "call: " + sBuilder1);
                        if (!sBuilder1.toString().trim().isEmpty()) {
//                            JSONObject jsonObject = new JSONObject(sBuilder1.toString());
//                            String success = jsonObject.getString("res");
//                            String message = jsonObject.getString("msg");
//                            if (success.matches("success")) {
//                                String data = jsonObject.getString("data");
                            Object json = new JSONTokener(sBuilder1.toString()).nextValue();
                            if (json instanceof JSONObject) {
                                activity.runOnUiThread(() -> response.onSuccess((JSONObject) json));
                            } else if (json instanceof JSONArray) {
                                activity.runOnUiThread(() -> response.onSuccess((JSONArray) json));
                            } else {
                                activity.runOnUiThread(() -> response.onSuccess(sBuilder1.toString()));
                            }

                            activity.runOnUiThread(() -> response.onSuccessInType(response.getData(sBuilder1.toString())));

//                            } else {
//                                activity.runOnUiThread(() -> response.onFailed(responseCode, message));
//                            }
                        } else {
                            activity.runOnUiThread(() -> response.onFailed(responseCode, "No Request Found For this Date."));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> response.onFailed(responseCode, "No Request Found For this Date."));
                    }

                } else {
                    activity.runOnUiThread(() -> response.onFailed(responseCode, res.message().isEmpty() ? "Page Not Found" : res.message()));
                }

            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> response.onFailed(500, e.getMessage()));
            }
        }).start();
    }

    private String embedUrl(String url) {
        StringBuilder u = new StringBuilder();
        u.append(url);
        if (perms.entrySet().size() > 0) {
            u.append("?");
            for (Map.Entry<String, String> entry : perms.entrySet()) {
                u.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return u.toString();
    }


}
