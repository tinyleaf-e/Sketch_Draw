package com.example.zhangtao103239.sketchdraw;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 晔 on 2017/4/27.
 */

public class picSelectFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.pic_dialog,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.button_pic_cancel)
    public void onbutton_pic_cancelClick()
    {
        getFragmentManager().popBackStack();
    }
}
