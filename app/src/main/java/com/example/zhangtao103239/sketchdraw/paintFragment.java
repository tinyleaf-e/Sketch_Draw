package com.example.zhangtao103239.sketchdraw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.zhangtao103239.sketchdraw.R.string.paintBoard;

/**
 * Created by 晔 on 2017/4/14.
 */

public class paintFragment extends Fragment {
    private File mfile;
    private static final int POST_ERROR = 0,POST_SUCCESS = 1,GET_ERROR = 3,GET_SUCCESS = 4;
    private static final String IMGUR_CLIENT_ID = "test";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();

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

    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case POST_ERROR:
                    Toast.makeText(getActivity(),"failed",Toast.LENGTH_SHORT).show();
                    break;
                case POST_SUCCESS:
                    Toast.makeText(getActivity(), "succeed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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
        final EditText et = new EditText(getActivity());

        new AlertDialog.Builder(getActivity()).setTitle("保存")
                .setIcon(android.R.drawable.ic_menu_save)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    // @OverRide
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getActivity(), "请重新输入！" + input, Toast.LENGTH_LONG).show();
                        }
                        else {
                            mfile=paintBoard_1.saveBitmapToPNG(input);
                            paintBoard_1.SavePNGtoCamera(getActivity());
                            Toast.makeText(getActivity(), "已保存",Toast.LENGTH_LONG).show();
                            //getFragmentManager().popBackStack();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
        //Toast.makeText(this.getActivity(), "已保存", Toast.LENGTH_SHORT).show();
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

                            new AlertDialog.Builder(getActivity()).setTitle("保存")
                                    .setIcon(android.R.drawable.ic_menu_save)
                                    .setView(et)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        // @OverRide
                                        public void onClick(DialogInterface dialog, int which) {
                                            String input = et.getText().toString();
                                            if (input.equals("")) {
                                                Toast.makeText(getActivity(), "请重新输入！" + input, Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                mfile=paintBoard_1.saveBitmapToPNG(input);
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

    /*public void OnSaveClicked(View view){
        mfile=paintBoard_1.saveBitmapToPNG();
        paintBoard_1.SavePNGtoCamera(getActivity());
        Toast.makeText(getActivity(),mfile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }*/

    @OnClick(R.id.button_size_s)
    public void sPaintClicked(View view)
    {
      paintBoard_1.setPaintSize(6);
    }

    @OnClick(R.id.button_size_m)
    public void mPaintClicked(View view)
    {
        paintBoard_1.setPaintSize(12);
    }

    @OnClick(R.id.button_size_l)
    public void lPaintClicked(View view)
    {
        paintBoard_1.setPaintSize(18);
    }

    @OnClick(R.id.button_upload)
    public void OnUploadClicked(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PostPic();
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(POST_ERROR);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void OnDownloadClicked(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    GetPic();
                } catch (Exception e){
                    mHandler.sendEmptyMessage(GET_ERROR);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void OnFreshClicded(View view){
        paintBoard_1.reFresh();
    }

    public void OnClearClicked(View view){
        paintBoard_1.DrawOrClear();
    }

    private void PostPic() throws Exception{
        mfile=paintBoard_1.saveBitmapToPNG();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Title")
                .addFormDataPart("file", mfile.getName(),RequestBody.create(MEDIA_TYPE_PNG, mfile))
                .build();
        mfile.delete();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("http://192.168.10.175:8080/uploads")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
        {
            throw new IOException("Unexpected code " + response);
        }
        else mHandler.sendEmptyMessage(POST_SUCCESS);

        //System.out.println(response.body().string());
        //Toast.makeText(this,"succeed",Toast.LENGTH_SHORT).show();
    }

    private void GetPic()throws Exception{
        OkHttpClient client = new OkHttpClient();
        String url="http://192.168.10.175:8080/uploads";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        InputStream ips = body.byteStream();
        Bitmap bim = BitmapFactory.decodeStream(ips);

        if(!response.isSuccessful())
        {
            throw new IOException("Unexpected code " + response);
        }
        Message msg = new Message();
        msg.what =GET_SUCCESS;
        msg.obj = bim;
        mHandler.sendMessage(msg);

    }
}
