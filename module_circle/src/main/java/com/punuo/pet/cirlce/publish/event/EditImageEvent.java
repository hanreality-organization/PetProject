package com.punuo.pet.cirlce.publish.event;

import java.util.List;

/**
 * Created by Kuiya on 2019/8/8.
 */

public class EditImageEvent {
    public List<String> mImages;

    public EditImageEvent(List<String> images) {
        mImages = images;
    }
}
