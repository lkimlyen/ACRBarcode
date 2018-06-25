package com.demo.scanacr.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.demo.scanacr.R;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.constants.Constants;
import com.demo.scanacr.screen.dashboard.DashboardActivity;
import com.demo.scanacr.util.Precondition;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {
    private final String TAG = LoginFragment.class.getName();
    @Bind(R.id.edt_username)
    EditText edtUsername;

    @Bind(R.id.edt_password)
    EditText edtPassword;

    @Bind(R.id.sp_server)
    Spinner spServer;

    private LoginContract.Presenter mPresenter;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.server, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spServer.setAdapter(adapter);

        spServer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        mPresenter.saveServer(Constants.SERVER_MAIN);
                        break;
                    case 2:
                        mPresenter.saveServer(Constants.SERVER_TEST);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
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

    @OnClick(R.id.btn_login)
    public void login() {
        if (edtUsername.getText().toString().equals("")) {
            showNotification(getString(R.string.text_username_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }
        if (edtPassword.getText().toString().equals("")) {
            showNotification(getString(R.string.text_password_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }

        if (spServer.getSelectedItem().toString().equals(getString(R.string.text_choose_server1))) {
            showNotification(getString(R.string.text_server_null), SweetAlertDialog.WARNING_TYPE);
            return;
        }
        mPresenter.login(edtUsername.getText().toString(), edtPassword.getText().toString());
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
    public void loginError(String content) {
        showNotification(content, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void startDashboardActivity() {
        DashboardActivity.start(getContext());
        getActivity().finish();
    }

}
