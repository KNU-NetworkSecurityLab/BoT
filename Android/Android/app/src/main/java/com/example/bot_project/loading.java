package com.example.bot_project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;

public class loading extends Activity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        startLoding();
    }

    private void startLoding(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}
