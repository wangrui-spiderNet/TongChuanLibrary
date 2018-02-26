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

    private OperatorPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pwd);

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
        presenter = new OperatorPresenter(this, this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {

            if (StringUtils.isEmpty(etAccount.getText().toString())) {
                ToastUtil.showToast("账号不能为空");
                return;
            }

            if (StringUtils.isEmpty(etPWd.getText().toString())) {
                ToastUtil.showToast("密码不能为空");
                return;
            }

            finish();
            AppSharedPreference.getInstance().setLogIn(true);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.ACCOUNT, etAccount.getText().toString());
            bundle.putString(Constant.PASSWORD, etPWd.getText().toString());
            IntentUtils.startAty(this, OperatorActivity.class, bundle);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //扫描全书柜，更新到本地数据库中

//        String time = DateUtils.getSystemTime();
//        String bookcode = "00834470";
//        String bookcode2 = "00834472";
//
//        String request = "17001" + time + "AO|AB" + bookcode + "|AY0AZ";
//        String request2 = "17001" + time + "AO|AB" + bookcode2 + "|AY0AZ";
//        presenter.getBookInfo(request);
//        presenter.getBookInfo(request2);

    }

    @Override
    public void getUserInfo(UserInfoBean userinfoBean) {

    }

    @Override
    public void getAllBoxBooks(BookInfoBean infoBean) {


    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

}
