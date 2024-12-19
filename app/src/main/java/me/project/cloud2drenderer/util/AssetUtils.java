package me.project.cloud2drenderer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class AssetUtils {
    private final static String TAG = AssetUtils.class.getSimpleName();


    public static boolean isAssetExist(Context ctx,String rootDirectory,String filepath) {
        String[] strings;
        int separatorIdx = filepath.lastIndexOf('/');
        String filename = filepath.substring(separatorIdx+1);
        String subDirectory = filepath.substring(0,separatorIdx);
        try {
            strings = ctx.getAssets().list( rootDirectory+"/"+subDirectory);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return Arrays.asList(strings).contains(filename);
    }
    public static Bitmap readBitmapResource(Context ctx, int resourceId){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeResource(ctx.getResources(), resourceId, options);
    }

    public static Bitmap flipBitmapVertically(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(1,-1);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }


    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap;

        String suffix = FileUtils.getFileSuffix(filePath);
        try {
            istr = assetManager.open(filePath);
            if(suffix.equals(".tga")){
                bitmap = TargaUtils.decode(istr);
            }else if(suffix.equals(".bmp")) {
                bitmap = BitmapFactory.decodeStream(istr);

            }else {
                bitmap = BitmapFactory.decodeStream(istr);
            }
        } catch (IOException e) {
            // handle exception
            throw new RuntimeException(e);
        }
        assert bitmap!=null;


        return flipBitmapVertically(bitmap);
    }

    public static String readAssetFile(Context ctx, String fileName){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader( new InputStreamReader(ctx.getAssets().open(fileName)) );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return  sb.toString();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return null;
    }

    public static float wrapTo2PI(float v) {
        float pi2 = (float) Math.PI*2;
        while (v > pi2){
            v -= pi2;
        }
        while (v < 0){
            v += pi2;
        }
        return v;
    }
}