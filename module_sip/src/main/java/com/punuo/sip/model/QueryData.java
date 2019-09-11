package com.punuo.sip.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by han.chen.
 * Date on 2019-08-21.
 **/
public class QueryData {
    @SerializedName("query_response")
    public QueryResponse mQueryResponse;

    public static class QueryResponse {
        @SerializedName("resolution")
        public String resolution;
    }
}
