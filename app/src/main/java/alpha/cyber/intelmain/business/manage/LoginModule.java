package alpha.cyber.intelmain.business.manage;

import android.database.Observable;

import alpha.cyber.intelmain.http.model.EmptyResponse;
import alpha.cyber.intelmain.http.model.Request;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wangrui on 2018/3/7.
 */

public interface LoginModule {
    @POST("/api/user/loginIn")
    Observable<EmptyResponse> managerLogin(@Body Request data);
}
