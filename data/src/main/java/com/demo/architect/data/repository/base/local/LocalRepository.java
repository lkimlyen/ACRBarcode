package com.demo.architect.data.repository.base.local;

import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.ImportWorksModel;
import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.LogCompleteMainList;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.architect.data.model.offline.ScanWarehousingModel;

import java.util.HashMap;
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

    Observable<String> updateStatusAndNumberProduct(int serverId);

    Observable<String> updateStatusLog(int logId);

    Observable<List<ProductModel>> findProductByOrderId(int orderId);

    Observable<String> addLogScanCreatePack(LogScanCreatePack item, int orderId, final String barcode);

    Observable<String> addLogCompleteCreatePack(int id, final int serverId, final int serial, final int numTotal, final String dateCreate);

    Observable<String> addLogCompleteCreatePack(final LogCompleteCreatePack model, final int serverId);

    Observable<OrderModel> findOrder(int orderId);

    Observable<LogScanCreatePackList> findAllLog(int orderId);

    Observable<HashMap<LogScanCreatePack, ProductModel>> findLogPrint(int orderId);

    Observable<String> deleteLogScanItem(final int id);

    Observable<String> updateNumberLog(final int id, final int number);

    Observable<String> deleteProduct();

    Observable<IPAddress> insertOrUpdateIpAddress(IPAddress model);

    Observable<IPAddress> findIPAddress();

    Observable<String> deleteAllLog();

    Observable<String> deleteLogCompleteAll();

    Observable<Integer> getSumLogPack(int orderId);

    Observable<List<OrderModel>> findOrderByLogComplete();

    Observable<LogCompleteMainList> findPackage(int orderId);

    Observable<LogCompleteCreatePackList> findLogCreatePack(int logId);

    Observable<LogCompleteCreatePackList> findLogCompletById(int logId);

    Observable<String> deleteLogComplete(int id, int logId);

    Observable<String> deletePack(int logId, final int orderId);

    Observable<Boolean> checkExistCode(int logId, String barcode);

    Observable<Integer> countCodeNotUp(int logId);

    Observable<Boolean> checkExistBarcodeInWarehousing(String barcode);

    Observable<Boolean> checkExistImportWorks(String barcode);

    Observable<Boolean> checkExistBarcodeInDelivery(String barcode);

    Observable<ScanWarehousingModel> addScanWareHousing(ScanWarehousingModel scanWarehousingModel);

    Observable<String> addScanDelivery(final ScanDeliveryModel model, final int times, final String codeRequest);


    Observable<ScanDeliveryList> findScanDelivery(int times, String requestCode);

    Observable<ScanDeliveryList> findScanDeliveryNotComplete(String requestCode);

    Observable<String> updateStatusScanDelivery(final int id, final HashMap<String, Integer> map);

    Observable<ImportWorksModel> addImportWorks(ImportWorksModel model);

    Observable<List<LogScanCreatePack>> logCreateToJson(final int id);

    Observable<List<ScanDeliveryModel>> deliveryToJson(final String request);
}
