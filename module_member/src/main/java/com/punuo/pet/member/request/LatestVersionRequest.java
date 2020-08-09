package com.punuo.pet.member.request;

import com.punuo.pet.member.version.VersionModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2020/8/3.
 **/
public class LatestVersionRequest extends BaseRequest<VersionModel> {

    public LatestVersionRequest() {
        setRequestPath("/latestVersion");
        setRequestType(RequestType.GET);
    }
}
