package com.example.tt.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tt.dao.UserInfoDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.UserInfo;
import com.example.tt.util.Utils;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameTxt, passwordTxt, rePasswordTxt;
    private Button registerBtn;
    private TextView goToLogin;

    private DatabaseHelp dbHelper;
    private UserInfoDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DatabaseHelp(this);
        userDao = new UserInfoDao(dbHelper);
        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        rePasswordTxt = (EditText) findViewById(R.id.rePasswordTxt);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        goToLogin = (TextView) findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTxt.getText().toString().trim();
                String password = passwordTxt.getText().toString().trim();
                String rePassword = rePasswordTxt.getText().toString().trim();
                if(username.isEmpty()){
                    Utils.toast(RegisterActivity.this, getString(R.string.err_empty_username));
                    usernameTxt.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    Utils.toast(RegisterActivity.this, getString(R.string.err_empty_password));
                    passwordTxt.requestFocus();
                    return;
                }
                if(rePassword.isEmpty()){
                    Utils.toast(RegisterActivity.this, getString(R.string.err_empty_rePassword));
                    rePasswordTxt.requestFocus();
                    return;
                }
                if(!password.equals(rePassword)){
                    Utils.toast(RegisterActivity.this, getString(R.string.err_not_match_rePassword));
                    rePasswordTxt.requestFocus();
                    return;
                }
                if(userDao.isUserExist(username)){
                    Utils.toast(RegisterActivity.this, getString(R.string.err_register_user_exist));
                    usernameTxt.requestFocus();
                    return;
                }
                UserInfo newUser = new UserInfo();
                newUser.setUsername(username);
                newUser.setPassword(password);
                userDao.save(newUser);

                UserInfo loginUser = userDao.findByUsernameAndPassword(username,password);
                Utils.loginUser = loginUser;
                SharedPreferences.Editor editor = getSharedPreferences(Utils.configPath,MODE_PRIVATE).edit();
                editor.putInt("loginUserId",loginUser.getId());
                editor.commit();
                Intent it = new Intent(RegisterActivity.this,MainActivity.class);
                Utils.isNewLoginUser = true;
//                Utils.isNewsNeedChange = true;
                Utils.isCollectionNeedChange = true;
                startActivity(it);
                Utils.toast(RegisterActivity.this,"注册成功！");
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
