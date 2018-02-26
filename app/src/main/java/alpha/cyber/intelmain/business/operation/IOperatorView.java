package alpha.cyber.intelmain.business.operation;

import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.BorrowBookBean;
import alpha.cyber.intelmain.bean.UserInfoBean;

/**
 * Created by wangrui on 2018/2/25.
 */

public interface IOperatorView {
    public void getUserInfo(UserInfoBean userinfoBean);
    void getAllBorrowBooks(BorrowBookBean infoBean);
}
