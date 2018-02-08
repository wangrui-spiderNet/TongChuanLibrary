package alpha.cyber.intelmain.business.login;

import android.content.Context;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/7.
 */

public class LoginPresenter {

    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    public void getUserInfo(final String request) {

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

    private void parseData(String result) {
        if (null != result) {
            String[] results = result.split("\\|");

            for (int i = 0; i < results.length; i++) {
                Log.e(Constant.TAG, results[i]);
            }
        }
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

    public void getBookInfo(String request) {
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

    public void borrowBook(String request) {
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

    public void backBook(String request) {
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


    public void continueBorrowBook(String request) {
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
