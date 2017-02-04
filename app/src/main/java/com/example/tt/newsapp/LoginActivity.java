package com.example.tt.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tt.dao.UserInfoDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.UserInfo;
import com.example.tt.util.Utils;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameTxt, passwordTxt;
    private Button loginBtn;
    private TextView goToRegister;

    private DatabaseHelp dbHelper;
    private UserInfoDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseHelp(this);
        userDao = new UserInfoDao(dbHelper);

        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        goToRegister = (TextView) findViewById(R.id.goToRegister);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTxt.getText().toString().trim();
                String password = passwordTxt.getText().toString().trim();
                if(username.isEmpty()){
                    Utils.toast(LoginActivity.this, getString(R.string.err_empty_username));
                    usernameTxt.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    Utils.toast(LoginActivity.this, getString(R.string.err_empty_password));
                    passwordTxt.requestFocus();
                    return;
                }
                UserInfo loginUser = userDao.findByUsernameAndPassword(username,password);
                if(loginUser == null){
                    Utils.toast(LoginActivity.this, getString(R.string.err_login_wrong));
                    usernameTxt.requestFocus();
                    return;
                }
                Utils.loginUser = loginUser;
                SharedPreferences.Editor editor = getSharedPreferences(Utils.configPath,MODE_PRIVATE).edit();
                editor.putInt("loginUserId",loginUser.getId());
                editor.commit();
                Intent it = new Intent(LoginActivity.this,MainActivity.class);
                Utils.isNewLoginUser = true;
//                Utils.isNewsNeedChange = true;
                Utils.isCollectionNeedChange = true;
                startActivity(it);
                Utils.toast(LoginActivity.this,"已登录");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper!=null){
            dbHelper.close();
        }
    }
}
