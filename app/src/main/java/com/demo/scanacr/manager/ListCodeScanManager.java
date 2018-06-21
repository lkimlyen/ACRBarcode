package com.demo.scanacr.manager;

import com.demo.architect.data.model.CodeOutEntity;
import com.demo.architect.data.model.PackageEntity;

import java.util.ArrayList;
import java.util.List;

public class ListCodeScanManager {
    private List<CodeOutEntity> listCodeScan = new ArrayList<>();
    private static ListCodeScanManager instance;

    public static ListCodeScanManager getInstance() {
        if (instance == null) {
            instance = new ListCodeScanManager();
        }
        return instance;
    }

    public List<CodeOutEntity> getListCodeScan() {
        return listCodeScan;
    }

    public void setListCodeScan(List<CodeOutEntity> listCodeScan) {
        this.listCodeScan = listCodeScan;
    }

    public CodeOutEntity getCodeScanByBarcode(String barcode) {
        for (CodeOutEntity codeOutEntity : listCodeScan) {
            if (codeOutEntity.getBarcode().equals(barcode)) {
                return codeOutEntity;
            }
        }
        return null;
    }

}
