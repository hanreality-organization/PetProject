package com.punuo.pet;

import com.punuo.pet.model.PetModel;
import com.punuo.pet.request.GetPetInfoRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by han.chen.
 * Date on 2019-07-11.
 **/
public class PetManager {

    private static GetPetInfoRequest mGetPetInfoRequest;

    public static void getPetInfo() {
        if (mGetPetInfoRequest != null && !mGetPetInfoRequest.isFinish()) {
            return;
        }
        mGetPetInfoRequest = new GetPetInfoRequest();
        mGetPetInfoRequest.addUrlParam("userName", AccountManager.getUserInfo().userName);
        mGetPetInfoRequest.setRequestListener(new RequestListener<PetModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(PetModel result) {
                if (result != null && result.success) {
                    EventBus.getDefault().post(result);
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(mGetPetInfoRequest);
    }
}