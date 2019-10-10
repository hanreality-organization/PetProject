package com.punuo.pet.cirlce.publish.request;

import com.punuo.sys.sdk.httplib.BaseRequest;

import okhttp3.MediaType;

/**
 * Created by Kuiya on 2019/8/8.
 */

public class UploadPostRequest extends BaseRequest {

    public UploadPostRequest() {
        setRequestPath("/circle/insertPost");
        setRequestType(RequestType.UPLOAD);
        contentType(MediaType.parse("multipart/form-data; charset=utf-8"));
    }
}
