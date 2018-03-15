package alpha.cyber.intelmain.business.borrowbook;

import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.http.model.EmptyResponse;
import alpha.cyber.intelmain.http.model.Request;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wangrui on 2018/2/11.
 */

public interface BorrowBookModule {

    @POST("/api/user/checkOut")//借书
    Observable<EmptyResponse> checkOutBook(@Body Request data);

    @POST("/api/user/checkIn")//还书
    Observable<EmptyResponse> checkInBook(@Body Request data);

    @POST("/api/book/bookInfo")//查看图书信息
    Observable<CheckoutListBean> getBookInfoByCode(@Body Request data);


}
