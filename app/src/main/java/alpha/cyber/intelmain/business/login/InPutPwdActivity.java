package alpha.cyber.intelmain.business.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.business.operation.OperatorActivity;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.StringUtils;
import alpha.cyber.intelmain.util.ToastUtil;

/**
 * Created by wangrui on 2018/1/31.
 */

public class InPutPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText etPWd,etAccount;
    private Button btnLogin;


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

    }

    @Override
    public void onClick(View v) {
        if(v==btnLogin){

            if(StringUtils.isEmpty(etAccount.getText().toString())){
                ToastUtil.showToast("账号不能为空");
                return;
            }

            if(StringUtils.isEmpty(etPWd.getText().toString())){
                ToastUtil.showToast("密码不能为空");
                return;
            }

            Bundle bundle=new Bundle();
            bundle.putString(Constant.ACCOUNT,etAccount.getText().toString());
            bundle.putString(Constant.PASSWORD,etPWd.getText().toString());
            IntentUtils.startAty(this, OperatorActivity.class,bundle);
        }
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

}
