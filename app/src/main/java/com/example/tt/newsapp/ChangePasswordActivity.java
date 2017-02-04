package com.example.tt.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tt.dao.UserInfoDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.newsapp.R;
import com.example.tt.util.Utils;

public class ChangePasswordActivity extends AppCompatActivity {
    private DatabaseHelp dbHelper;
    private UserInfoDao userDao;
    private EditText oldPwdTxt,newPwdTxt,rePwdTxt;
    private Button changePwdBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        dbHelper = new DatabaseHelp(this);
        userDao = new UserInfoDao(dbHelper);
        oldPwdTxt = (EditText) findViewById(R.id.oldPwdTxt);
        newPwdTxt = (EditText) findViewById(R.id.newPwdTxt);
        rePwdTxt = (EditText) findViewById(R.id.rePwdTxt);
        changePwdBtn = (Button) findViewById(R.id.changePwdBtn);
        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPwd = oldPwdTxt.getText().toString().trim();
                String newPwd = newPwdTxt.getText().toString().trim();
                String rePwd = rePwdTxt.getText().toString().trim();
                if(oldPwd.isEmpty()){
                    Utils.toast(ChangePasswordActivity.this, getString(R.string.err_empty_oldPwd));
                    oldPwdTxt.requestFocus();
                    return;
                }
                if(newPwd.isEmpty()){
                    Utils.toast(ChangePasswordActivity.this, getString(R.string.err_empty_newPwd));
                    newPwdTxt.requestFocus();
                    return;
                }
                if(rePwd.isEmpty()){
                    Utils.toast(ChangePasswordActivity.this, getString(R.string.err_empty_rePassword));
                    rePwdTxt.requestFocus();
                    return;
                }
                if(!rePwd.equals(newPwd)){
                    Utils.toast(ChangePasswordActivity.this, getString(R.string.err_not_match_rePassword));
                    rePwdTxt.requestFocus();
                    return;
                }
                if(!oldPwd.equals(Utils.loginUser.getPassword())){
                    Utils.toast(ChangePasswordActivity.this, getString(R.string.err_not_match_oldPwd));
                    oldPwdTxt.requestFocus();
                    return;
                }
                Utils.loginUser.setPassword(newPwd);
                userDao.updatePassword(Utils.loginUser.getId(),newPwd);
                Utils.toast(ChangePasswordActivity.this, "密码修改成功！");
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper != null){
            dbHelper.close();
        }
    }
}
