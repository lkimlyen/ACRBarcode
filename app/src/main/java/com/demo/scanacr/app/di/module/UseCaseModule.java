package com.demo.scanacr.app.di.module;


import com.demo.architect.data.repository.base.account.remote.AuthRepository;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;
import com.demo.architect.domain.AddLogScanACRUsecase;
import com.demo.architect.domain.AddLogScanInStoreACRUsecase;
import com.demo.architect.domain.AddLogScanbyJsonUsecase;
import com.demo.architect.domain.AddPackageACRUsecase;
import com.demo.architect.domain.AddPackageACRbyJsonUsecase;
import com.demo.architect.domain.ChangePasswordUsecase;
import com.demo.architect.domain.DeletePackageDetailUsecase;
import com.demo.architect.domain.DeletePackageUsecase;
import com.demo.architect.domain.GetAllDetailForSOACRUsecase;
import com.demo.architect.domain.GetAllPackageForRequestUsecase;
import com.demo.architect.domain.GetAllPackageUsecase;
import com.demo.architect.domain.GetAllRequestACRInUsecase;
import com.demo.architect.domain.GetAllRequestACRUsecase;
import com.demo.architect.domain.GetAllSOACRUsecase;
import com.demo.architect.domain.GetAllScanTurnOutUsecase;
import com.demo.architect.domain.GetDateServerUsecase;
import com.demo.architect.domain.GetMaxPackageForSOUsecase;
import com.demo.architect.domain.GetMaxTimesACRUsecase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.architect.domain.UpdateVersionUsecase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by uyminhduc on 12/16/16.
 */
@Module
public class UseCaseModule {
    public UseCaseModule() {
    }


    @Provides
    LoginUsecase provideLoginUsecase(AuthRepository remoteRepository) {
        return new LoginUsecase(remoteRepository);
    }

    @Provides
    ChangePasswordUsecase provideChangePasswordUsecase(AuthRepository remoteRepository) {
        return new ChangePasswordUsecase(remoteRepository);
    }

    @Provides
    GetAllPackageUsecase provideGetAllPackageUsecase(OrderRepository remoteRepository) {
        return new GetAllPackageUsecase(remoteRepository);
    }

    @Provides
    GetAllSOACRUsecase provideGetAllSOACRUsecase(OrderRepository remoteRepository) {
        return new GetAllSOACRUsecase(remoteRepository);
    }

    @Provides
    GetAllRequestACRUsecase provideGetAllRequestACRUsecase(OrderRepository remoteRepository) {
        return new GetAllRequestACRUsecase(remoteRepository);
    }

    @Provides
    GetAllPackageForRequestUsecase provideGetAllPackageForRequestUsecase(OrderRepository remoteRepository) {
        return new GetAllPackageForRequestUsecase(remoteRepository);
    }

    @Provides
    GetAllDetailForSOACRUsecase provideGetAllDetailForSOACRUsecase(ProductRepository remoteRepository) {
        return new GetAllDetailForSOACRUsecase(remoteRepository);
    }

    @Provides
    GetMaxPackageForSOUsecase provideGetMaxPackageForSOUsecase(OrderRepository remoteRepository) {
        return new GetMaxPackageForSOUsecase(remoteRepository);
    }

    @Provides
    AddPackageACRUsecase provideAddPackageACRUsecase(OrderRepository remoteRepository) {
        return new AddPackageACRUsecase(remoteRepository);
    }

    @Provides
    AddPackageACRbyJsonUsecase provideAddPackageACRbyJsonUsecase(OrderRepository remoteRepository) {
        return new AddPackageACRbyJsonUsecase(remoteRepository);
    }

    @Provides
    AddLogScanACRUsecase provideAddLogScanACRUsecase(OrderRepository remoteRepository) {
        return new AddLogScanACRUsecase(remoteRepository);
    }

    @Provides
    AddLogScanbyJsonUsecase provideAddLogScanbyJsonUsecase(OrderRepository remoteRepository) {
        return new AddLogScanbyJsonUsecase(remoteRepository);
    }

    @Provides
    AddLogScanInStoreACRUsecase provideAddLogScanInStoreACRUsecase(OrderRepository remoteRepository) {
        return new AddLogScanInStoreACRUsecase(remoteRepository);
    }

    @Provides
    GetMaxTimesACRUsecase provideGetMaxTimesACRUsecase(OrderRepository remoteRepository) {
        return new GetMaxTimesACRUsecase(remoteRepository);
    }

    @Provides
    GetAllRequestACRInUsecase provideGetAllRequestACRInUsecase(OrderRepository remoteRepository) {
        return new GetAllRequestACRInUsecase(remoteRepository);
    }

    @Provides
    UpdateVersionUsecase provideUpdateVersionUsecase(AuthRepository remoteRepository) {
        return new UpdateVersionUsecase(remoteRepository);
    }

    @Provides
    GetDateServerUsecase provideGetDateServerUsecase(AuthRepository authRepository) {
        return new GetDateServerUsecase(authRepository);
    }

    @Provides
    DeletePackageDetailUsecase provideDeletePackageDetailUsecase(OrderRepository authRepository) {
        return new DeletePackageDetailUsecase(authRepository);
    }

    @Provides
    DeletePackageUsecase provideDeletePackageUsecase(OrderRepository authRepository) {
        return new DeletePackageUsecase(authRepository);
    }

    @Provides
    GetAllScanTurnOutUsecase provideAllScanTurnOutUsecase(OrderRepository orderRepository) {
        return new GetAllScanTurnOutUsecase(orderRepository);
    }
}

