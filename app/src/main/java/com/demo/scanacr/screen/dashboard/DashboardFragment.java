package com.demo.scanacr.screen.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.model.UserResponse;
import com.demo.scanacr.R;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.manager.ServerManager;
import com.demo.scanacr.screen.confirm_delivery.ConfirmDeliveryActivity;
import com.demo.scanacr.screen.create_code_package.CreateCodePackageActivity;
import com.demo.scanacr.screen.history_pack.HistoryPackageActivity;
import com.demo.scanacr.screen.import_works.ImportWorksActivity;
import com.demo.scanacr.screen.login.LoginActivity;
import com.demo.scanacr.screen.scan_delivery.ScanDeliveryActivity;
import com.demo.scanacr.screen.scan_warehousing.ScanWarehousingActivity;
import com.demo.scanacr.screen.setting.SettingActivity;
import com.demo.scanacr.util.Precondition;
import com.thefinestartist.finestwebview.FinestWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class DashboardFragment extends BaseFragment implements DashboardContract.View {
    private final String TAG = DashboardFragment.class.getName();

    @Bind(R.id.txt_name)
    TextView txtName;

    @Bind(R.id.txt_position)
    TextView txtPosition;

    @Bind(R.id.btn_link)
    Button btnLink;

    @Bind(R.id.btn_confirm_delivery)
    Button btnConfirmDelivery;

    @Bind(R.id.btn_create_package)
    Button btnCreatePack;

    @Bind(R.id.btn_report_detail)
    Button btnReportDetail;

    @Bind(R.id.btn_history_pack)
    Button btnHistoryPack;

    @Bind(R.id.btn_warehousing)
    Button btnWarehousing;

    @Bind(R.id.btn_delivery)
    Button btnDelivery;

    @Bind(R.id.btn_import_works)
    Button btnImportWorks;
    private DashboardContract.Presenter mPresenter;

    public DashboardFragment() {
        // Required empty public constructor
    }


    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        btnLink.setText(String.format(getString(R.string.text_web_report), ServerManager.getInstance().getServer()));
        btnCreatePack.setVisibility(View.GONE);
        btnHistoryPack.setVisibility(View.GONE);
        btnWarehousing.setVisibility(View.GONE);
        btnDelivery.setVisibility(View.GONE);
        btnConfirmDelivery.setVisibility(View.GONE);
        btnImportWorks.setVisibility(View.GONE);
    }


    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
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


    @Override
    public void showUser(UserResponse user) {
        txtName.setText(user.getFullName());
        if (user.getUserRoleID() == 17) {
            txtPosition.setText("Scan Tạo mã Gói");
            btnHistoryPack.setVisibility(View.VISIBLE);
            btnCreatePack.setVisibility(View.VISIBLE);
        } else if (user.getUserRoleID() == 18) {
            txtPosition.setText("Scan Nhập Kho");
            btnWarehousing.setVisibility(View.VISIBLE);
        } else if (user.getUserRoleID() == 19) {
            txtPosition.setText("Scan Giao Hàng");
            btnConfirmDelivery.setVisibility(View.VISIBLE);
            btnConfirmDelivery.setVisibility(View.VISIBLE);
        } else if (user.getUserRoleID() == 20) {
            txtPosition.setText("Scan Nhập Công Trình");
            btnImportWorks.setVisibility(View.VISIBLE);
        } else {
            txtPosition.setText("ADMIN APP");
            btnCreatePack.setVisibility(View.VISIBLE);
            btnHistoryPack.setVisibility(View.VISIBLE);
            btnWarehousing.setVisibility(View.VISIBLE);
            btnDelivery.setVisibility(View.VISIBLE);
            btnConfirmDelivery.setVisibility(View.VISIBLE);
            btnImportWorks.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDeliveryNotComplete(int count) {
        if (count > 0) {
            btnConfirmDelivery.setText(getString(R.string.text_check_code_scan) + " (" + count + ")");
        } else {
            btnConfirmDelivery.setText(getString(R.string.text_check_code_scan));
        }
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.text_title_noti))
                .setContentText(getString(R.string.text_do_you_want_logout))
                .setConfirmText(getString(R.string.text_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mPresenter.logout();
                        RealmHelper.getInstance().initRealm(false);
                        LoginActivity.start(getContext());
                        getActivity().finishAffinity();
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

    @OnClick(R.id.btn_setting)
    public void setting() {
        SettingActivity.start(getContext());
    }

    @OnClick(R.id.btn_report_detail)
    public void reportDetailCreatePack() {
        new FinestWebView.Builder(getActivity()).show(String.format(getString(R.string.text_url_report), ServerManager.getInstance().getServer()));
    }

    @OnClick(R.id.btn_create_package)
    public void createPackage() {
        CreateCodePackageActivity.start(getContext());
    }

    @OnClick(R.id.btn_history_pack)
    public void historyPack() {
        HistoryPackageActivity.start(getContext());
    }

    @OnClick(R.id.btn_link)
    public void link() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(btnLink.getText().toString()));
        startActivity(intent);
    }

    @OnClick(R.id.btn_warehousing)
    public void scanWarehousing() {
        ScanWarehousingActivity.start(getActivity());
    }

    @OnClick(R.id.btn_delivery)
    public void delivery() {
        ScanDeliveryActivity.start(getContext());
    }

    @OnClick(R.id.btn_confirm_delivery)
    public void confirmDelivery() {
        ConfirmDeliveryActivity.start(getContext());
    }

    @OnClick(R.id.btn_import_works)
    public void importWorks() {
        ImportWorksActivity.start(getContext());
    }
}
