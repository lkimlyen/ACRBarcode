package com.demo.architect.data.repository.base.product.remote;

import com.demo.architect.data.BaseListResponse;
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

    public ProductRepositoryImpl(ProductApiInterface mRemoteApiInterface) {
        this.mRemoteApiInterface = mRemoteApiInterface;
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
                handleProductResponse(mRemoteApiInterface.getAllDetailForSOACR(requestId), subscriber);
            }
        });
    }
}
