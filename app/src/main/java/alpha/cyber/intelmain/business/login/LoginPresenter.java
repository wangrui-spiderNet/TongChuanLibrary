package alpha.cyber.intelmain.business.login;

import android.content.Context;

import alpha.cyber.intelmain.http.socket.MyAsyncTask;

/**
 * Created by wangrui on 2018/2/7.
 */

public class LoginPresenter  {

    private Context context;

    public LoginPresenter (Context context){
        this.context=context;
    }

    public void getUserInfo( String request) {

        new MyAsyncTask(new MyAsyncTask.OnSocketRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onFinish() {

            }
        }).execute(request);
    }

    public void getUserState(String request){
        new MyAsyncTask(new MyAsyncTask.OnSocketRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {

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
