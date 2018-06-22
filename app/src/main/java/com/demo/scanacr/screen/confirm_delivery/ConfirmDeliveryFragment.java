package com.demo.scanacr.screen.confirm_delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.scanacr.R;
import com.demo.scanacr.adapter.DeliveryAdapter;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.util.Precondition;
import com.demo.scanacr.widgets.spinner.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmDeliveryFragment extends BaseFragment implements ConfirmDeliveryContract.View {
    private final String TAG = ConfirmDeliveryFragment.class.getName();
    private ConfirmDeliveryContract.Presenter mPresenter;
    private DeliveryAdapter adapter;
    private String requestCode = "";

    @Bind(R.id.btn_scan)
    Button btnScan;

    @Bind(R.id.txt_title)
    TextView txtTitle;

    @Bind(R.id.lv_code)
    ListView lvCode;

    @Bind(R.id.img_refresh)
    ImageView imgRefresh;

    @Bind(R.id.layout_code)
    LinearLayout llRequestCode;

    @Bind(R.id.layout_add)
    LinearLayout llAdd;

    @Bind(R.id.ss_produce)
    SearchableSpinner ssProduce;

    @Bind(R.id.btn_check)
    Button btnCheck;

    public ConfirmDeliveryFragment() {
        // Required empty public constructor
    }

    public static ConfirmDeliveryFragment newInstance() {
        ConfirmDeliveryFragment fragment = new ConfirmDeliveryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        txtTitle.setText(getString(R.string.text_check_code_scan));
        imgRefresh.setVisibility(View.GONE);
        llRequestCode.setVisibility(View.VISIBLE);
        llAdd.setVisibility(View.GONE);
        ssProduce.setTitle(getString(R.string.text_choose_request_produce));
        btnCheck.setVisibility(View.VISIBLE);
        btnScan.setText(getString(R.string.text_end));
        btnScan.setVisibility(View.GONE);

        ssProduce.setListener(new SearchableSpinner.OnClickListener() {
            @Override
            public void onClick() {

            }
        });
        List<String> list = new ArrayList<>();
        list.add(CoreApplication.getInstance().getString(R.string.text_choose_request_produce));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        ssProduce.setAdapter(adapter);
    }


    @Override
    public void setPresenter(ConfirmDeliveryContract.Presenter presenter) {
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
        showNotification(message, SweetAlertDialog.SUCCESS_TYPE);
        btnScan.setVisibility(View.GONE);
    }

    @Override
    public void showListRequest(List<OrderRequestEntity> list) {
        ArrayAdapter<OrderRequestEntity> adapter = new ArrayAdapter<OrderRequestEntity>(getContext(), android.R.layout.simple_spinner_item, list);

        ssProduce.setAdapter(adapter);
        ssProduce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requestCode = list.get(position).getCodeSX();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showListPackage(ScanDeliveryList list) {
        if (list == null) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.text_title_noti))
                    .setContentText(getString(R.string.text_no_data))
                    .setConfirmText(getString(R.string.text_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
            btnScan.setVisibility(View.GONE);
            return;
        }
        adapter = new DeliveryAdapter(list.getItemList());
        lvCode.setAdapter(adapter);
        btnScan.setVisibility(View.VISIBLE);
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


    @OnClick(R.id.btn_scan)
    public void end() {
        mPresenter.uploadData();
    }

    @OnClick(R.id.btn_check)
    public void check() {
        if (requestCode.equals("")) {
            return;
        }
        mPresenter.checkRequest(requestCode);
    }

}
