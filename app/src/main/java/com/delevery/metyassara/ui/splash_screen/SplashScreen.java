package com.delevery.metyassara.ui.splash_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.delevery.metyassara.R;
import com.delevery.metyassara.ui.login.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView logo = findViewById(R.id.logo);
        //بعرف الانميشن هنا اللي عايزه
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.from_top_to_below);

        //بدي الصوره الانميشن اللي عرفته
        logo.startAnimation(animation);
        //بعمل counter لمدة 3 ثواني يخلصوا يعمل intent
        Thread t = new Thread() {

            public void run() {

                try {

                    sleep(3000);
                    finish();
                    Intent IntentToLoginActivity = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(IntentToLoginActivity);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }
}

