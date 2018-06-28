package com.demo.architect.data.helper;

/**
 * Created by uyminhduc on 4/5/17.
 */

public class RealmHelper {
    private boolean aBoolean = false;
    private static RealmHelper instance;

    public static RealmHelper getInstance() {
        if (instance == null) {
            instance = new RealmHelper();
        }
        return instance;
    }
    public void initRealm(boolean aBoolean){
        this.aBoolean = aBoolean;
    }

    public boolean getInitRealm(){
        return aBoolean;
    }
}
