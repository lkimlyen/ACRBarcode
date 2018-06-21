package com.demo.scanacr.screen.confirm_delivery;


import com.demo.scanacr.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ConfirmDeliveryModule.class})
public interface ConfirmDeliveryComponent {
    void inject(ConfirmDeliveryActivity activity);

}
