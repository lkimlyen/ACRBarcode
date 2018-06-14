package com.demo.scanacr.screen.splash;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.demo.scanacr.R;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.screen.dashboard.DashboardActivity;
import com.demo.scanacr.screen.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (UserManager.getInstance().getUser() != null) {
                    DashboardActivity.start(SplashActivity.this);
                }else {
                    LoginActivity.start(SplashActivity.this);
                }
            }
        };
        countDownTimer.start();
    }
}
