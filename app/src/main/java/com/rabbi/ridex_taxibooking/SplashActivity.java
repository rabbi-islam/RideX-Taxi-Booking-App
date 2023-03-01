package com.rabbi.ridex_taxibooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    TextView app_name;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            }
        },2000);

        init();
    }

    public void init(){
        app_name = findViewById(R.id.app_name);
//        markerImage = findViewById(R.id.imageView);
        animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.ridex_animation);
        app_name.setAnimation(animation);
//        Glide.with(this).load(R.drawable.marker).into(markerImage);

    }
}