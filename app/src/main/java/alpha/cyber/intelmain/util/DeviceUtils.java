package alpha.cyber.intelmain.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import alpha.cyber.intelmain.MyApplication;

/**
 * 设备工具类
 * <p/>
 * 创建时间: 2014-8-19 下午2:10:48 <br/>
 *
 * @author xnjiang
 * @since v0.0.1
 */
public abstract class DeviceUtils {
    private final static String TAG = DeviceUtils.class.getSimpleName();

    /**
     * 当前手机时区名称。如：GMT+0800
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getTimeZoneName(Context context) {
        String timeZoneDisplayName = "GMT+0800";
        try {
            // GMT+0800
            timeZoneDisplayName = TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT);
            long offset = TimeZone.getDefault().getRawOffset();
            int zone = (int) (offset / (60 * 60 * 1000));
            zone = zone * 100;
            if (zone >= 0) {
                timeZoneDisplayName = String.format("GMT+%04d", Math.abs(zone));
            } else {
                timeZoneDisplayName = String.format("GMT-%04d", Math.abs(zone));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeZoneDisplayName;
    }

    /**
     * 当前手机时区编号。如：Asia/Shanghai
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getTimeZoneID(Context context) {
        String timeZoneID = "Asia/Shanghai";
        try {
            // Asia/Shanghai
            timeZoneID = TimeZone.getDefault().getID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeZoneID;
    }

    /**
     * 获取手机语言信息(例如：en、zh) <br>
     * (设置成简体中文的时候，getLanguage()返回的是zh,getCountry()返回的是cn)
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getLanguage(Context context) {
        // 获取系统当前使用的语言
        String language = Locale.getDefault().getLanguage();
        return language;
    }

    /**
     * 获取手机国家信息(例如：EN、CN) <br>
     * (设置成简体中文的时候，getLanguage()返回的是zh,getCountry()返回的是cn)
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getCountry(Context context) {
        // 获取区域
        String country = Locale.getDefault().getCountry();
        return country;
    }

    /**
     * 获取当前设备的唯一标识字符串，自己组装的
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getUUID(Context context) throws SecurityException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String DeviceId, SimSerialNumber, AndroidId;
        DeviceId = "" + tm.getDeviceId();
        SimSerialNumber = "" + tm.getSimSerialNumber();
        AndroidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(AndroidId.hashCode(), ((long) DeviceId.hashCode() << 32) | SimSerialNumber.hashCode());
        return deviceUuid.toString();
    }

    /**
     * 获取系统唯一标识码(IMEI)<br>
     * 获取机串IMEI: 仅仅只对Android手机有效
     */
    public static String getIMEI(Context context) throws SecurityException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DeviceId = tm.getDeviceId();
        if (DeviceId == null) {
            return "";
        } else {
            return DeviceId;
        }
    }


    /**
     * 获取系统的ANDROID_ID
     */
    public static String getANDROID_ID(Context context) {
        String ANDROID_ID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if (ANDROID_ID == null) {
            return "";
        } else {
            return ANDROID_ID;
        }
    }

    /**
     * @param context
     * @return String
     * @Description: 获取卡串IMSI
     */
    public static String getIMSI(Context context) throws SecurityException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getSubscriberId();
        if (id == null) {
            return "";
        } else {
            return id;
        }
    }

    /**
     * @return String
     * @Description: 获取手机型号
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * @return String
     * @Description: 获取系统版本
     */
    public static String getOS() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SIM卡的可用状态
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static boolean isSimActive() {
        TelephonyManager tm = (TelephonyManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }
        return TelephonyManager.SIM_STATE_READY == tm.getSimState();
    }

    /**
     * 获取设备是否是横屏(AndroidPad)设备
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static boolean getLandscapeDevice(Context context) {
        Activity activity = (Activity) context;
        int orientation = activity.getResources().getConfiguration().orientation;
        int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        return (orientation == Configuration.ORIENTATION_PORTRAIT && displayRotation % 2 != 0) || (orientation == Configuration.ORIENTATION_LANDSCAPE && displayRotation % 2 == 0);
    }

    /**
     * @Description: 获取手机屏幕宽像素
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * @Description: 获取手机屏幕高像素
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 状态栏的高度
     *
     * @param activity 必须传入Activity
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static int getScreenStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);// /取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
        int screenWidth = frame.width();
        int screenHeight = frame.height();
        // 状态栏的高度，所以frame.height,frame.width分别是系统的高度的宽度
        int statusBarHeight = frame.top;

        View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);// /获得根视图
        // 状态栏标题栏的总高度/ statusBarHeight是上面所求的状态栏的高度
        int contentTop = v.getTop();
        // 所以标题栏的高度为contentTop-statusBarHeight
        int titleBarHeight = contentTop - statusBarHeight;
        // 视图区域可以使用的宽度
        int realViewWidth = v.getWidth();
        // 视图的高度，不包括状态栏和标题栏
        int realViewHeight = v.getHeight();
        String strDeviceSizeInfo = "屏幕宽度" + screenWidth + "\n" + "屏幕高度" + screenHeight + "\n" + "状态栏高度" + statusBarHeight + "\n" + "标题栏高度" + titleBarHeight + "\n" + "可用宽度" + realViewWidth + "\n" + "可用高度（不含状态栏和标题栏）" + realViewHeight;


        return statusBarHeight;
    }

    /**
     * 获取屏幕分辨率（单位px）（eg:480x800）
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getResolution_px(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    /**
     * 获取屏幕密度系数 eg 160、240、320<br>
     * 谷歌定义 densityDpi=160为基准。density=densityDpi/标准dpi(160)
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static int getDensityDpi(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * 获取屏幕密度系数 eg 1.5、2.0、3.0<br>
     * 单位dip/dp的换算值。 一般用在设定控件大小中，这样可以适配在多个分辨率上面。<br>
     * px=1dp*density
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取屏幕字体缩放比例 单位sp的换算值。 一般用在设定字体大小中，这样可以适配在多个分辨率上面。 px=1sp*scaledDensity
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }

    /**
     * dip转为px
     *
     * @param context
     * @param dip
     * @return px
     */
    public static int dip2Px(Context context, int dip) {
        Resources resources = context.getResources();
        float flo_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        int px = Math.round(flo_px);
        return px;

    }

    public static int sp2Px(Context context, int sp) {
        Resources resources = context.getResources();
        float ipx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
        return Math.round(ipx);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取用于显示的版本号(显示如：1.0.0)
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 获取用于升级的版本号(内部识别号)
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // TODO: 以下操作时对安装包，类文件，以及APP文件的安装、卸载处理

    /**
     * 安装APK程序代码
     *
     * @param context
     * @param apkPath
     */
    public static void ApkInstall(Context context, String apkPath) {
        File fileAPK = new File(apkPath);
        if (fileAPK.exists() && fileAPK.getName().toLowerCase().endsWith(".apk")) {
            Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(fileAPK), "application/vnd.android.package-archive");
            context.startActivity(install);// 安装
        }
    }

    /**
     * 卸载APK程序代码
     *
     * @param context
     * @param packageName
     */
//	public static void ApkUnInstall(Context context, String packageName) {
//		if (isPackageExists(context, packageName)) {
//			Uri packageURI = Uri.parse("package:" + packageName);
//			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//			context.startActivity(uninstallIntent);
//		}
//	}

    /**
     * 获取应用程序的完整包名
     *
     * @param context
     * @return eg. com.xxx.xxx
     */
    public static String getAppPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获取当前实例所在的父包名
     *
     * @param context
     * @return eg. com.xxx.xxx
     */
    public static String getPackageNameClass(Context context) {
        if (context == null || "".equals(context)) {
            return "";
        }
        return context.getPackageName();
    }

    /**
     * 检测该包名所对应的应用是否存在（eg.com.org）
     *
     * @param context
     * @param packageName
     * @return
     */
//	public static boolean isPackageExists(Context context, String packageName) {
//		if (packageName == null || "".equals(packageName)) {
//			return false;
//		}
//		try {
//			context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
//			return true;
//		} catch (NameNotFoundException e) {
//			return false;
//		}
//	}

    /**
     * 检测该包名所对应类是否存在（eg.com.org.MainActivity）
     *
     * @param className
     * @return
     */
    public static boolean isClassExists(String className) {
        if (className == null || "".equals(className)) {
            return false;
        }
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

//	/**
//	 * 检测sdcard卡状态并使用提示信息提示用户(仅当sdcard不存在时才显示提示).
//	 *
//	 * @return true, sdcard卡存在并且具有读写权限; false, sdcard卡不存在.
//	 */
//	public static boolean checkAndToastWhenSdcardNotExists(Context context, String msg) {
//		boolean exists = FileUtil.isSDExist();
//		if (!exists) {
//			ToastUtils.toastLong(context, msg);
//		}
//
//		return exists;
//	}

    /**
     * 检测当前系统声音是否为正常模式
     *
     * @return
     */
    public boolean isAudioNormal() {
        AudioManager mAudioManager = (AudioManager) MyApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isVersionCompat(int VersionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取当前设置的电话号码 <a
     * href="http://www.open-open.com/lib/view/open1331537862874.html" >参考资料</a>
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public String getNativePhoneNumber(Context context) throws SecurityException {
        String NativePhoneNumber = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        NativePhoneNumber = telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * 获取手机的IMSI码,并判断是中国移动\中国联通\中国电信 <BR>
     * 需要加入权限 android.permission.READ_PHONE_STATE <BR>
     * <a href="http://www.open-open.com/lib/view/open1331537862874.html"
     * >参考资料</a>
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getNetworkOperators(Context context) throws SecurityException {
        String strOperators = "unknown";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI != null) {
            if (IMSI.equals("46000") || IMSI.equals("46002") || IMSI.equals("46007")) {
                strOperators = "中国移动";
            } else if (IMSI.equals("46001")) {
                strOperators = "中国联通";
            } else if (IMSI.equals("46003")) {
                strOperators = "中国电信";
            }
        }
        return strOperators;
    }

    /**
     * 获取当前网络类型 <a
     * href="http://blog.csdn.net/shakespeare001/article/details/7505932"
     * >参考资料</a>
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "unknown";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return strNetworkType;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            strNetworkType = "WIFI";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            switch (networkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_CDMA:// 网络类型为CDMA
                case TelephonyManager.NETWORK_TYPE_EDGE:// 网络类型为EDGE
                case TelephonyManager.NETWORK_TYPE_GPRS:// 网络类型为GPRS
                    strNetworkType = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// 网络类型为EVDO0
                case TelephonyManager.NETWORK_TYPE_EVDO_A:// 网络类型为EVDOA
                case TelephonyManager.NETWORK_TYPE_HSDPA:// 网络类型为HSDPA
                case TelephonyManager.NETWORK_TYPE_HSPA:// 网络类型为HSPA
                case TelephonyManager.NETWORK_TYPE_HSUPA:// 网络类型为HSUPA
                case TelephonyManager.NETWORK_TYPE_UMTS:// 网络类型为UMTS
                    strNetworkType = "3G";
                    break;
                default:
                    break;
            }
        }
        return strNetworkType;
    }

//	/**
//	 * 获取系统的MAC地址 (错误返回12个0 )
//	 *
//	 * @author xnjiang
//	 * @return
//	 * @since v0.0.1
//	 */
//	public static String getMacAddress(Context context) {
//		String strMacAddress= "";
//		switch (NetworkUtils.getNetworkType()) {
//		case 0:
//			strMacAddress = "000000000000";
//			break;
//		case 1:
//			strMacAddress = getMacAddressByWifi(context);
//			break;
//		default:
//			strMacAddress = getLocalIpAddressByGPRS();
//			break;
//		}
//		return strMacAddress;
//	}

    /**
     * 获取设备Wifi的MAC地址 (错误返回12个0 )
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getMacAddressByWifi(Context context) {
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress())) {
                    macAddress = info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /**
     * 获取设备非Wifi的MAC地址 (错误返回12个0 )
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getMacAddressByEthernet(Context context) {
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress())) {
                    macAddress = info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /**
     * 获取以太网的mac地址（设备自带网线接口时）
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static String getMacAddressOfEthernet() {
        try {
            String macFileString = "/sys/class/net/eth0/address";
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader(macFileString));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            String macAddress = fileData.toString();
            return macAddress.toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//	/**
//	 * 获取当前网络下的IP地址 <a
//	 * href="http://www.cnblogs.com/android100/p/Android-get-ip.html" >参考资料</a>
//	 *
//	 * @author xnjiang
//	 * @param context
//	 * @return
//	 * @since v0.0.1
//	 */
//	public static String getIPAddress(Context context) {
//		String strIPAddress = "";
//		switch (NetworkUtils.getNetworkType()) {
//		case 0:
//			strIPAddress = "0.0.0.0";
//			break;
//		case 1:
//			strIPAddress = getLocalIpAddressByWifi(context);
//			break;
//		default:
//			strIPAddress = getLocalIpAddressByGPRS();
//			break;
//		}
//		return strIPAddress;
//	}

    /**
     * 获取Wifi网络下的IP地址
     *
     * @param context
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    private static String getLocalIpAddressByWifi(Context context) throws SocketException {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);

    }

    /**
     * 获取GPRS移动网络或以太网下的IP地址
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    private static String getLocalIpAddressByGPRS() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    return inetAddress.getHostAddress().toString();
                }
            }
        }

        return "0.0.0.0";
    }


    /**
     * 得到一个程序的权限列表(精简AppSecurityPermissions代码实现)。
     *
     * @param context
     * @author xnjiang
     * @since v0.0.1
     */
    public static void getAppPermisson(Context context) throws NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        // 得到自己的包名
        String pkgName = pi.packageName;

        PackageInfo pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);// 通过包名，返回包信息
        String sharedPkgList[] = pkgInfo.requestedPermissions;// 得到权限列表

        for (int i = 0; i < sharedPkgList.length; i++) {
            String permName = sharedPkgList[i];

            PermissionInfo tmpPermInfo = pm.getPermissionInfo(permName, 0);// 通过permName得到该权限的详细信息
            PermissionGroupInfo pgi = pm.getPermissionGroupInfo(tmpPermInfo.group, 0);// 权限分为不同的群组，通过权限名，我们得到该权限属于什么类型的权限。
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(i + "-" + permName + "\n");
            stringBuffer.append(i + "-" + pgi.loadLabel(pm).toString() + "\n");
            stringBuffer.append(i + "-" + tmpPermInfo.loadLabel(pm).toString() + "\n");
            stringBuffer.append(i + "-" + tmpPermInfo.loadDescription(pm).toString() + "\n");
            stringBuffer.append(i + "- end" + "\n");
            Log.i("getAppPermisson", stringBuffer.toString());
        }

    }

    /**
     * 设置手机的外放，听筒模式
     *
     * @param context
     * @param boolCallIn true＝外放模式，false=听筒模式
     * @author xnjiang
     * @since v0.0.1
     */
    public static void setPlayMode(Context context, boolean boolCallIn) throws Exception {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (boolCallIn) {
            Log.i("播放模式", "正常放音模式");
            audioManager.setSpeakerphoneOn(true);// 关闭扬声器
            audioManager.setMode(AudioManager.MODE_NORMAL);// 把模式调成正常放音模式
        } else {
            Log.i("播放模式", "听筒放音模式");
            audioManager.setSpeakerphoneOn(true);// 关闭扬声器
            audioManager.setMode(AudioManager.MODE_IN_CALL);// 把模式调成听筒放音模式
        }
    }

    public static int getStatusBarHeight(Context context) {
        Class c = null;
        Object bj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            bj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(bj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 判断是否具有ROOT权限
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
    public static boolean checkPermissionRoot() {
        boolean res = false;
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                res = false;
            } else {
                res = true;
            }
        } catch (Exception e) {

        }
        return res;
    }

    /**
     * 判断权限是否可用
     *
     * @author xnjiang
     * @param context
     * @return
     * @since v0.0.1
     */
//	public static boolean checkPermission(Context context, String permissionString) {
//		PackageManager pm = context.getPackageManager();
//		// boolean permission = (PackageManager.PERMISSION_GRANTED ==
//		// pm.checkPermission(permissionString,
//		// DeviceUtils.getAppPackageName(MyApplication.getInstance())));
//		boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permissionString, "com.cicada.cicada"));
//		if (permission) {
//			ToastUtils.showToastImage(context, "有这个权限", 0);
//		} else {
//			ToastUtils.showToastImage(context, "木有这个权限", 0);
//		}
//		return permission;
//	}

    /**
     * 获取权限列表
     *
     * @return
     * @author xnjiang
     * @since v0.0.1
     */
//	public static void getPermissionList(Context context) {
//		try {
//			PackageManager pm = context.getPackageManager();
//			PackageInfo pack = pm.getPackageInfo("packageName", PackageManager.GET_PERMISSIONS);
//			String[] permissionStrings = pack.requestedPermissions;
//			ToastUtils.showToastImage(context, "权限清单--->" + permissionStrings.toString(), 0);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
    public void getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Log.i(TAG, "---" + str2);
            }
        } catch (IOException e) {
        }
    }

    public long getAvailMemory(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    public long[] getRomMemroy() {
        long[] romInfo = new long[2];
        //Total rom memory
        romInfo[0] = getTotalInternalMemorySize();

        //Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;
        getVersion();
        return romInfo;
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public long[] getSDCardMemory() {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
    }

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            //  level加%就是当前电量了
        }
    };

    public String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }

    public String[] getVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2] = Build.MODEL;//model
        version[3] = Build.DISPLAY;//system version
        return version;
    }

    public static String getHandInfo(ActivityManager mActivityManager) {
        String handSetInfo = "手机型号:" + Build.MODEL
                + "\n系统版本:" + Build.VERSION.RELEASE
                + "\n产品型号:" + Build.PRODUCT
                + "\n版本显示:" + Build.DISPLAY
//        6                 + "\n基带版本:" + reflect()
                + "\n系统定制商:" + Build.BRAND
                + "\n设备参数:" + Build.DEVICE
                + "\n开发代号:" + Build.VERSION.CODENAME
                + "\nSDK版本号:" + Build.VERSION.SDK_INT
                + "\nCPU类型:" + Build.CPU_ABI
                + "\n硬件类型:" + Build.HARDWARE
                + "\n主机:" + Build.HOST
                + "\n生产ID:" + Build.ID
                + "\nROM制造商:" + Build.MANUFACTURER
                + "\n全部内存:" + getSystemTotalMemorySize(mActivityManager)
                + "\n可用内存：" + getSystemAvaialbeMemorySize(mActivityManager);
        ;// 这行返回的是rom定制商的名称
        return handSetInfo;
    }


    //获得系统可用内存信息
    private static String getSystemTotalMemorySize(ActivityManager mActivityManager) {
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);
        long memSize = memoryInfo.totalMem;

        //字符类型转换
        String availMemStr = formateFileSize(memSize);

        return availMemStr;
    }

    private static String getSystemAvaialbeMemorySize(ActivityManager mActivityManager) {
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);
        long memSize = memoryInfo.availMem;

        //字符类型转换
        String availMemStr = formateFileSize(memSize);

        return availMemStr;
    }

    //调用系统函数，字符串转换 long -String KB/MB
    private static String formateFileSize(long size) {
        return Formatter.formatFileSize(MyApplication.getInstance(), size);
    }
}
