package com.demo.architect.data.repository.base.account.remote;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.model.UserResponse;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface AuthRepository {
    Observable<UserResponse> login(String username, String password, String type);
    Observable<BaseResponse> changePassword(String userId, String oldPass, String newPass);
    Observable<BaseResponse> updateSoft(String appCode, int userId, String version, int numNotUpdate,
                                        String dateServer, String ime);
    Observable<UpdateAppResponse> getUpdateVersionACR();
    Observable<String> getDateServer();
}
