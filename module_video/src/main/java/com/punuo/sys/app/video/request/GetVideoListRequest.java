package com.punuo.sys.app.video.request;

import com.punuo.sys.app.video.model.VideoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetVideoListRequest extends BaseRequest<VideoModel> {
    public GetVideoListRequest(){
        setRequestPath("/movedetectvideo/getVideoUrl");
        setRequestType(RequestType.GET);
    }
}
