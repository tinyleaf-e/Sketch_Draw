package com.example.zhangtao103239.sketchdraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by 13051 on 2017/4/6.
 */

public class PaintBoard extends View {
    private Paint mPaint = null;
    private Bitmap mBitmap = null;
    private Bitmap mBitmapNew = null;
    private Bitmap mBitmapOld = null;
    private Canvas mBitmapCanvas = null;
    private float startX;
    private float startY ;
    private File ImgDir;
    private String ImgName;
    private int paintSize;
    private int IsDrawing;
    public int onTouchState=1;// 1正常画画  2 拖动  3 放大 4 旋转
    double nLenStart = 0;//缩放前长度
    float nAngleStart = 0;//旋转前角度
    float MidPointX;
    float MidPointY;
    Matrix matrix = new Matrix();

    public PaintBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mBitmap = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0,0,0,0));
        mBitmapCanvas = new Canvas(mBitmap);
        mBitmapCanvas.drawColor(Color.TRANSPARENT);
        IsDrawing=1;
        setPaintSize(12);
        setPaintStyle();
        mPaint.setAntiAlias(true);  //消除锯齿
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (onTouchState)
        {
            case 1: //正常画画
            {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float stopX = event.getX();
                        float stopY = event.getY();
                        Log.e(TAG,"onTouchEvent-ACTION_MOVE\nstartX is "+startX+
                                " startY is "+startY+" stopX is "+stopX+ " stopY is "+stopY);
                        mBitmapCanvas.drawLine(startX, startY, stopX, stopY, mPaint);
                        startX = event.getX();
                        startY = event.getY();
                        invalidate();//call onDraw()
                        break;
                }
                return true;
            }
            case 2: //放大
            {
                int nCnt = event.getPointerCount();

                int n = event.getAction();
                if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == nCnt)
                {
                    for(int i=0; i< nCnt; i++)
                    {
                        float x = event.getX(i);
                        float y = event.getY(i);
                        Point pt = new Point((int)x, (int)y);
                    }
                    MidPointX=(event.getX(0)+event.getX(1))/2;
                    MidPointY=(event.getY(0)+event.getY(1))/2;
                    int xlen = Math.abs((int)event.getX(0) - (int)event.getX(1));
                    int ylen = Math.abs((int)event.getY(0) - (int)event.getY(1));
                    nLenStart = Math.sqrt((double)xlen*xlen + (double)ylen * ylen);
                    //mBitmapOld=mBitmap;
                    mBitmapOld=mBitmap.copy(Bitmap.Config.ARGB_8888,true);
                //}else if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP  && 2 == nCnt)
                }else if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE  && 2 == nCnt)
                {
                    for(int i=0; i< nCnt; i++)
                    {
                        float x = event.getX(i);
                        float y = event.getY(i);
                        Point pt = new Point((int)x, (int)y);
                    }
                    int xlen = Math.abs((int)event.getX(0) - (int)event.getX(1));
                    int ylen = Math.abs((int)event.getY(0) - (int)event.getY(1));
                    double nLenEnd = Math.sqrt((double)xlen*xlen + (double)ylen * ylen);
                    double rate =1+ (nLenEnd-nLenStart)/1000;
                        //放大
                        matrix.setScale((float) rate,(float) rate,MidPointX,MidPointY);
                        //mBitmapCanvas.scale((float) rate,(float)rate);
                        mBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        mBitmapCanvas.drawBitmap(mBitmapOld,matrix,null);
                        invalidate();
                        mBitmapNew=mBitmap;
                }
                return true;
            }
            case 3: //旋转
            {
                int nCnt = event.getPointerCount();

                int n = event.getAction();
                if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == nCnt)
                {
                    for(int i=0; i< nCnt; i++)
                    {
                        float x = event.getX(i);
                        float y = event.getY(i);
                        Point pt = new Point((int)x, (int)y);
                    }
                    MidPointX=(event.getX(0)+event.getX(1))/2;
                    MidPointY=(event.getY(0)+event.getY(1))/2;
                    nAngleStart = (float) Math.toDegrees(Math.atan((event.getY(0) - event.getY(1))/(event.getX(0) - event.getX(1))));
                    mBitmapOld=mBitmap.copy(Bitmap.Config.ARGB_8888,true);
                }else if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE  && 2 == nCnt)
                {
                    for(int i=0; i< nCnt; i++)
                    {
                        float x = event.getX(i);
                        float y = event.getY(i);
                        Point pt = new Point((int)x, (int)y);
                    }
                    float nAngleEnd = (float)Math.toDegrees(Math.atan((event.getY(0) - event.getY(1))/(event.getX(0) - event.getX(1))));

                    //TODO
                    matrix.setRotate(nAngleEnd-nAngleStart,MidPointX,MidPointY);
                    //mBitmapCanvas.scale((float) rate,(float)rate);
                    mBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mBitmapCanvas.drawBitmap(mBitmapOld,matrix,null);
                    invalidate();
                    mBitmapNew=mBitmap;
                }
                return true;
            }
            case 4: //移动
            {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        mBitmapOld=mBitmap.copy(Bitmap.Config.ARGB_8888,true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float stopX = event.getX();
                        float stopY = event.getY();

                        matrix.setTranslate(stopX-startX,stopY-startY);
                        mBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        mBitmapCanvas.drawBitmap(mBitmapOld,matrix,null);
                        invalidate();//call onDraw()
                        break;
                }
                return true;
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        }
    }

    public File saveBitmapToPNG() {
       return saveBitmapToPNG("forum-" + String.valueOf(System.currentTimeMillis()));
    }

    public File saveBitmapToPNG(String ImageName) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "SD *****>> SD卡不存在");
        } else {
            Log.i(TAG, "SD *****>> SD卡 存在");
        }
        // 创建图片保存目录
        ImgDir = new File(Environment.getExternalStorageDirectory(), "ForumAlbum");
        if (!ImgDir.exists()) {
            ImgDir.mkdir();
        }
        // 以系统时间命名文件
        //ImgName = "forum-" + String.valueOf(System.currentTimeMillis()) + ".png";
        ImgName = ImageName+".png";
        File file = new File(ImgDir, ImgName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void SavePNGtoCamera(Context context)
    {
        // 保存后要扫描一下文件，及时更新到系统目录（一定要加绝对路径，这样才能更新）
        MediaScannerConnection.scanFile(context,
                new String[]{Environment.getExternalStorageDirectory()
                        + File.separator + "ForumAlbum" + File.separator + ImgName},
                null, null);


        //会导入到camera相册
       /*try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), faceImgName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/


        //会导入到ForumAlbum相册
        /*Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);*/
        //return (Environment.getExternalStorageDirectory() + File.separator + "ForumAlbum" + File.separator + faceImgName);
    }
    public void reFresh()
    {
        mBitmap=Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
        mBitmapCanvas=new Canvas(mBitmap);
        this.invalidate();
    }

    public void DrawOrClear()
    {
        IsDrawing=IsDrawing^1;
        setPaintStyle();
    }

    private void setPaintStyle()
    {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        if (IsDrawing == 1) {
            mPaint.setStrokeWidth(paintSize);
            mPaint.setARGB(255,0,0,0);
        } else {//橡皮擦
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(50);
        }
        invalidate();
    }
    public void setPaintSize(int size)
    {
        paintSize=size;
        setPaintStyle();
    }
}
