package com.punuo.pet.home.feed.request;

import com.punuo.pet.home.feed.model.FeedingAdviceModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 * 获取每日建议喂食
 **/
public class GetFeedingAdviceRequest extends BaseRequest<FeedingAdviceModel> {

    public GetFeedingAdviceRequest() {
        setRequestPath("/pets/getFeedingAdvice");
        setRequestType(RequestType.GET);
    }
}
