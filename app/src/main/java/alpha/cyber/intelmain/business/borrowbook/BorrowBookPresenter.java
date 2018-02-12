package alpha.cyber.intelmain.business.borrowbook;

import android.content.Context;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/12.
 */

public class BorrowBookPresenter {

    private Context context;
    public BorrowBookPresenter(Context context){
        this.context = context;
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

    private void parseData(String result) {
        if (null != result) {
            String[] results = result.split("\\|");

            for (int i = 0; i < results.length; i++) {
                Log.e(Constant.TAG, results[i]);
            }
        }
    }

}
