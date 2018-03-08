package alpha.cyber.intelmain.business.operation;

import alpha.cyber.intelmain.base.IBaseView;
import alpha.cyber.intelmain.bean.BorrowBookBean;
import alpha.cyber.intelmain.bean.UserBorrowInfo;

/**
 * Created by wangrui on 2018/2/25.
 */

public interface IOperatorView extends IBaseView{
    void getAllBorrowBookInfo(UserBorrowInfo infoBean);
}
