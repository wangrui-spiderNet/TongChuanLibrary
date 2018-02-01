package alpha.cyber.intelmain;

import android.app.Application;
import android.content.Context;

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

//        ImHelper.getInstance().init(appContext);

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
