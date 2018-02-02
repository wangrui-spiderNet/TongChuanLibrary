package alpha.cyber.intelmain.reciever;

import android.content.Context;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/2.
 */

public class XGMessageReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        Log.e(Constant.TAG,"onRegisterResult Token :"+xgPushRegisterResult.getToken());
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        Log.e(Constant.TAG,"onUnregisterResult i :"+i);
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        Log.e(Constant.TAG,"onSetTagResult i :"+i);
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.e(Constant.TAG,"onTextMessage title:"+xgPushTextMessage.getTitle());
        Log.e(Constant.TAG,"onTextMessage content:"+xgPushTextMessage.getContent());
        Log.e(Constant.TAG,"onTextMessage custom content:"+xgPushTextMessage.getCustomContent());
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        Log.e(Constant.TAG,"onNotifactionClickedResult title:"+xgPushClickedResult.getTitle());
        Log.e(Constant.TAG,"onNotifactionClickedResult content:"+xgPushClickedResult.getContent());
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.e(Constant.TAG,"onNotifactionShowedResult title:"+xgPushShowedResult.getTitle());
        Log.e(Constant.TAG,"onNotifactionShowedResult content:"+xgPushShowedResult.getContent());
    }
}
