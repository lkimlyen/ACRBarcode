package com.demo.scanacr.screen.capture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.demo.scanacr.R;
import com.demo.scanacr.app.base.BaseActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScanActivity extends BaseActivity {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private Switch Stus;
    private boolean flash = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barcodeScannerView = initializeContent();
        Stus = (Switch) findViewById(R.id.btnSwitch);
        Stus.setChecked(flash);
        // turn the flash on if set via intent

        if (Stus.isChecked()) {
            barcodeScannerView.setTorchOn();
            UpdateView();
        } else {
            barcodeScannerView.setTorchOff();
            UpdateView();
        }
        setEvent();
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    protected DecoratedBarcodeView initializeContent() {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_capture);
        return (DecoratedBarcodeView) findViewById(com.google.zxing.client.android.R.id.zxing_barcode_scanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    public void UpdateView() {
        if (Stus.isChecked() == true) {
            Stus.setButtonDrawable(R.drawable.btn_switch_on);
        } else {
            Stus.setButtonDrawable(R.drawable.btn_switch_off);
        }
    }

    public void setEvent() {
        Stus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    barcodeScannerView.setTorchOn();
                    flash = true;
                    UpdateView();
                } else {
                    barcodeScannerView.setTorchOff();
                    flash = false;
                    UpdateView();
                }
            }
        });
    }
}
