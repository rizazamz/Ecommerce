package com.riza.ecommerce.presentation.splashScreen;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import com.riza.ecommerce.databinding.ActivitySplashscreenBinding;
import com.riza.ecommerce.presentation.MainActivity;

public class SplashscreenActivity extends AppCompatActivity {
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

        ActivitySplashscreenBinding binding = ActivitySplashscreenBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                finish();
            }, 1500);
        }
    }