package alpha.cyber.intelmain.base;

/**
 * Created by huxin on 16/7/7.
 */
public interface IBaseView {

    //显示loading界面
    void showLoadingDialog();

    //隐藏loading界面
    void hideLoadingDialog();

    //显示错误信息
    void showErrorMsg(String msg);
}
