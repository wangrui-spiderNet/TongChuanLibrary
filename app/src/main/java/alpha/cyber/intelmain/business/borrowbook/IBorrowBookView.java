package alpha.cyber.intelmain.business.borrowbook;

import alpha.cyber.intelmain.bean.CheckoutListBean;

/**
 * Created by wangrui on 2018/3/14.
 */

public interface IBorrowBookView  {

    void getBookInfoByCode(  int type,  int count,CheckoutListBean listBean);
}
