package com.demo.scanacr.screen.history_pack;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.offline.LogCompleteMainList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPackagePresenter implements HistoryPackageContract.Presenter {

    private final String TAG = HistoryPackagePresenter.class.getName();
    private final HistoryPackageContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    HistoryPackagePresenter(@NonNull HistoryPackageContract.View view) {
        this.view = view;

    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);

    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getRequestProduce() {
        localRepository.findOrderByLogComplete().subscribe(new Action1<List<OrderModel>>() {
            @Override
            public void call(List<OrderModel> orderModels) {
                List<OrderModel> list = new ArrayList<>();
                list.add(new OrderModel(CoreApplication.getInstance().getString(R.string.text_choose_request_produce)));
                list.addAll(orderModels);
                view.showRequestProduction(list);
            }
        });
    }

    @Override
    public void search(int orderId) {
        localRepository.findPackage(orderId).subscribe(new Action1<LogCompleteMainList>() {
            @Override
            public void call(LogCompleteMainList logCompleteMainList) {
                view.showListHistory(logCompleteMainList);
            }
        });
    }
}
