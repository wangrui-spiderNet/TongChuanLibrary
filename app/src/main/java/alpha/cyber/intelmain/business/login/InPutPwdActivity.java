package alpha.cyber.intelmain.business.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.business.operation.OperatorActivity;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.ToastUtil;

/**
 * Created by wangrui on 2018/1/31.
 */

public class InPutPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText etPWd;
    private Button btnLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pwd);

    }

    @Override
    protected void findWidgets() {
        etPWd = findView(R.id.et_pwd);
        btnLogin = findView(R.id.btn_login);
        btnRightButton.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(this);

        etPWd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                if(s.toString().trim().length()<6){
//                    btnLogin.setEnabled(false);
//                }else{
//                    btnLogin.setEnabled(true);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==btnLogin){
            IntentUtils.startAty(this, OperatorActivity.class);
            String requeststr = "6300120180206162150Y        AO|AA6101008880085324|AC|AD666666|BP00001|BQ00005AY0AZ";
            new LoginPresenter(this).getUserInfo(requeststr);
        }
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

}
