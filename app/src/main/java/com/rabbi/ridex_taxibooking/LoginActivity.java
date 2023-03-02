package com.rabbi.ridex_taxibooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rabbi.ridex_taxibooking.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animation = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.ridex_animation);
        binding.phoneButton.setAnimation(animation);
        binding.googleButton.setAnimation(animation);

        binding.phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,PhoneActivity.class));
                Animatoo.INSTANCE.animateSlideUp(LoginActivity.this);

            }
        });


    }
}