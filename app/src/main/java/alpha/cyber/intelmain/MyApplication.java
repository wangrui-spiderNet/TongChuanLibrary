package alpha.cyber.intelmain;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tendcloud.tenddata.TCAgent;

import alpha.cyber.intelmain.util.ActivityManager;
import alpha.cyber.intelmain.util.AppSharedPreference;

/**
 * Created by wangrui on 2016/6/19.
 */
public class MyApplication extends Application {

    private static Context appContext;
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        instance = this;

        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(true);
        TCAgent.LOG_ON = true;

        AppSharedPreference.getInstance();

        XGPushConfig.enableDebug(this,true);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.e(Constant.TAG, "注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e(Constant.TAG, "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

        String token= XGPushConfig.getToken(this);
        Log.e(Constant.TAG,"token:"+token);

    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public String getRunningActivityName() {
        return ActivityManager.getAppManager().currentActivity().getLocalClassName();
    }

}
