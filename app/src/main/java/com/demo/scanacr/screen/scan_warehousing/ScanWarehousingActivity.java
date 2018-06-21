package com.demo.scanacr.screen.scan_warehousing;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.app.base.BaseActivity;
import com.demo.scanacr.app.di.Precondition;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.screen.print_stemp.PrintStempActivity;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class ScanWarehousingActivity extends BaseActivity {
    public static final int REQUEST_CODE = 123;
    @Inject
    ScanWarehousingPresenter ScanWarehousingPresenter;

    ScanWarehousingFragment fragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ScanWarehousingActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new ScanWarehousingModule(fragment))
                .inject(this);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void initFragment() {
        fragment = (ScanWarehousingFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = ScanWarehousingFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(ScanWarehousingFragment fragment, int frameId) {
        Precondition.checkNotNull(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        fragment.back();
        // super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode,resultCode, data);

    }

}
