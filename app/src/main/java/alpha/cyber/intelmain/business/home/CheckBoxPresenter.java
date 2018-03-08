package alpha.cyber.intelmain.business.home;

import android.content.Context;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.base.AppException;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.business.borrowbook.BorrowBookModule;
import alpha.cyber.intelmain.http.DefaultSubscriber;
import alpha.cyber.intelmain.http.model.EmptyResponse;
import alpha.cyber.intelmain.http.model.Request;
import alpha.cyber.intelmain.http.utils.RetrofitUtils;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangrui on 2018/3/8.
 */

public class CheckBoxPresenter {

    private BorrowBookModule bookModule;
    private CheckCallBack callBack;
    private Context context;
    public CheckBoxPresenter (CheckCallBack callBack,Context context){
        bookModule = RetrofitUtils.createService(BorrowBookModule.class);
        this.callBack = callBack;
        this.context =context;
    }

    public void getBookInfoByCode(String item_id){
        bookModule.getBookInfoByCode(new Request.Builder()
                .withParam("item_id",item_id)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<CheckoutListBean>() {
                    @Override
                    public void onSuccess(CheckoutListBean response) {
                        callBack.getBookInfoByCode(response);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(context, errorCode, errorMessage);
                        Log.e(Constant.TAG,"错误信息："+errorCode+errorMessage);
                    }
                });
    }
}
