package alpha.cyber.intelmain.business.borrowbook;

import java.util.List;

import alpha.cyber.intelmain.bean.CheckoutListBean;

/**
 * Created by wangrui on 2018/3/14.
 */

public interface IBorrowBookView  {

//    void getBookInfoByCode(  int type,  int count,CheckoutListBean listBean);

    void checkInBookSuccess(List<CheckoutListBean> checkoutListBeans);

    void checkOutBookSuccess(List<CheckoutListBean> checkoutListBeans);

    void checkOutFail(String errorcode,String msg);

    void checkInFial(String errorcode,String msg);
}
