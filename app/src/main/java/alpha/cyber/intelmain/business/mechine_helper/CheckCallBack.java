package alpha.cyber.intelmain.business.mechine_helper;

import alpha.cyber.intelmain.bean.CheckoutListBean;

/**
 * Created by wangrui on 2018/3/8.
 */

public interface CheckCallBack {

    void getBookInfoByCode(CheckoutListBean checkoutListBean,String uid);
}
