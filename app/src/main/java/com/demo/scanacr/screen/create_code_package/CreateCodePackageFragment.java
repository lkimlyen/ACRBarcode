package com.demo.scanacr.screen.create_code_package;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.scanacr.R;
import com.demo.scanacr.adapter.CreateCodePackListViewAdapter;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.screen.print_stemp.PrintStempActivity;
import com.demo.scanacr.util.Precondition;
import com.demo.scanacr.widgets.spinner.SearchableSpinner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreateCodePackageFragment extends BaseFragment implements CreateCodePackageContract.View {
    private static final int MY_LOCATION_REQUEST_CODE = 1234;
    private final String TAG = CreateCodePackageFragment.class.getName();
    private CreateCodePackageContract.Presenter mPresenter;
    private FusedLocationProviderClient mFusedLocationClient;
    private CreateCodePackListViewAdapter adapter;
    @Bind(R.id.ss_produce)
    SearchableSpinner ssProduce;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSO;

    @Bind(R.id.edt_barcode)
    EditText edtBarcode;

    @Bind(R.id.rv_code)
    ListView rvCode;

    private int orderId = 0;
    private Location mLocation;
    private int countList = 0;

    public CreateCodePackageFragment() {
        // Required empty public constructor
    }


    public static CreateCodePackageFragment newInstance() {
        CreateCodePackageFragment fragment = new CreateCodePackageFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_code_pack, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ssProduce.setTitle(getString(R.string.text_choose_request_produce));
        checkPermissionLocation();

        ssProduce.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public void onClick() {
                ssProduce.setCountListScan(mPresenter.countListScan(orderId));
            }
        });

    }


    @Override
    public void setPresenter(CreateCodePackageContract.Presenter presenter) {
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
    public void showRequestProduction(List<OrderModel> list) {
        ArrayAdapter<OrderModel> adapter = new ArrayAdapter<OrderModel>(getContext(), android.R.layout.simple_spinner_item, list);
        ssProduce.setAdapter(adapter);
        ssProduce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCodeSO.setText(list.get(position).getCodeSO());
                txtCustomerName.setText(list.get(position).getCustomerName());
                mPresenter.getProduct(list.get(position).getId());
                orderId = list.get(position).getId();
                mPresenter.getListCreateCode(orderId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showLogScanCreatePack(LogScanCreatePackList list) {


        adapter = new CreateCodePackListViewAdapter(list.getItemList(), new CreateCodePackListViewAdapter.OnItemClearListener() {
            @Override
            public void onItemClick(LogScanCreatePack item) {
                mPresenter.deleteItemLog(item);
            }
        }, new CreateCodePackListViewAdapter.OnEditTextChangeListener() {
            @Override
            public void onEditTextChange(LogScanCreatePack item, int number) {
                mPresenter.updateNumberInput(item.getId(), number);
            }
        });
        rvCode.setAdapter(adapter);

    }

    @OnClick(R.id.ic_refresh)
    public void refresh() {
        mPresenter.getData();
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @OnClick(R.id.btn_save)
    public void save() {
        if (edtBarcode.getText().toString().equals("")) {
            return;
        }

        if (mPresenter.countListScan(orderId) == 11) {
            showNotification(getString(R.string.text_list_had_enough), SweetAlertDialog.WARNING_TYPE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mLocation = location;
                        }
                    }
                });

        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_default_title))
                .setContentText(getString(R.string.text_save_barcode))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.checkBarcode(edtBarcode.getText().toString().trim(), orderId,
                                mLocation.getLatitude(), mLocation.getLongitude());
                        sweetAlertDialog.dismiss();
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
                                // Logic to handle location object
                            }
                        }
                    });

        }

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

    @OnClick(R.id.img_back)
    public void back() {
        if (mPresenter.countListScan(orderId) > 0) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_back_cancel_order_not_print))
                    .setConfirmText(getString(R.string.text_yes))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mPresenter.deleteAllItemLog();
                            sweetAlertDialog.dismiss();
                            getActivity().finish();
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

        } else {
            getActivity().finish();
        }
    }

    @OnClick(R.id.txt_print)
    public void print() {
        PrintStempActivity.start(getContext(), orderId);
    }
}
