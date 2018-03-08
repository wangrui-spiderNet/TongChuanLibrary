package alpha.cyber.intelmain.business.borrowbook;

import android.content.Context;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.base.AppException;
import alpha.cyber.intelmain.http.DefaultSubscriber;
import alpha.cyber.intelmain.http.model.EmptyResponse;
import alpha.cyber.intelmain.http.model.Request;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.http.utils.RetrofitUtils;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangrui on 2018/2/12.
 */

public class BorrowBookPresenter {

    private Context context;
    private BorrowBookModule borrowBookModule;

    public BorrowBookPresenter(Context context){
        this.context = context;
        borrowBookModule = RetrofitUtils.createService(BorrowBookModule.class);;
    }

    public void checkOutBook(String item_ids){
        borrowBookModule.checkOutBook(new Request.Builder()
                .withParam("item_ids",item_ids)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<EmptyResponse>() {
                    @Override
                    public void onSuccess(EmptyResponse response) {

                        ToastUtil.showToast(response.getMsg());
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        AppException.handleException(context, errorCode, errorMessage);
                    }
                });
    }

    public void checkInBook(String item_ids,String patron_id){
        borrowBookModule.checkInBook(new Request.Builder()
                .withParam("item_ids",item_ids)
                .withParam("patron_id",patron_id)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<EmptyResponse>() {
                    @Override
                    public void onSuccess(EmptyResponse response) {

                        ToastUtil.showToast(response.getMsg());
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(context, errorCode, errorMessage);
                    }
                });
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
