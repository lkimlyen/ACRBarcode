package com.demo.scanacr.screen.setting;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class SettingModule {
    private final SettingContract.View SettingContractView;

    public SettingModule(SettingContract.View SettingContractView) {
        this.SettingContractView = SettingContractView;
    }

    @Provides
    @NonNull
    SettingContract.View provideSettingContractView() {
        return this.SettingContractView;
    }
}

