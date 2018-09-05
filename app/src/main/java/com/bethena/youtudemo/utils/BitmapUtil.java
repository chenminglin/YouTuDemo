package com.bethena.youtudemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class BitmapUtil {

    public static Bitmap getBitmap(String path , int maxWidth, int maxHeight){
        //先解析图片边框的大小
        Bitmap bm = null;
        File file = new File(path);
        if(file.exists()) {
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inJustDecodeBounds = true;
            ops.inSampleSize = 1;
            BitmapFactory.decodeFile(path, ops);
            int oHeight = ops.outHeight;
            int oWidth = ops.outWidth;

            //控制压缩比
            int contentHeight = maxWidth;
            int contentWidth = maxHeight;
            if (((float) oHeight / contentHeight) < ((float) oWidth / contentWidth)) {
                ops.inSampleSize = (int) Math.ceil((float) oWidth / contentWidth);
            } else {
                ops.inSampleSize = (int) Math.ceil((float) oHeight / contentHeight);
            }
            ops.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, ops);

        }

        return bm;
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) throws IOException {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
