package alpha.cyber.intelmain.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import alpha.cyber.intelmain.Constant;

/**
 * Created by wangrui on 2018/2/24.
 */

public class SilentInstallReceiver extends BroadcastReceiver {
    public static final String UPDATE_ACTION = "android.intent.action.PACKAGE_REPLACED";

    // APP包名ID
    public static final String PACKAGE_NAME = "alpha.cyber.intelmain";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(UPDATE_ACTION)) {
            String packageName = intent.getData().getEncodedSchemeSpecificPart();
            if (packageName.equals(PACKAGE_NAME)) {

                Log.e(Constant.TAG, "更新安装成功....." + packageName);
// 重新启动APP
                Intent intentToStart = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(intentToStart);
            }
        }
    }


}
