package com.demo.scanacr.screen.chang_password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.demo.scanacr.R;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.util.Precondition;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class ChangePasswordFragment extends BaseFragment implements ChangePasswordContract.View {
    private final String TAG = ChangePasswordFragment.class.getName();
    @Bind(R.id.edt_old_pass)
    EditText edtOldPass;

    @Bind(R.id.edt_new_pass)
    EditText edtNewPass;

    @Bind(R.id.edt_confirm_pass)
    EditText edtConfirmPass;

    private ChangePasswordContract.Presenter mPresenter;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void setPresenter(ChangePasswordContract.Presenter presenter) {
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

    @OnClick(R.id.btn_change)
    public void changePass() {
        if (edtOldPass.getText().toString().equals("")) {
            showNotification(getString(R.string.text_old_password_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }
        if (edtNewPass.getText().toString().equals("")) {
            showNotification(getString(R.string.text_new_password_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }

        if (edtConfirmPass.getText().toString().equals("")) {
            showNotification(getString(R.string.text_confirm_password_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }

        if (!edtConfirmPass.getText().toString().trim().equals(edtNewPass.getText().toString().trim())){
            showNotification(getString(R.string.text_confirm_pass_no_equal), SweetAlertDialog.WARNING_TYPE);
            return;
        }
        mPresenter.changePass(edtOldPass.getText().toString().trim(), edtConfirmPass.getText().toString().trim());
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
    public void changePassSuccess() {
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK,returnIntent);
        getActivity().finish();
    }

    @Override
    public void changeError(String error) {
        showNotification(error, SweetAlertDialog.ERROR_TYPE);
    }

    @OnClick(R.id.img_back)
    public void back(){
        getActivity().finish();
    }
}
