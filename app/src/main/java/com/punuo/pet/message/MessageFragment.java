package com.punuo.pet.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.punuo.pet.R;
import com.punuo.sys.sdk.fragment.BaseFragment;

/**
 * Created by han.chen.
 * Date on 2019-06-25.
 **/
public class MessageFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_message, container, false);
        return mFragmentView;
    }
}