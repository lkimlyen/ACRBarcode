package com.demo.architect.data.repository.base.product.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductEntity;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class ProductRepositoryImpl implements ProductRepository {
    private final static String TAG = ProductRepositoryImpl.class.getName();

    private ProductApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public ProductRepositoryImpl(ProductApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
    }

    private void handleProductResponse(Call<BaseListResponse<ProductEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ProductEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    @Override
    public Observable<BaseListResponse<ProductEntity>> getAllDetailForSOACR(final int requestId) {
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ProductEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ProductEntity>> subscriber) {
                handleProductResponse(mRemoteApiInterface.getAllDetailForSOACR(
                        server+"/WS/api/GetAllDetailForSOACR",requestId), subscriber);
            }
        });
    }
}
