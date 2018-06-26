package com.demo.scanacr.screen.detail_package;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.scanacr.R;
import com.demo.scanacr.adapter.DetailPackAdapter;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.dialogs.CreateBarcodeDialog;
import com.demo.scanacr.screen.capture.ScanActivity;
import com.demo.scanacr.util.ConvertUtils;
import com.demo.scanacr.util.Precondition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailPackageFragment extends BaseFragment implements DetailPackageContract.View {
    private final String TAG = DetailPackageFragment.class.getName();
    private DetailPackageContract.Presenter mPresenter;
    private DetailPackAdapter adapter;
    private IntentIntegrator integrator = new IntentIntegrator(getActivity());
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLocation;
    private int orderId;
    private int logId;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.lv_code)
    ListView lvCodes;

    @Bind(R.id.txt_total)
    TextView txtTotal;

    @Bind(R.id.txt_date_create)
    TextView txtDateCreate;

    @Bind(R.id.txt_serial)
    TextView txtSerial;

    @Bind(R.id.txt_code_request)
    TextView txtCodeRequest;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSo;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;
    private final int MY_LOCATION_REQUEST_CODE = 167;

    public DetailPackageFragment() {
        // Required empty public constructor
    }

    public static DetailPackageFragment newInstance() {
        DetailPackageFragment fragment = new DetailPackageFragment();
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String contents = data.getStringExtra(Constants.KEY_SCAN_RESULT);
                String barcode = contents.replace("DEMO", "");
                if (adapter.getCount() == 11){
                    showError(getString(R.string.text_list_had_enough));
                }else {
                    mPresenter.checkBarcode(barcode, orderId, logId);
                }

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_pack, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        checkPermissionLocation();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        orderId = getActivity().getIntent().getIntExtra(Constants.KEY_ORDER_ID, 0);
        logId = getActivity().getIntent().getIntExtra(Constants.KEY_ID, 0);
        initView();
        return view;
    }

    private void initView() {

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
                    });

        }

    }

    @Override
    public void setPresenter(DetailPackageContract.Presenter presenter) {
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
        mPresenter.getOrder(orderId);
        mPresenter.getListHistory(logId);
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
    public void showOrder(OrderModel model) {
        txtCodeRequest.setText(model.getCodeProduction());
        txtCodeSo.setText(model.getCodeSO());
        txtCustomerName.setText(model.getCustomerName());
    }

    @Override
    public void showListCreatePack(LogCompleteCreatePackList list) {
        adapter = new DetailPackAdapter(list.getItemList());
        lvCodes.setAdapter(adapter);

        lvCodes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_code))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.deleteCode(list.getItemList().get(position).getId(), list.getItemList().get(position).getProductId(), logId);
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
                return false;
            }
        });
    }

    @Override
    public void showDetailPack(LogCompleteCreatePackList pack) {
        txtDateCreate.setText(ConvertUtils.ConvertStringToShortDate(pack.getDateCreate()));
        txtSerial.setText(String.valueOf(pack.getSerial()));
        txtTotal.setText(String.valueOf(pack.getNumTotal()));
    }

    @Override
    public void deletePackSuccess() {
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();

    }

    @Override
    public void showNumTotal(int num) {
        txtTotal.setText(String.valueOf(num));
    }

    @Override
    public void showDialogNumber(final ProductModel productModel, String barcode) {
        CreateBarcodeDialog dialog = new CreateBarcodeDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setModel(productModel, barcode);
        dialog.setListener(new CreateBarcodeDialog.OnItemSaveListener() {
            @Override
            public void onSave(int numberInput) {
                checkPermissionLocation();
                mPresenter.saveBarcode(mLocation.getLatitude(), mLocation.getLongitude(), barcode,
                        logId, numberInput);
            }
        });
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


    @OnClick(R.id.img_back)
    public void back() {
        if (mPresenter.countListScan(logId) > 0) {
            showNotification(getString(R.string.text_not_complete), SweetAlertDialog.WARNING_TYPE);
        } else {
            getActivity().finish();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_delete:
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_delete_pack))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                                mPresenter.deletePack(logId, orderId);
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

                return true;
            case R.id.it_done:
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_done_pack))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.updateData(logId, orderId, Integer.parseInt(txtSerial.getText().toString()),
                                        false);
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

                return true;
            case R.id.it_print:
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_print_pack))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.printStemp(orderId, Integer.parseInt(txtSerial.getText().toString()), 0,
                                        logId);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btn_scan)
    public void scan() {
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
}
