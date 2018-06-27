package com.demo.scanacr.screen.splash;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.demo.scanacr.R;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.screen.dashboard.DashboardActivity;
import com.demo.scanacr.screen.login.LoginActivity;
import com.demo.scanacr.util.LocationHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashActivity extends AppCompatActivity{
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
                    SplashActivity.this.finish();
                }else {
                    LoginActivity.start(SplashActivity.this);
                }
            }
        };
        countDownTimer.start();

    }


}
