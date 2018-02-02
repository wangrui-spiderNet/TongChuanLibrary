package alpha.cyber.intelmain.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by huxin on 16/6/28.
 */
public class FileUtils {

    public static int getFileLen(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                return fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 安装APK程序代码(测试成功)
     *
     * @param context
     * @param apkPath
     */
    public static void ApkInstall(Context context, String apkPath) {
        File fileAPK = new File(apkPath);
        if (fileAPK.exists() && fileAPK.getName().toLowerCase(Locale.getDefault()).endsWith(".apk")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(context.getPackageName());
            intent.setDataAndType(Uri.fromFile(fileAPK),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 获得Root路径
     * 如果有sdcard，则路径为 /mnt/sdcard/;
     * 没有sdcard  isIndude 为true时，路径为/data/data/com.xxxx.xxxx/
     * isIndude 为false时，路径为null
     *
     * @param context
     * @param isIndude
     * @return
     */
    public static String getRootPath(Context context, boolean isIndude) {
        String rootPath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            if (isIndude) {
                rootPath = context.getFilesDir().getAbsolutePath();
            }
        }

        return rootPath;
    }

    /**
     * 获取图片缓存路径
     *
     * @param context
     * @return
     */
    public static String getCacheQrImage(Context context,String filename) {
        String basePath = getRootPath(context, true);
//        getSdInfo(context);
        String cachePath = "/file_path/pics";
        if (basePath != null && cachePath != null) {
            try {
                File newFile = new File(createFileDir(basePath, cachePath, null));
                if (!newFile.exists()) {
                    newFile.mkdirs();
                }
                return newFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String createFileDir( String basePath, String cache, String name) {
        StringBuffer sb = new StringBuffer().append(basePath).append(File.separator)
                .append("library");
        if (!TextUtils.isEmpty(cache)) {
            sb.append(cache);
        }
        if (name != null) {
            sb.append(File.separator).append(name);
        }
        return sb.toString();
    }

    /**
     * 删除一个文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 删除一个文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    /**
     * 删除一个目录（可以是非空目录）
     *
     * @param dir
     */
    public static boolean deleteDir(File dir) {
        if (dir == null || !dir.exists() || dir.isFile()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteDir(file);// 递归
            }
        }
        dir.delete();
        return true;
    }

    /**
     * 删除一个目录（可以是非空目录）
     *
     * @param path
     */
    public static boolean deleteDir(String path) {
        return deleteDir(new File(path));
    }
}
