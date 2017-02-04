package com.example.tt.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.example.tt.util.Utils;

public class LogoutActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.loginUser = null;
        SharedPreferences.Editor e = getSharedPreferences(Utils.configPath,MODE_PRIVATE).edit();
        e.remove("loginUserId").commit();
        Utils.toast(this,"已登出");
        Utils.isNewLoginUser = true;
//        Utils.isNewsNeedChange = true;
        Utils.isCollectionNeedChange = true;
        finish();
    }
}
