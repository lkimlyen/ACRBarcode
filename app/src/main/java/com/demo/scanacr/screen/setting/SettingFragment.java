package com.demo.scanacr.screen.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.architect.data.model.offline.IPAddress;
import com.demo.scanacr.R;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.dialogs.ChangeIPAddressDialog;
import com.demo.scanacr.screen.chang_password.ChangePasswordActivity;
import com.demo.scanacr.util.ConvertUtils;
import com.demo.scanacr.util.Precondition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class SettingFragment extends BaseFragment implements SettingContract.View {
    private final String TAG = SettingFragment.class.getName();

    private SettingContract.Presenter mPresenter;
    private IPAddress mModel;
    private StorageReference storageRef;
    @Bind(R.id.txt_version)
    TextView txtVersion;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        return view;
    }


    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
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

    @OnClick(R.id.btn_check_update)
    public void checkUpdate() {
        mPresenter.updateApp();
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
    public void installApp(String path) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        install.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivityForResult(install, 333);

    }

    @Override
    public void showVersion(String version) {
        txtVersion.setText(String.format(getString(R.string.text_version), version));
    }

    @Override
    public void showIPAddress(IPAddress model) {
        mModel = model;
    }

    @Override
    public void showSuccess(String message) {

        showNotification(message, SweetAlertDialog.SUCCESS_TYPE);

    }

    @Override
    public void showError(String message) {
        showNotification(message, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void uploadFile(String path, int userId, String userName, String phone) {
        UploadTask uploadTask;
        Uri file = Uri.fromFile(new File(path));
        StorageReference riversRef = storageRef.child(userId + "_" + userName + "_" + phone + "/" + ConvertUtils.getTimeMillis() +file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                showError(getString(R.string.text_backup_fail));
                Log.d(TAG, exception.getMessage());
                hideProgressBar();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                hideProgressBar();
                showSuccess(getString(R.string.text_backup_success));
            }
        });
    }


    @OnClick(R.id.btn_change_ip_address)
    public void changeIPAddress() {
        ChangeIPAddressDialog dialog = new ChangeIPAddressDialog();
        dialog.show(getActivity().getFragmentManager(), TAG);
        dialog.setListener(new ChangeIPAddressDialog.OnItemSaveListener() {
            @Override
            public void onSave(String ipAddress, int port) {
                mPresenter.saveIPAddress(ipAddress, port);
                dialog.dismiss();
            }
        });
        if (mModel != null) {
            dialog.setContent(mModel.getIpAddress(), String.valueOf(mModel.getPortNumber()));
        }
    }

    @OnClick(R.id.btn_change_password)
    public void changePassword() {
        ChangePasswordActivity.start(getActivity());
    }

    public void showDialogChangePassSuccess() {
        showNotification(getString(R.string.text_change_password_succes), SweetAlertDialog.SUCCESS_TYPE);
    }

    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_clone)
    public void cloneData() {
        mPresenter.cloneDataAndSendMail();
    }
}
