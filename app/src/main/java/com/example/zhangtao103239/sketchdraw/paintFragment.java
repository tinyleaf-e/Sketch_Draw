package com.example.zhangtao103239.sketchdraw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
    public picSelectFragment mypicSelectFragment=new picSelectFragment();
    private ArrayList<String> UrlPool=new ArrayList<String>();
    private int pic_id;

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
      paintBoard_1.onTouchState=1;
      paintBoard_1.setPaintSize(6);
    }

    @OnClick(R.id.button_size_m)
    public void mPaintClicked(View view)
    {
        paintBoard_1.onTouchState=1;
        paintBoard_1.setPaintSize(12);
    }

    @OnClick(R.id.button_size_l)
    public void lPaintClicked(View view)
    {
        paintBoard_1.onTouchState=1;
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

        CreateShowDialog();
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

    private void CreateShowDialog()
    {
        /*暂时用了生成图片url数组*/
        UrlPool.add(0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493897313&di=975a78102da1fbd55cd09b123e74ef47&imgtype=jpg&er=1&src=http%3A%2F%2Fimg3.ptpcp.com%2Fv2%2Fthumb%2Fjpg%2FNjY5MSw2MDAsMTAwLDQsMywxLC0xLDEs%2Fu%2F6EBC6208E20D6B024A85EE3905E83C11BF6709DB587A64F44141629DE67998ACF20997EDB1E36BC352BBB0FDFAD525D50234048FD3FFDDD8.jpg");
        UrlPool.add(1,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493908741&di=df049e2572f653c8f75baa66088fc057&imgtype=jpg&er=1&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3Db55cbec80ef3d7ca0ca33772c72f923f%2Faec379310a55b319c6ba7b8341a98226cefc1780.jpg");
        UrlPool.add(2,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493314023521&di=e729e36efeba06f6f2202c6c6a692ef6&imgtype=0&src=http%3A%2F%2Fp1.gexing.com%2FG1%2FM00%2F8A%2FE0%2FrBACE1PZi0HAabtGAAB7xjCrO-0206.jpg");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.pic_select_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final ImageView imageView = (ImageView) dialog.findViewById(R.id.show_view);
        Button pre_btn = (Button) dialog.findViewById(R.id.pre_button);
        Button nex_btn = (Button) dialog.findViewById(R.id.nex_button);
        Button cancel_btn = (Button) dialog.findViewById(R.id.updlg_cancel_btn);
        Button confirm_btn = (Button) dialog.findViewById(R.id.updlg_confirm_btn);



        pic_id = 0;
        String internetUrl = UrlPool.get(pic_id);
        Glide
                .with(this)
                .load(internetUrl)
                .into(imageView);

        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pic_id>=1)
                {
                String internetUrl = UrlPool.get(--pic_id);
                Glide
                        .with(getActivity())
                        .load(internetUrl)
                        .into(imageView);
                }
                else
                    Toast.makeText(getActivity(),"已经是第一张了",Toast.LENGTH_LONG).show();
            }
        });

        nex_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pic_id<UrlPool.size()-1)
                {
                    String internetUrl = UrlPool.get(++pic_id);
                    Glide
                            .with(getActivity())
                            .load(internetUrl)
                            .into(imageView);
                }
                else
                    Toast.makeText(getActivity(),"已经是最后一张了",Toast.LENGTH_LONG).show();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.cancel();
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("替换").setMessage("即将用此图片覆盖画板，是否确定？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int which) {
                                dialog.dismiss();
                                imageView.setDrawingCacheEnabled(true);
                                Bitmap bitmap=Bitmap.createBitmap(imageView.getDrawingCache());
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                // 设置想要的大小
                                int newWidth = paintBoard_1.Pic_Width;
                                int newHeight = paintBoard_1.Pic_Height;
                                // 计算缩放比例
                                float scaleWidth = ((float) newWidth) / width;
                                float scaleHeight = ((float) newHeight) / height;
                                // 取得想要缩放的matrix参数
                                Matrix matrix = new Matrix();
                                matrix.postScale(scaleWidth, scaleHeight);
                                // 得到新的图片
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                                        true);
                                imageView.setDrawingCacheEnabled(false);

                                paintBoard_1.setmBitmap(bitmap);
                            }
                        }).show();
            }
        });
    }
}
