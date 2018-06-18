package com.demo.scanacr.screen.history_pack;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.OrderModel;
import com.demo.scanacr.R;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.util.Precondition;
import com.demo.scanacr.widgets.spinner.SearchableSpinner;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPackageFragment extends BaseFragment implements HistoryPackageContract.View {
    private final String TAG = HistoryPackageFragment.class.getName();
    private HistoryPackageContract.Presenter mPresenter;
    @Bind(R.id.ss_produce)
    SearchableSpinner ssProduce;

    @Bind(R.id.txt_code_so)
    TextView txtCodeSO;

    @Bind(R.id.lv_code)
    ListView rvCode;


    public HistoryPackageFragment() {
        // Required empty public constructor
    }


    public static HistoryPackageFragment newInstance() {
        HistoryPackageFragment fragment = new HistoryPackageFragment();
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
        View view = inflater.inflate(R.layout.fragment_history_pack, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ssProduce.setTitle(getString(R.string.text_choose_request_produce));


//        ssProduce.setListener(new SearchableSpinner.OnClickListener() {
//            @Override
//            public void onClick() {
//                ssProduce.setCountListScan(mPresenter.countListScan(orderId));
//            }
//        });

    }


    @Override
    public void setPresenter(HistoryPackageContract.Presenter presenter) {
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
              //  mPresenter.getProduct(list.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    }


}
