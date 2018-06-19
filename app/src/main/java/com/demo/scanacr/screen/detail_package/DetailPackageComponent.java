package com.demo.scanacr.screen.detail_package;


import com.demo.scanacr.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {DetailPackageModule.class})
public interface DetailPackageComponent {
    void inject(DetailPackageActivity activity);

}
