package alpha.cyber.intelmain.business.home;

import java.util.List;

import alpha.cyber.intelmain.base.IBaseView;
import alpha.cyber.intelmain.bean.HomeNewsBean;

/**
 * Created by wangrui on 2018/1/29.
 */

public interface IHomeView extends IBaseView{


    public void onGetHomePageSuccess(List<HomeNewsBean> newsBeanList);
}
