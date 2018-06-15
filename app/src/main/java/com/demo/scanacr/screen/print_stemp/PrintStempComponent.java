package com.demo.scanacr.screen.print_stemp;


import com.demo.scanacr.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {PrintStempModule.class})
public interface PrintStempComponent {
    void inject(PrintStempActivity activity);

}
