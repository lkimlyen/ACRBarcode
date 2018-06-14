package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogDeleteCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;

import java.util.List;

import rx.Observable;

public interface LocalRepository {

    Observable<String> add(MessageModel model);

    Observable<List<MessageModel>> findAll();

    Observable<OrderModel> addItemAsyns(OrderModel model);

    Observable<List<OrderModel>> findAllOrder();

    Observable<String> deleteAllOrder();

    Observable<CustomerModel> addCustomer(CustomerModel customerModel);

    Observable<ProductModel> addProduct(ProductModel model);

    Observable<String> updateStatusProduct(int orderId);

    Observable<List<ProductModel>> findProductByOrderId(int orderId);

    Observable<String> addLogScanCreatePack(LogScanCreatePack item, int orderId, final String barcode);

    Observable<OrderModel> findOrder(int orderId);

    Observable<LogScanCreatePackList> findAllLog(int orderId);

    Observable<String> deleteLogScanItem(LogScanCreatePack item);

    Observable<String> updateNumberLog(int number, int id);

    Observable<String> deleteProduct();

    Observable<String> updateNumberRestProduct(int number, int orderId,int productId, int serial);

    Observable<IPAddress> insertOrUpdateIpAddress(IPAddress model);

    Observable<IPAddress> findIPAddress();

    Observable<String> deleteAllLog();
}
