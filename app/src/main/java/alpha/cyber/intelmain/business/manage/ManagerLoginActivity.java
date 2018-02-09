package alpha.cyber.intelmain.business.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.util.IntentUtils;

/**
 * Created by wangrui on 2018/2/2.
 */

public class ManagerLoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_pwd;
    private Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_login);
    }

    @Override
    protected void findWidgets() {

        et_pwd =findView(R.id.et_pwd);
        btn_login =findView(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        btnRightButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v==btn_login){

            IntentUtils.startAty(this,ManagerOperatorActivity.class);
        }
    }
}
