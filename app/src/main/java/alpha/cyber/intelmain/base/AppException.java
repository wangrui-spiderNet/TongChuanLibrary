package alpha.cyber.intelmain.base;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import alpha.cyber.intelmain.http.BaseURL;
import alpha.cyber.intelmain.util.NetworkUtils;


/**
 * Created by huxin on 16/7/11.
 */
public class AppException extends Exception implements Thread.UncaughtExceptionHandler {

    static Context mContext;

    public AppException(Context context){
        AppException.mContext = context;
    }

    public static boolean handleException(Context context, String errorCode, String errorMessage){
        if(!NetworkUtils.isNetworkAvailable(context)){
            if(!NetworkUtils.isHaveNet(context))
                return true;
            handleExceptionNetworkNo(context);
            return true;
        }
        if(BaseURL.APP_EXCEPTION_HTTP_TIMEOUT.equalsIgnoreCase(errorCode)){
            handleExceptionConnectTimeOut(context,errorCode,errorMessage);
            return true;
        }
        if (BaseURL.APP_EXCEPTION_HTTP_404.equalsIgnoreCase(errorCode)
                || BaseURL.APP_EXCEPTION_HTTP_500.equalsIgnoreCase(errorCode)) {
            handleExceptionServer(context, errorCode, errorMessage);
            return true;
        }
        if (BaseURL.APP_EXCEPTION_HTTP_OTHER.equalsIgnoreCase(errorCode)) {
            handleExceptionSelf(context, errorCode, errorMessage);
            return true;
        }
        if (errorCode.length() != 7) {
            handleExceptionServer(context, errorCode, errorMessage);
        } else {
            handleExceptionBusiness(context, errorCode, errorMessage);
        }
        return false;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(Looper.getMainLooper().getThread()!=thread){
            return;
        }
    }

    /**
     * 无网络错误提示信息
     *
     * @param context
     * @author xnjiang
     * @since v0.0.1
     */
    private static void handleExceptionNetworkNo(Context context){
        if(!NetworkUtils.isNetworkAvailable(context)){
            Toast toast = Toast.makeText(context,"网络链接异常", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    /**
     * 弱网(请求超时)错误提示信息
     *
     * @param context
     * @author xnjiang
     * @since v0.0.1
     */
    private static void handleExceptionConnectTimeOut(Context context, String errorCode, String errorMessage) {
        Toast toast = Toast.makeText(context,"网络差，请尝试断开从新链接", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * 服务器错误提示信息
     *
     * @param context
     * @author xnjiang
     * @since v0.0.1
     */
    private static void handleExceptionServer(Context context, String errorCode, String errorMessage) {
        Toast toast = Toast.makeText(context,"服务器异常", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * 内部错误提示信息
     *
     * @param context
     * @param errorCode
     * @param errorMessage
     * @author xnjiang
     * @since v0.0.1
     */
    private static void handleExceptionSelf(Context context, String errorCode, String errorMessage) {
        Toast toast = Toast.makeText(context,errorMessage, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * 业务逻辑错误处理提示信息
     *
     * @param context
     * @param errorCode
     * @param errorMessage
     * @author xnjiang
     * @since v0.0.1
     */
    private static void handleExceptionBusiness(Context context, String errorCode, String errorMessage) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            Toast toast = Toast.makeText(context,errorMessage, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } else {
            AppException.handleExceptionNetworkNo(context);
        }
    }
}
