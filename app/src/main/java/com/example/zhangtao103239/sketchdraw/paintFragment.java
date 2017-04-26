package com.example.zhangtao103239.sketchdraw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.zhangtao103239.sketchdraw.R.string.paintBoard;

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
    @BindView(R.id.myPaintBoard_1)
    public PaintBoard paintBoard_1;
    @BindView(R.id.operate_confirm_button)
    public Button operate_confirm_button;
    @BindView(R.id.operate_cancel_button)
    public Button operate_cancel_button;


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
        operate_confirm_button.setVisibility(View.GONE);
        operate_cancel_button.setVisibility(View.GONE);
        paintBoard_1.onTouchState=1;
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

    @OnClick(R.id.paint_rotate_button)
    public void onpaint_rotate_buttonClick()
    {
        operate_confirm_button.setVisibility(View.VISIBLE);
        operate_cancel_button.setVisibility(View.VISIBLE);
        paintBoard_1.onTouchState=3;
    }

    @OnClick(R.id.paint_scale_button)
    public void onpaint_scale_buttonClick()
    {
        operate_confirm_button.setVisibility(View.VISIBLE);
        operate_cancel_button.setVisibility(View.VISIBLE);
        paintBoard_1.onTouchState=2;
    }

    @OnClick(R.id.paint_translate_button)
    public void onpaint_translate_buttonClick()
    {
        operate_confirm_button.setVisibility(View.VISIBLE);
        operate_cancel_button.setVisibility(View.VISIBLE);
        paintBoard_1.onTouchState=4;
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
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("退出").setMessage("即将退出，是否保存作品？")
                .setPositiveButton("保存并退出",new DialogInterface.OnClickListener() {
                        //@Override
                        public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                            dialog.dismiss();

                            final EditText et = new EditText(getActivity());

                            new AlertDialog.Builder(getActivity()).setTitle("搜索")
                                    .setIcon(android.R.drawable.ic_menu_save)
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        // @OverRide
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            if (input.equals("")) {
                                                Toast.makeText(getActivity(), "搜索内容不能为空！" + input, Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                File mfile;
                                                mfile=paintBoard_1.saveBitmapToPNG(getActivity(),input);
                                                paintBoard_1.SavePNGtoCamera(getActivity());
                                                Toast.makeText(getActivity(), "已保存",Toast.LENGTH_LONG).show();
                                                getFragmentManager().popBackStack();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();

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
