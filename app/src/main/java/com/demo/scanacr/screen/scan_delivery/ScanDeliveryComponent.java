package com.demo.scanacr.screen.scan_delivery;


import com.demo.scanacr.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ScanDeliveryModule.class})
public interface ScanDeliveryComponent {
    void inject(ScanDeliveryActivity activity);

}
