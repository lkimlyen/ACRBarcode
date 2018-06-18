package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;
import rx.Observable;

public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();

    Observable<OrderModel> addItemAsyns(OrderModel model);

    Observable<List<OrderModel>> findAllOrder();

    Observable<String> deleteAllOrder();

    Observable<CustomerModel> addCustomer(CustomerModel customerModel);

    Observable<ProductModel> addProduct(ProductModel model);

    Observable<String> updateStatusProduct(int serverId);

    Observable<List<ProductModel>> findProductByOrderId(int orderId);

    Observable<String> addLogScanCreatePack(LogScanCreatePack item, int orderId, final String barcode);

    Observable<String> addLogCompleteCreatePack(int id, final int serverId, final int serial);

    Observable<OrderModel> findOrder(int orderId);

    Observable<LogScanCreatePackList> findAllLog(int orderId);

    Observable<HashMap<LogScanCreatePack, ProductModel>> findLogPrint(int orderId);

    Observable<String> deleteLogScanItem(final int id);

    Observable<String> updateNumberLog(final int id, final int number);

    Observable<String> deleteProduct();

    Observable<IPAddress> insertOrUpdateIpAddress(IPAddress model);

    Observable<IPAddress> findIPAddress();

    Observable<String> deleteAllLog();

    Observable<Integer> getSumLogPack(int orderId);

    Observable<List<OrderModel>> findOrderByLogComplete();
}
