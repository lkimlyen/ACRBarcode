package com.demo.scanacr.screen.confirm_delivery;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ConfirmDeliveryModule {
    private final ConfirmDeliveryContract.View ConfirmDeliveryView;

    public ConfirmDeliveryModule(ConfirmDeliveryContract.View ConfirmDeliveryView) {
        this.ConfirmDeliveryView = ConfirmDeliveryView;
    }

    @Provides
    @NonNull
    ConfirmDeliveryContract.View provideConfirmDeliveryView() {
        return this.ConfirmDeliveryView;
    }
}

