package com.punuo.sys.app.video.request;

import com.punuo.sys.app.video.model.MusicModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2020-01-04.
 **/
public class GetMusicListRequest extends BaseRequest<MusicModel> {
    public GetMusicListRequest() {
        setRequestPath("/users/getAuidoFileList");
        setRequestType(RequestType.GET);
    }
}
