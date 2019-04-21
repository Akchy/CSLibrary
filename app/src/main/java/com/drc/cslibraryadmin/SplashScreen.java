package com.drc.cslibraryadmin;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                File file = new File(getApplicationContext().getFilesDir(),"user_details");
                if(file.exists()){
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
