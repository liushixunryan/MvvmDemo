package com.xql.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xql.annotation.BindPath;

@BindPath(key = "login/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}