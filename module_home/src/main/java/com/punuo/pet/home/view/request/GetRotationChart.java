package com.punuo.pet.home.view.request;

import com.punuo.pet.home.view.model.RotationChartData;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * http://feeder.qinqingonline.com:8080/xcx/shop/get_banner/home?shop_id=1
 */
public class GetRotationChart extends BaseRequest<RotationChartData> {
    public GetRotationChart(){
        setRequestType(RequestType.GET);
//        setRequestPath("/xcx/shop/get_banner/home");
        setRequestPath("/getRotationChart");
    }

//    @Override
//    public String getHost() {
//        return "feeder.qinqingonline.com";
//    }
//
//    @Override
//    public int getPort() {
//        return 8080;
//    }
//
//    @Override
//    public String getPrefixPath() {
//        return "";
//    }
}
