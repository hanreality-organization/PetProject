package com.punuo.sys.app.video.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class DeleteVideoRequest extends BaseRequest<BaseModel> {
    public DeleteVideoRequest() {
        setRequestPath("/users/moveDetectVideoDel");
        setRequestType(RequestType.GET);
    }
}
