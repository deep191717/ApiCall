package com.deep.apicall;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.deep.apicall.api.Api;
import com.deep.apicall.api.RequestMethod;
import com.deep.apicall.api.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api.with(MainActivity.this,"https://test.com/")
                .setRequestMethod(RequestMethod.POST)
                .setPerms("test","test")
                .call("test.php", new Response() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                    }

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        super.onSuccess(jsonObject);
                    }

                    @Override
                    public void onSuccess(InputStream inputStream) {
                        super.onSuccess(inputStream);
                    }

                    @Override
                    public void onSuccess(String response) {
                        super.onSuccess(response);
                    }

                    @Override
                    public void onFailed(int code, String exception) {
                        super.onFailed(code, exception);
                    }

                });

    }
}