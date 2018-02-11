package alpha.cyber.intelmain.business.home;

import android.content.Context;

import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.base.AppException;
import alpha.cyber.intelmain.bean.HomeNewsBean;
import alpha.cyber.intelmain.http.DefaultSubscriber;
import alpha.cyber.intelmain.http.model.Request;
import alpha.cyber.intelmain.http.utils.RetrofitUtils;
import alpha.cyber.intelmain.util.Log;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangrui on 2018/1/29.
 */

public class HomePresenter {

    private HomePageModule module;
    private Context context;
    private IHomeView homeView;

    public HomePresenter (Context context,IHomeView homeView){
        this.context = context;
        this.module = RetrofitUtils.createService(HomePageModule.class);;
        this.homeView = homeView;
    }


    public void getHomeNews(){
        module.getHomeNews(new Request.Builder()
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<HomeNewsBean>>() {
                    @Override
                    public void onSuccess(List<HomeNewsBean> lessonMenuList) {

                        homeView.onGetHomePageSuccess(lessonMenuList);

                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        AppException.handleException(context, errorCode, errorMessage);
                    }
                });
    }
}
