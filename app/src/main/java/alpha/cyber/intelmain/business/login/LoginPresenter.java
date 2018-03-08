package alpha.cyber.intelmain.business.login;

import android.content.Context;

import alpha.cyber.intelmain.base.AppException;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.http.DefaultSubscriber;
import alpha.cyber.intelmain.http.model.Request;
import alpha.cyber.intelmain.http.utils.RetrofitUtils;
import alpha.cyber.intelmain.util.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangrui on 2018/2/7.
 */

public class LoginPresenter {

    private Context context;
    private UserInfoModule infoModule;
    private IUserView userView;

    public LoginPresenter(Context context, IUserView view) {
        this.context = context;
        infoModule = RetrofitUtils.createService(UserInfoModule.class);
        userView = view;
    }

    public void login(String patron_id, String password) {
        userView.showLoadingDialog();

        infoModule.login(new Request.Builder()
                .withParam("patron_id", patron_id)
                .withParam("password", password)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<UserInfoBean>() {
                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        if(null!=userInfoBean){
                            userView.getUserInfo(userInfoBean);
                            userView.hideLoadingDialog();
                        }else{
                            ToastUtil.showToast("请检查账号和密码是否输入正确！");
                        }

                        userView.hideLoadingDialog();
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(context, errorCode, errorMessage);
                        userView.hideLoadingDialog();
                        userView.showErrorMsg(errorMessage);
                    }
                });
    }

}
