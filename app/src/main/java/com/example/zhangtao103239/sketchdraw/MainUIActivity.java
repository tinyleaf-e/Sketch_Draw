package com.example.zhangtao103239.sketchdraw;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by 晔 on 2017/4/14.
 */

public class MainUIActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) return;
        setContentView(R.layout.main_ui_layout);
        getFragmentManager().beginTransaction().add(R.id.myFrameLayout,new newFragment()).commit();
    }

}
