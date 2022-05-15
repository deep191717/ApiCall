package com.deep.api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.deep.apicall.api.Api;
import com.deep.apicall.api.RequestMethod;
import com.deep.apicall.api.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api.with(this,"https://reqres.in/")
                .setRequestMethod(RequestMethod.POST)
                .setPerms("name", "morpheus")
                .setPerms("job", "leader")
                .call("api/users", new Response() {
                    @Override
                    public void onSuccess(String response) {
                        super.onSuccess(response);
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code, String exception) {
                        super.onFailed(code, exception);
                        Toast.makeText(MainActivity.this, exception, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}