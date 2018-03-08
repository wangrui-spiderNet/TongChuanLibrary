package alpha.cyber.intelmain.business.home;

import java.util.List;

import alpha.cyber.intelmain.bean.AppUpgradeInfo;
import alpha.cyber.intelmain.bean.HomeNewsBean;
import alpha.cyber.intelmain.http.model.Request;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wangrui on 2018/2/11.
 */

public interface HomePageModule {

    @POST("/api")
    Observable<HomeNewsBean> getHomeNews(@Body Request data);

    @POST("/api/checkUpdate")
    Observable<AppUpgradeInfo> getUpGradeInfo(@Body Request data);


    @POST("/api/qrcodeInfo")
    Observable<AppUpgradeInfo> qrcodeInfo(@Body Request data);
}
