package com.example.tt.newsapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    ImageView welcomePic;
    TextView pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        pass = (TextView) findViewById(R.id.pass);
        welcomePic = (ImageView) findViewById(R.id.welcomePic);
        AlphaAnimation aa = (AlphaAnimation) AnimationUtils.loadAnimation(this,R.anim.alpha_welcom);
        welcomePic.startAnimation(aa);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },5000);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
