package alpha.cyber.intelmain.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by wangrui on 2017/12/18.
 */

public class SdcardUtil {

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        Log.e(">>>>>>","sdCardExist :"+sdCardExist);

        if (sdCardExist) {

            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }

        return sdDir.toString();

    }

    public  static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }
}
