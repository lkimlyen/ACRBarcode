package com.demo.scanacr.screen.setting;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.demo.architect.data.model.UserResponse;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.UpdateVersionUsecase;
import com.demo.architect.utils.view.SendMailUtil;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.manager.UserManager;
import com.demo.scanacr.util.ConvertUtils;
import com.demo.scanacr.util.DownloadUtils;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private final String TAG = SettingPresenter.class.getName();
    private final SettingContract.View view;
    private final UpdateVersionUsecase updateVersionUsecase;
    private BroadcastReceiver mUpdateReceiver;
    @Inject
    LocalRepository localRepository;

    @Inject
    SettingPresenter(@NonNull SettingContract.View view, UpdateVersionUsecase updateVersionUsecase) {
        this.view = view;
        this.updateVersionUsecase = updateVersionUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        getVersion();
        getIPAddress();

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void updateApp() {
        view.showProgressBar();
        updateVersionUsecase.executeIO(new UpdateVersionUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<UpdateVersionUsecase.ResponseValue, UpdateVersionUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateVersionUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        //SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushString(Constants.LINK_DOWNLOAD,successResponse.getLink());
                        DownloadUtils.DownloadFile(CoreApplication.getInstance(), successResponse.getLink());
                        mUpdateReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Toast.makeText(CoreApplication.getInstance(), "Download Xong", Toast.LENGTH_SHORT).show();
                                String FilePath = Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + DownloadUtils.getFileName(successResponse.getLink());
                                CoreApplication.getInstance().unregisterReceiver(mUpdateReceiver);
                                view.installApp(FilePath);

                            }
                        };
                        CoreApplication.getInstance().registerReceiver(mUpdateReceiver,
                                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    }

                    @Override
                    public void onError(UpdateVersionUsecase.ErrorValue errorResponse) {

                    }
                });
    }

    @Override
    public String getVersion() {
        PackageManager manager = CoreApplication.getInstance().getPackageManager();
        PackageInfo info;
        String version = "";
        try {
            info = manager.getPackageInfo(
                    CoreApplication.getInstance().getPackageName(), 0);
            view.showVersion(info.versionName);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public void saveIPAddress(String ipAddress, int port) {
        int userId = UserManager.getInstance().getUser().getUserId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                view.showIPAddress(address);
            }
        });
    }

    @Override
    public void getIPAddress() {
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                view.showIPAddress(address);
            }
        });
    }

    @Override
    public void cloneDataAndSendMail() {
        view.showProgressBar();
        UserResponse user = UserManager.getInstance().getUser();
        String dataPath = ConvertUtils.exportRealmFile();
        if (!dataPath.equals("")) {
//            SendMailUtil.sendMail(user.getUserId(), user.getUserName(), user.getPhone(), dataPath, getVersion(),
//                    CoreApplication.getInstance().getString(R.string.text_name_database));
            view.uploadFile(dataPath,user.getUserId(), user.getUserName(), user.getPhone());
        }else {

            view.showError(CoreApplication.getInstance().getString(R.string.text_backup_fail));
            view.hideProgressBar();
        }
    }


}
