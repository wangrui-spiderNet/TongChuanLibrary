package alpha.cyber.intelmain.business.login;

import alpha.cyber.intelmain.base.IBaseView;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.UserInfoBean;

/**
 * Created by wangrui on 2018/2/11.
 */

public interface IUserView {

    public void getUserInfo(UserInfoBean userinfoBean);
    void getAllBoxBooks(BookInfoBean infoBean);

}
