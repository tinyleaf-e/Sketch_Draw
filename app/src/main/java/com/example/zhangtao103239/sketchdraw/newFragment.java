package com.example.zhangtao103239.sketchdraw;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 晔 on 2017/4/14.
 */

public class newFragment extends Fragment {

    public newFragment() {
        super();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View  view=inflater.inflate(R.layout.newlayout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.paintBoardButton)
    public void onPaintBoardClick(){
        //Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().replace(R.id.myFrameLayout,new paintFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.mypaintingButton)
    public void onmypaintingClick()
    {
        getFragmentManager().beginTransaction().replace(R.id.myFrameLayout,new mypaintingFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.settingButton)
    public void onsettingClick()
    {
        getFragmentManager().beginTransaction().replace(R.id.myFrameLayout,new settingFragment()).addToBackStack(null).commit();
    }

}
