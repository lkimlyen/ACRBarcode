package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogDeleteCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class LocalRepositoryImpl implements LocalRepository {

    DatabaseRealm databaseRealm;

    public LocalRepositoryImpl() {
        databaseRealm = new DatabaseRealm();
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.add(model);

                    subscriber.onNext(model.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<MessageModel>>() {
            @Override
            public void call(Subscriber<? super List<MessageModel>> subscriber) {
                try {
                    List<MessageModel> models = databaseRealm.findAll(MessageModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<OrderModel> addItemAsyns(final OrderModel model) {
        return Observable.create(new Observable.OnSubscribe<OrderModel>() {
            @Override
            public void call(Subscriber<? super OrderModel> subscriber) {
                try {
                    databaseRealm.addItemAsync(model);
                    LogScanCreatePackList packList = new LogScanCreatePackList(model.getId());
                    databaseRealm.insertOrUpdate(packList);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<OrderModel>> findAllOrder() {
        return Observable.create(new Observable.OnSubscribe<List<OrderModel>>() {
            @Override
            public void call(Subscriber<? super List<OrderModel>> subscriber) {
                try {
                    List<OrderModel> models = databaseRealm.findAll(OrderModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteAllOrder() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteAll(OrderModel.class);

                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<CustomerModel> addCustomer(final CustomerModel customerModel) {
        return Observable.create(new Observable.OnSubscribe<CustomerModel>() {
            @Override
            public void call(Subscriber<? super CustomerModel> subscriber) {
                try {
                    databaseRealm.addItemAsync(customerModel);

                    subscriber.onNext(customerModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ProductModel> addProduct(final ProductModel model) {
        return Observable.create(new Observable.OnSubscribe<ProductModel>() {
            @Override
            public void call(Subscriber<? super ProductModel> subscriber) {
                try {
                    databaseRealm.addItemAsync(model);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusProduct(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusProduct(orderId);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<ProductModel>> findProductByOrderId(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<List<ProductModel>>() {
            @Override
            public void call(Subscriber<? super List<ProductModel>> subscriber) {
                try {
                    List<ProductModel> models = databaseRealm.findProductByOrderId(orderId);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogScanCreatePack> addLogScanCreatePack(final LogScanCreatePack item, final int orderId) {
        return Observable.create(new Observable.OnSubscribe<LogScanCreatePack>() {
            @Override
            public void call(Subscriber<? super LogScanCreatePack> subscriber) {
                try {
                    databaseRealm.addLogScanCreatePackAsync(item, orderId);
                    subscriber.onNext(item);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<OrderModel> findOrder(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<OrderModel>() {
            @Override
            public void call(Subscriber<? super OrderModel> subscriber) {
                try {
                    OrderModel model = databaseRealm.findOrderByOrderId(orderId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogScanCreatePackList> findAllLog(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<LogScanCreatePackList>() {
            @Override
            public void call(Subscriber<? super LogScanCreatePackList> subscriber) {
                try {
                    LogScanCreatePackList model = databaseRealm.findLogById(orderId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteLogScanItem(final LogScanCreatePack item) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String newFormat = formatter.format(new Date());
                    LogDeleteCreatePack model = new LogDeleteCreatePack(databaseRealm.getIdCurrent() + 1, item.getBarcode(), item.getOrderId(),
                            item.getDeviceTime(), item.getServerTime(), item.getLatitude(),
                            item.getLongitude(), item.getCreateByPhone(),
                            item.getSerial(), item.getNumCodeScan(),
                            item.getNumTotal(), item.getNumPack(), item.getNumInput(), item.getCreateBy(),
                            newFormat);
                    databaseRealm.addItemAsync(model);
                    databaseRealm.deleteLogCreateAsync(item.getId());
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberLog(final int number, final int id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {


                    databaseRealm.updateNumber(number, id);
                    subscriber.onNext(String.valueOf(id));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteProduct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteAll(ProductModel.class);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberRestProduct(final int number, final int orderId, final int productId, final int serial) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberRestProduct(number, orderId, productId, serial);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model) {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    databaseRealm.insertOrUpdate(model);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<IPAddress> findIPAddress() {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    IPAddress ipAddress = databaseRealm.findFirst(IPAddress.class);
                    subscriber.onNext(ipAddress);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


}
