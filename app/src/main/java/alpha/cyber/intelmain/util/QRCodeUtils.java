package alpha.cyber.intelmain.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by huxin on 16/6/2.
 */
public class QRCodeUtils {

    public static Bitmap generateQRCode(String content,int width,int height,Bitmap logoBitmap,String filepath){
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,width,height);
            return bitMatrix2Bitmap(matrix,logoBitmap,filepath);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap bitMatrix2Bitmap(BitMatrix matrix,Bitmap logoBitmap,String filePath){
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w*h];
        for(int i=0;i<w;i++){
            for(int j=0;j<h;j++){
                int color = Color.WHITE;
                if(matrix.get(i,j)){
                    color = Color.BLACK;
                }
                rawData[i+(j*w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData,0,w,0,0,w,h);

        try{
            if(null!=logoBitmap){
                bitmap = addLogo(bitmap,logoBitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 150, new FileOutputStream(filePath));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 7 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }
}
