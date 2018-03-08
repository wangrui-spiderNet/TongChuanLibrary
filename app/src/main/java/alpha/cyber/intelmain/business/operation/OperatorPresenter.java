package alpha.cyber.intelmain.business.operation;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.base.AppException;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.BorrowBookBean;
import alpha.cyber.intelmain.bean.UserBorrowInfo;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.business.login.IUserView;
import alpha.cyber.intelmain.business.login.UserInfoModule;
import alpha.cyber.intelmain.http.DefaultSubscriber;
import alpha.cyber.intelmain.http.model.Request;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.http.socket.SocketConstants;
import alpha.cyber.intelmain.http.utils.RetrofitUtils;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.Log;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangrui on 2018/2/7.
 */

public class OperatorPresenter {
    private Context context;
    private OperatorModule operatorModule;
    private IOperatorView iOperatorView;

    public OperatorPresenter(Context context,IOperatorView operatorView) {
        this.context = context;
        this.iOperatorView = operatorView;
        operatorModule = RetrofitUtils.createService(OperatorModule.class);
    }

    public void getBorrowBookInfo(String patron_id){
        iOperatorView.showLoadingDialog();
        operatorModule.userCheckoutInfo(new Request.Builder()
                .withParam("patron_id", patron_id)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<UserBorrowInfo>() {
                    @Override
                    public void onSuccess(UserBorrowInfo userInfoBean) {
                        iOperatorView.hideLoadingDialog();
                        iOperatorView.getAllBorrowBookInfo(userInfoBean);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(context, errorCode, errorMessage);
                        iOperatorView.hideLoadingDialog();
                        iOperatorView.showErrorMsg(errorMessage);
                    }
                });
    }


}
