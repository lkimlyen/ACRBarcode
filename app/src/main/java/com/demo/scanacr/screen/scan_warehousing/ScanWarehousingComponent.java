package com.demo.scanacr.screen.scan_warehousing;


import com.demo.scanacr.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ScanWarehousingModule.class})
public interface ScanWarehousingComponent {
    void inject(ScanWarehousingActivity activity);

}
