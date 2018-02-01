package alpha.cyber.intelmain.util;

import android.widget.Toast;

import alpha.cyber.intelmain.MyApplication;


public class ToastUtil {

    private static Toast toast;

    /**
     * Toast提醒
     *
     * @param msg
     */
    public static void showToast( String msg) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * Toast提醒
     *
     */
    public static void showToast(int resId) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(MyApplication.getInstance().getResources().getString(resId));
        }
        toast.show();
    }


}
