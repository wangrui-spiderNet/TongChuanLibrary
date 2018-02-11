package alpha.cyber.intelmain.business.home;

import java.util.List;

import alpha.cyber.intelmain.bean.HomeNewsBean;
import alpha.cyber.intelmain.http.model.Request;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wangrui on 2018/2/11.
 */

public interface HomePageModule {

    @POST("/weitu/getIndexImgs?address=address1")
    Observable<List<HomeNewsBean>> getHomeNews(@Body Request data);

}
