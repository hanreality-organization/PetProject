package com.punuo.pet.cirlce.publish.event;

import java.util.List;

/**
 * Created by Kuiya on 2019/8/8.
 */

public class ChooseImageResultEvent {
    public List<String> mImages;

    public ChooseImageResultEvent(List<String> images) {
        mImages = images;
    }
}
