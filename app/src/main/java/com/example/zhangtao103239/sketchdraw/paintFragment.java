package com.example.zhangtao103239.sketchdraw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 晔 on 2017/4/14.
 */

public class paintFragment extends Fragment {
    @BindView(R.id.linearLayout_right)
    public LinearLayout linearLayout_right;
    @BindView(R.id.linerLaout_layer_operation)
    public LinearLayout linearLayout_layer_operation;
    @BindView(R.id.linerLayout_paint)
    public LinearLayout linearLayout_paint;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.painting_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.button_layer_show)
    public void onbutton_layer_showClick()
    {
        if(linearLayout_right.getVisibility()==View.VISIBLE)
        {
            linearLayout_right.setVisibility(View.GONE);
            linearLayout_layer_operation.setVisibility(View.GONE);
        }
        else
            linearLayout_right.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.paintShowButton)
    public void onpaintShowButtonClick()
    {
        if(linearLayout_paint.getVisibility()==View.VISIBLE)
            linearLayout_paint.setVisibility(View.GONE);
        else
            linearLayout_paint.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.operationShowButton)
    public void onoperationShowButtonClick()
    {
        if(linearLayout_layer_operation.getVisibility()==View.VISIBLE)
            linearLayout_layer_operation.setVisibility(View.GONE);
        else
            linearLayout_layer_operation.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.paint_save_button)
    public void onpaint_save_buttonClick()
    {
        Toast.makeText(this.getActivity(), "已保存", Toast.LENGTH_SHORT).show();
    }

    @OnClick (R.id.paint_exit_button)
    public void onpaint_exit_buttonClick()
    {
        Dialog dialog = new AlertDialog.Builder(this.getActivity())
                .setIcon(android.R.drawable.btn_star)
                .setTitle("退出").setMessage("即将退出，是否保存作品？")
                .setPositiveButton("保存并退出",new DialogInterface.OnClickListener() {
                        //@Override
                        public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "已保存",Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack();
                    }})
                .setNegativeButton("不保存退出", new DialogInterface.OnClickListener() {
                        // @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getFragmentManager().popBackStack();
                    }})
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    // @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }})
                .create();
        dialog.show();
    }
}
