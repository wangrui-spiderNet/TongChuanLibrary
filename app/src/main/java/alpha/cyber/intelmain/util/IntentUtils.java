package alpha.cyber.intelmain.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 页面跳转工具类,可携带参数
 */
public class IntentUtils {


    public static void startAty(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    public static void startAtyForResult(Activity aty, Class<?> cls, int requestCode, Bundle mbundle) {
        Intent intent = new Intent(aty, cls);
        intent.putExtras(mbundle);
        aty.startActivityForResult(intent, requestCode);
    }

    public static void startAty(Activity activity, Class<?> cls, Bundle mbundle) {
        Intent intent = new Intent(activity, cls);
        intent.putExtras(mbundle);
        activity.startActivity(intent);
    }


    public static void startAtyWithSingleParam(Activity activity, Class<?> cls, String key, int value) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(key, value);
        activity.startActivity(intent);

    }
    public static void startAtyWithParamForResult(Fragment fragment, Class<?> cls, int requestCode, String key, int value) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        intent.putExtra(key, value);

        fragment.startActivityForResult(intent,requestCode);

    } public static void startAtyWithParamForResult(Activity activity, Class<?> cls, int requestCode, String key, int value) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(key, value);

        activity.startActivityForResult(intent,requestCode);

    }

    public static void startAtyWithSingleParam(Activity aty, Class<?> cls, String key, long value) {
        Intent intent = new Intent(aty, cls);
        intent.putExtra(key, value);
        aty.startActivity(intent);
    }

    public static void startAtyWithSingleParam(Activity aty, Class<?> cls, String key, String value) {
        Intent intent = new Intent(aty, cls);
        intent.putExtra(key, value);
        aty.startActivity(intent);
    }

    public static void startAtyWithParams(Activity activity, Class<?> cls, List<ParamUtils.NameValue> extras) {
        Intent intent = new Intent(activity, cls);
        for (ParamUtils.NameValue item : extras) {
            setValueToIntent(intent, item.name, item.value);
        }
        activity.startActivity(intent);
    }


    public static void startAtyForResult(Activity aty, Class<?> cls, int requestCode, List<ParamUtils.NameValue> extras) {
        Intent intent = new Intent(aty, cls);
        for (ParamUtils.NameValue item : extras) {
            setValueToIntent(intent, item.name, item.value);
        }
        aty.startActivityForResult(intent, requestCode);
    }


    public static void startAtyWithSerialObj(Activity activity, Class<?> cls, String key, Serializable obj) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(key, obj);
        activity.startActivity(intent);
    }

    public static void startAtyWithParcelableObj(Activity activity, Class<?> cls, String key, Parcelable obj) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(key, obj);
        activity.startActivity(intent);
    }

    public static void startAtyForResult(Activity aty, Class<?> cls, int requestCode) {
        Intent intent = new Intent(aty, cls);
        aty.startActivityForResult(intent, requestCode);
    }

    public static void startAtyForResult(Fragment fragment, Class<?> cls, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startAtyForResult(Fragment fragment, Class<?> cls, int requestCode, Bundle mBundle) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        intent.putExtras(mBundle);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startAtyClearTop(Activity context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startDialNumberIntent(Activity context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    public static void startSMSIntent(Activity context, String phoneNumber, String body) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber)));
    }

    public static void startSettingIntent(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        activity.startActivity(intent);
    }

    private static void setValueToIntent(Intent intent, String key, Object val) {
        if (null == key || null == val)
            return;
        if (val instanceof Boolean)
            intent.putExtra(key, (Boolean) val);
        else if (val instanceof Boolean[])
            intent.putExtra(key, (Boolean[]) val);
        else if (val instanceof String)
            intent.putExtra(key, (String) val);
        else if (val instanceof String[])
            intent.putExtra(key, (String[]) val);
        else if (val instanceof Integer)
            intent.putExtra(key, (Integer) val);
        else if (val instanceof Integer[])
            intent.putExtra(key, (Integer[]) val);
        else if (val instanceof Long)
            intent.putExtra(key, (Long) val);
        else if (val instanceof Long[])
            intent.putExtra(key, (Long[]) val);
        else if (val instanceof Double)
            intent.putExtra(key, (Double) val);
        else if (val instanceof Double[])
            intent.putExtra(key, (Double[]) val);
        else if (val instanceof Float)
            intent.putExtra(key, (Float) val);
        else if (val instanceof Float[])
            intent.putExtra(key, (Float[]) val);
        else {
            throw new IllegalArgumentException("Not support data Type!");
        }
    }

    /**
     * 获取安装APK的Intent
     *
     * @param uri
     * @return
     */
    public static Intent getInstallAPKIntent(Uri uri) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 新开辟一个栈
        installIntent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        return installIntent;
    }

    /**
     * Android获取一个用于打开APK文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }


}
