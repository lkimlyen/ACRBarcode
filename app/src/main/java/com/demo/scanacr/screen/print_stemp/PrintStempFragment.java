package com.demo.scanacr.screen.print_stemp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.scanacr.R;
import com.demo.scanacr.adapter.DetailPrintTempAdapter;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.dialogs.ChangeIPAddressDialog;
import com.demo.scanacr.util.ConvertUtils;
import com.demo.scanacr.util.Precondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempFragment extends BaseFragment implements PrintStempContract.View {
    public static final String ORDER_ID = "order_id";
    private final String TAG = PrintStempFragment.class.getName();
    private PrintStempContract.Presenter mPresenter;
    private DetailPrintTempAdapter adapter;
    private int orderId;
    @Bind(R.id.lv_codes)
    ListView lvCode;

    @Bind(R.id.txt_request_code)
    TextView txtRequestCode;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSO;

    @Bind(R.id.txt_customer_name)
    TextView txtCustomerName;

    @Bind(R.id.txt_total)
    TextView txtTotal;

    @Bind(R.id.txt_serial)
    TextView txtSerial;

    @Bind(R.id.txt_date_create)
    TextView txtDate;

    private boolean isClick = false;

    public PrintStempFragment() {
        // Required empty public constructor
    }


    public static PrintStempFragment newInstance() {
        PrintStempFragment fragment = new PrintStempFragment();
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
        View view = inflater.inflate(R.layout.fragment_print_stemp, container, false);
        ButterKnife.bind(this, view);
        orderId = getActivity().getIntent().getIntExtra(ORDER_ID, 0);
        initView();
        return view;
    }

    private void initView() {
        txtDate.setText(ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent()));
    }


    @Override
    public void setPresenter(PrintStempContract.Presenter presenter) {
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
        mPresenter.getMaxNumberOrder(orderId);
        mPresenter.getListCreatePack(orderId);
        mPresenter.getOrder(orderId);
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
    public void showSerialPack(int serial) {
        txtSerial.setText(serial + "");
    }

    @Override
    public void showOrder(OrderModel model) {
        txtCodeSO.setText(model.getCodeSO());
        txtCustomerName.setText(model.getCustomerName());
        txtRequestCode.setText(model.getCodeProduction());
    }

    @Override
    public void showListCreatePack(LogScanCreatePackList list) {

        adapter = new DetailPrintTempAdapter(list.getItemList());
        lvCode.setAdapter(adapter);
    }

    @Override
    public void showSumPack(int sum) {
        txtTotal.setText(sum + "");
    }

    @Override
    public void backToCreatePack() {
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    @Override
    public void showDialogCreateIPAddress() {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port,orderId, Integer.parseInt(txtSerial.getText().toString()), 0, Integer.parseInt(txtTotal.getText().toString()));
                dialog.dismiss();
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
        getActivity().finish();
        isClick = true;
    }

    @OnClick(R.id.btn_save)
    public void save() {

        mPresenter.printStemp(orderId, Integer.parseInt(txtSerial.getText().toString()), 0, Integer.parseInt(txtTotal.getText().toString()));
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isClick){
            mPresenter.deleteAllLog();
        }
    }
}
