package alpha.cyber.intelmain.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import alpha.cyber.intelmain.MyApplication;

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
    public static void ApkInstall(Context context, String apkPath) throws Exception{
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

    /**
     * 获得当前应用的根文件存放路径
     */
    public static String getAppRootDir() {
        checkAndMakeDir(Environment.getExternalStorageDirectory()
                .getPath() + "/tlibrary");
        return Environment.getExternalStorageDirectory().getPath()
                + "/tlibrary";
    }

    public static boolean checkAndMakeDir(String fileDir) {
        File file = new File(fileDir);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
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
     * 根据给定的文件的完整路径，判断 并创建文件夹 及文件
     *
     * @author hwp
     * @param filePath
     * @return
     * @since v0.0.1
     */
    public static boolean createDirAndFile(String filePath) {
        boolean isSuccess = false;
        File file = new File(filePath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 将字符串写入文件
     *
     * @param path
     * @param data
     * @return
     */
    public static boolean writeFileFromString(String path, String data) {
        boolean result = false;
        FileWriter fw = null;
        try {
            File file = new File(path);
            fw = new FileWriter(file);
            fw.write(data);
            fw.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
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
