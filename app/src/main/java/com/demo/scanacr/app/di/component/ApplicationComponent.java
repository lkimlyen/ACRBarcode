package com.demo.scanacr.app.di.component;



import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.app.base.BaseActivity;
import com.demo.scanacr.app.base.BaseFragment;
import com.demo.scanacr.app.di.module.ApplicationModule;
import com.demo.scanacr.app.di.module.NetModule;
import com.demo.scanacr.app.di.module.RepositoryModule;
import com.demo.scanacr.app.di.module.UseCaseModule;
import com.demo.scanacr.screen.chang_password.ChangePasswordComponent;
import com.demo.scanacr.screen.chang_password.ChangePasswordModule;
import com.demo.scanacr.screen.confirm_delivery.ConfirmDeliveryComponent;
import com.demo.scanacr.screen.confirm_delivery.ConfirmDeliveryModule;
import com.demo.scanacr.screen.create_code_package.CreateCodePackageComponent;
import com.demo.scanacr.screen.create_code_package.CreateCodePackageModule;
import com.demo.scanacr.screen.dashboard.DashboardComponent;
import com.demo.scanacr.screen.dashboard.DashboardModule;
import com.demo.scanacr.screen.detail_package.DetailPackageComponent;
import com.demo.scanacr.screen.detail_package.DetailPackageModule;
import com.demo.scanacr.screen.history_pack.HistoryPackageComponent;
import com.demo.scanacr.screen.history_pack.HistoryPackageModule;
import com.demo.scanacr.screen.import_works.ImportWorksComponent;
import com.demo.scanacr.screen.import_works.ImportWorksModule;
import com.demo.scanacr.screen.login.LoginComponent;
import com.demo.scanacr.screen.login.LoginModule;
import com.demo.scanacr.screen.print_stemp.PrintStempComponent;
import com.demo.scanacr.screen.print_stemp.PrintStempModule;
import com.demo.scanacr.screen.scan_delivery.ScanDeliveryComponent;
import com.demo.scanacr.screen.scan_delivery.ScanDeliveryModule;
import com.demo.scanacr.screen.scan_warehousing.ScanWarehousingComponent;
import com.demo.scanacr.screen.scan_warehousing.ScanWarehousingModule;
import com.demo.scanacr.screen.setting.SettingComponent;
import com.demo.scanacr.screen.setting.SettingModule;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by uyminhduc on 12/16/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        NetModule.class,
        UseCaseModule.class,
        RepositoryModule.class})
public interface ApplicationComponent {

    void inject(CoreApplication application);

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

    LoginComponent plus(LoginModule module);

    CreateCodePackageComponent plus(CreateCodePackageModule module);

    DashboardComponent plus(DashboardModule module);

    SettingComponent plus(SettingModule module);

    ChangePasswordComponent plus(ChangePasswordModule module);

    PrintStempComponent plus(PrintStempModule module);

    HistoryPackageComponent plus(HistoryPackageModule module);

    DetailPackageComponent plus(DetailPackageModule module);

    ScanWarehousingComponent plus(ScanWarehousingModule module);

    ScanDeliveryComponent plus(ScanDeliveryModule module);

    ConfirmDeliveryComponent plus(ConfirmDeliveryModule module);

    ImportWorksComponent plus(ImportWorksModule module);

}
