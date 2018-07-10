package com.demo.scanacr.screen.import_works;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.offline.ImportWorksModel;
import com.demo.scanacr.R;
import com.demo.scanacr.adapter.WorksAdapter;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.screen.capture.ScanActivity;
import com.demo.scanacr.util.Precondition;
import com.demo.scanacr.widgets.spinner.SearchableSpinner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * Created by MSI on 26/11/2017.
 */

public class ImportWorksFragment extends BaseFragment implements ImportWorksContract.View {
    private final String TAG = ImportWorksFragment.class.getName();
    private ImportWorksContract.Presenter mPresenter;
    private IntentIntegrator integrator = new IntentIntegrator(getActivity());
    private FusedLocationProviderClient mFusedLocationClient;
    public MediaPlayer mp1, mp2;
    private Location mLocation;
    private WorksAdapter adapter;
    private final int MY_LOCATION_REQUEST_CODE = 167;
    private int requestId;
    private Vibrator vibrate;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.txt_title)
    TextView txtTitle;

    @Bind(R.id.lv_code)
    ListView lvCode;

    @Bind(R.id.layout_code)
    LinearLayout llRequestCode;

    @Bind(R.id.ss_produce)
    SearchableSpinner ssProduce;

    public ImportWorksFragment() {
        // Required empty public constructor
    }

    public static ImportWorksFragment newInstance() {
        ImportWorksFragment fragment = new ImportWorksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            checkPermissionLocation();
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String contents = data.getStringExtra(Constants.KEY_SCAN_RESULT);
                String barcode = contents.replace("DEMO", "");
                checkPermissionLocation();
                mPresenter.checkBarcode(requestId, barcode, mLocation.getLatitude(), mLocation.getLongitude());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        ButterKnife.bind(this, view);
        mp1 = MediaPlayer.create(getActivity(), R.raw.beepperrr);
        mp2 = MediaPlayer.create(getActivity(), R.raw.beepfail);
        initView();
        return view;
    }

    private void initView() {
        vibrate = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        txtTitle.setText(getString(R.string.text_import_works));
        llRequestCode.setVisibility(View.VISIBLE);
        ssProduce.setTitle(getString(R.string.text_choose_request_produce));
        checkPermissionLocation();
        ssProduce.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public boolean onClick() {
                return false;
            }
        });
        adapter = new WorksAdapter(getContext(), new ArrayList<ImportWorksModel>());
        lvCode.setAdapter(adapter);
        List<String> list = new ArrayList<>();
        list.add(CoreApplication.getInstance().getString(R.string.text_choose_request_produce));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        ssProduce.setAdapter(adapter);

        mPresenter.getRequest();
    }

    public void checkPermissionLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        } else {
            // Access to the location has been granted to the app.
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLocation = location;
                                // Logic to handle location object
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

    }

    @Override
    public void setPresenter(ImportWorksContract.Presenter presenter) {
        this.mPresenter = Precondition.checkNotNull(presenter);
    }

    @Override
    public void showProgressBar() {
        showProgressDialog();
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    public void showNotification(String content, int type) {
        new SweetAlertDialog(getContext(), type)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(content)
                .setConfirmText(getString(R.string.text_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void showError(String message) {
        showNotification(message, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void showSuccess(String message) {
        showToast(message);
    }


    @Override
    public void showListRequest(List<OrderRequestEntity> list) {
        ArrayAdapter<OrderRequestEntity> adapter = new ArrayAdapter<OrderRequestEntity>(getContext(), android.R.layout.simple_spinner_item, list);
        ssProduce.setAdapter(adapter);
        ssProduce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ssProduce.getSelectedItem().equals(getString(R.string.text_choose_request_produce))) {
                    return;
                }
                requestId = list.get(position).getId();
                mPresenter.getCodeScan(requestId);
                edtBarcode.setText("");
                ImportWorksFragment.this.adapter.clearItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListPackage(ImportWorksModel model) {
        adapter.addItem(model);
    }

    @Override
    public void startMusicError() {
        mp2.start();
    }

    @Override
    public void startMusicSuccess() {
        mp1.start();
    }

    @Override
    public void turnOnVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrate.vibrate(500);
        }
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionLocation();
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @OnClick(R.id.img_refresh)
    public void refresh() {
        mPresenter.getRequest();
    }

    @OnClick(R.id.btn_scan)
    public void scan() {
        if (ssProduce.getSelectedItem().toString().equals(getString(R.string.text_choose_request_produce))) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_order_id_null))
                    .setConfirmText(getString(R.string.text_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();

                        }
                    })

                    .show();
            return;
        }
        integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Đặt mã cần quét vào khung");
        integrator.setCameraId(CAMERA_FACING_BACK);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @OnClick(R.id.btn_save)
    public void save() {
        checkPermissionLocation();
        if (edtBarcode.getText().toString().equals("")) {
            return;
        }

        if (ssProduce.getSelectedItem().toString().equals(getString(R.string.text_choose_request_produce))) {
            showError(getString(R.string.text_order_id_null));
            return;
        }
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                        mPresenter.checkBarcode(requestId, edtBarcode.getText().toString().trim(), mLocation.getLatitude(), mLocation.getLongitude());
                    }
                })
                .setCancelText(getString(R.string.text_no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }
}
