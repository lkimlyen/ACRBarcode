package com.demo.scanacr.manager;

import com.demo.architect.data.model.PackageEntity;
import java.util.ArrayList;
import java.util.List;

public class ListPackageManager {
    private List<PackageEntity> listPackage = new ArrayList<>();
    private static ListPackageManager instance;

    public static ListPackageManager getInstance() {
        if (instance == null) {
            instance = new ListPackageManager();
        }
        return instance;
    }

    public List<PackageEntity> getListPackage() {
        return listPackage;
    }

    public void setListPackage(List<PackageEntity> listPackage) {
        this.listPackage = listPackage;
    }

    public PackageEntity getPackageByBarcode(String request, int serial) {
        for (PackageEntity packageEntity : listPackage) {
            if (packageEntity.getCodeSX().equals(request) && packageEntity.getSTT() == serial) {
                return packageEntity;
            }
        }
        return null;
    }

}
