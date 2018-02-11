package alpha.cyber.intelmain.business.operation;

import android.content.Context;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.http.socket.SocketConstants;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/7.
 */

public class OperatorPresenter {
    private Context context;
    private IUserView userView;

    public OperatorPresenter(Context context,IUserView userView) {
        this.context = context;
        this.userView = userView;
    }

    public void getUserInfo(final String request) {

        new MyAsyncTask(new MyAsyncTask.OnSocketRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {

                userView.getUserInfo(parseData(result));

            }

            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onFinish() {

            }
        }).execute(request);

    }

    private UserInfoBean parseData(String result) {
        if (null != result) {
            String[] results = result.split("\\|");

            UserInfoBean infoBean=new UserInfoBean();
            for (int i = 0; i < results.length; i++) {
                Log.e(Constant.TAG, results[i]);

                String temp=results[i];

                if(temp.contains(SocketConstants.personal_name_ae)){
                    infoBean.setName(temp.substring(SocketConstants.personal_name_ae.length()));
                }else if(temp.contains(SocketConstants.hold_items_limit)){
                    infoBean.setMaxcount(temp.substring(SocketConstants.hold_items_limit.length()));
                }else if(temp.contains(SocketConstants.has_borrowed)){
                    infoBean.setBorrowcount(temp.substring(SocketConstants.has_borrowed.length()));
                }else if(temp.contains(SocketConstants.patron_identifier_aa)){
                    infoBean.setCardnum(temp.substring(SocketConstants.patron_identifier_aa.length()));
                }

            }

            return infoBean;

        }

        return null;
    }

    public void getUserState(String request) {
        new MyAsyncTask(new MyAsyncTask.OnSocketRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {
                parseData(result);
            }

            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onFinish() {

            }
        }).execute(request);
    }
}
