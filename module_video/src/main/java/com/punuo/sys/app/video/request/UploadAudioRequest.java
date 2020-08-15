package com.punuo.sys.app.video.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.httplib.upload.UploadResult;

import okhttp3.MediaType;

/**
 * Created by han.chen.
 * Date on 2020/8/15.
 **/
public class UploadAudioRequest extends BaseRequest<UploadResult> {

    public UploadAudioRequest() {
        setRequestType(RequestType.UPLOAD);
        setRequestPath("/users/addAudiofiles");
        contentType(MediaType.parse("multipart/form-data"));
    }
}
