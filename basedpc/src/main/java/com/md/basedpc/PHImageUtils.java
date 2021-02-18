package com.md.basedpc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具类
 * Created by 朱大可 on 2018/1/4.
 */

public class PHImageUtils {
    /**
     * 根据流资源保存图片到指定本地地址
     *
     * @param context
     * @param data
     * @param path
     * @param cameraFace
     * @return
     */
    public String savePic(Context context, byte[] data, String path, int cameraFace) {
        File pictureFile = new File(path);
        if (pictureFile.exists()) {
            pictureFile.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            int degree = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                degree = cameraInfo.orientation;   
                if (cameraInfo.facing == cameraFace) {
                    break;
                }
            }
            boolean flipHorizontal;
            if (cameraFace == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                flipHorizontal = true;
            } else {
                flipHorizontal = false;
            }

            realImage = changeBitmap(realImage, degree, flipHorizontal);
            boolean isSaveSuccess = realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            if (!isSaveSuccess) {
                return null;
            }
        } catch (Exception e) {
            Log.e("",e.getMessage());
            return null;
        }
        return path;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param degree
     * @param flip
     * @return
     */
    private Bitmap changeBitmap(Bitmap bitmap, int degree, boolean flip) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix outMatrix;
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(degree);

        Matrix scaleMatrix = new Matrix();

        if (flip) {
            scaleMatrix.postScale(1, -1);   //镜像垂直翻转
            scaleMatrix.postConcat(rotateMatrix);
            outMatrix = scaleMatrix;
        } else {
            outMatrix = rotateMatrix;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, outMatrix, true);
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    /**
     * 将本地图片转为byte[]类型
     *
     * @param imgPath
     * @return
     */
    public byte[] getByteImage(String imgPath) {
        File file = new File(imgPath);
        Log.e("上传图片大小", file.length() + "");
        Bitmap bitmap = getBitmap(imgPath);//filePath
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param path
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public void compressAndGenImage(Context context, String path, String outPath, int maxSize) throws IOException {
        File sourceFile = new File(path);
        long sourceSize = sourceFile.length();
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap image = BitmapFactory.decodeFile(path, newOpts);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            if (options <= 100 && options >= 0){
                image.compress(Bitmap.CompressFormat.JPEG, options, os);
            }
        }

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
        File currentFile = new File(outPath);
    }

    /*
     * 保存文件，文件名为当前日期
     */
    public void saveBitmap(Context context, Bitmap bitmap, String bitName){
        String fileName ;
        File file ;
        if(Build.BRAND .equals("Xiaomi") ){ // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName ;
        }else{ // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+bitName ;
        }
        file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
            {
                out.flush();
                out.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param path 文件路径
     * @throws Exception
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) {
        File file = new File(path);
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("",e.getMessage());
        }
        byte[] buffer = new byte[(int) file.length()];
        try {
            inputFile.read(buffer);
        } catch (IOException e) {
            Log.e("",e.getMessage());
        }
        try {
            inputFile.close();
        } catch (IOException e) {
            Log.e("",e.getMessage());
        }
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param path 文件路径
     * @throws Exception
     * @since JDK 1.6
     */
    public static byte[] getByteImageFile(String path) {
        File file = new File(path);
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("",e.getMessage());
        }
        byte[] buffer = new byte[(int) file.length()];
        try {
            inputFile.read(buffer);
        } catch (IOException e) {
            Log.e("",e.getMessage());
        }
        try {
            inputFile.close();
        } catch (IOException e) {
            Log.e("",e.getMessage());
        }
        return buffer;
    }

    /***
     * 获取本地图片宽度
     * @param path
     * @return
     */
    public static int getLocalPicWidth(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        if (bmp == null) {
            Log.e("getLocalPicWidth", "通过options获取到的bitmap为空 ===");
        }
        return options.outWidth;
    }

    /***
     * 获取本地图片高度
     * @param path
     * @return
     */
    public static int getLocalPicHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        if (bmp == null) {
            Log.e("getLocalPicHeight", "通过options获取到的bitmap为空 ===");
        }
        return options.outHeight;
    }
}
