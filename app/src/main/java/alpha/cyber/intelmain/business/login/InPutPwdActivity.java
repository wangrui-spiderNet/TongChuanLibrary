package alpha.cyber.intelmain.business.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.business.operation.OperatorActivity;
import alpha.cyber.intelmain.business.operation.OperatorPresenter;
import alpha.cyber.intelmain.db.DatabaseHelper;
import alpha.cyber.intelmain.db.UserDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.StringUtils;
import alpha.cyber.intelmain.util.ToastUtil;

/**
 * Created by wangrui on 2018/1/31.
 */

public class InPutPwdActivity extends BaseActivity implements View.OnClickListener, IUserView {

    private EditText etPWd, etAccount;
    private Button btnLogin;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pwd);
        presenter = new LoginPresenter(this, this);

    }

    @Override
    protected void findWidgets() {
        etPWd = findView(R.id.et_pwd);
        etAccount = findView(R.id.et_account);
        btnLogin = findView(R.id.btn_login);
        btnRightButton.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(this);

        etAccount.setText(Constant.cardnum);
        etPWd.setText(Constant.pwd);

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {

            if (StringUtils.isEmpty(etAccount.getText().toString())) {
                ToastUtil.showToast(InPutPwdActivity.this,"账号不能为空");
                return;
            }

            if (StringUtils.isEmpty(etPWd.getText().toString())) {
                ToastUtil.showToast(InPutPwdActivity.this,"密码不能为空");
                return;
            }

            presenter.login(etAccount.getText().toString(), etPWd.getText().toString());
        }
    }

    @Override
    public void getUserInfo(UserInfoBean userinfoBean) {
        try {
            UserDao userDao = new UserDao();
            userDao.insertBook(userinfoBean);

            AppSharedPreference.getInstance().setLogIn(true);

            AppSharedPreference.getInstance().saveUserInfo(userinfoBean);

            Bundle bundle = new Bundle();
            bundle.putString(Constant.ACCOUNT, etAccount.getText().toString());
            bundle.putString(Constant.PASSWORD, etPWd.getText().toString());
            IntentUtils.startAty(this, OperatorActivity.class, bundle);

            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showLoadingDialog() {

        showDialog("正在加载");

    }

    @Override
    public void hideLoadingDialog() {

        closeDialog();
    }

    @Override
    public void showErrorMsg(String msg) {

        ToastUtil.showToast(InPutPwdActivity.this,msg);
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

}
