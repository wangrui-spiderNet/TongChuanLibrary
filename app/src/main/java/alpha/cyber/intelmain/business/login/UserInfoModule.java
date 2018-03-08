package alpha.cyber.intelmain.business.login;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.http.model.Request;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wangrui on 2018/3/8.
 */

public interface UserInfoModule {
    @POST("/api/user/loginIn")
    Observable<UserInfoBean> login(@Body Request data);
}
