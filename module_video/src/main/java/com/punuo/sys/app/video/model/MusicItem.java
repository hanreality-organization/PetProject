package com.punuo.sys.app.video.model;

/**
 * Created by han.chen.
 * Date on 2020-01-04.
 **/
public class MusicItem {
    public String url;
    public boolean selected;

    public String getFileName() {
        if (url != null) {
            return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        } else {
            return "";
        }
    }
}
