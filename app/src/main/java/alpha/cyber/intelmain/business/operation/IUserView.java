package alpha.cyber.intelmain.business.operation;

import alpha.cyber.intelmain.bean.BookInfoBean;

/**
 * Created by wangrui on 2018/2/11.
 */

public interface IUserView {

    public void getUserInfo(UserInfoBean userinfoBean);
    void getBorrowedBookInfo(BookInfoBean infoBean);

}
