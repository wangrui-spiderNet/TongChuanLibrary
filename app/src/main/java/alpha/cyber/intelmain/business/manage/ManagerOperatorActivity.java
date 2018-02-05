package alpha.cyber.intelmain.business.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.util.IntentUtils;

/**
 * Created by wangrui on 2018/2/2.
 */

public class ManagerOperatorActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_open_box,tv_refresh_box,tv_statistic_box,tv_record_box;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_operator);
    }

    @Override
    protected void findWidgets() {
        tv_open_box = findView(R.id.tv_open_box);
        tv_record_box = findView(R.id.tv_record_box);
        tv_refresh_box = findView(R.id.tv_refresh_box);
        tv_statistic_box = findView(R.id.tv_statistic_box);

        tv_statistic_box.setOnClickListener(this);
        tv_refresh_box.setOnClickListener(this);
        tv_record_box.setOnClickListener(this);
        tv_open_box.setOnClickListener(this);
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
        if(v==tv_open_box){

            IntentUtils.startAty(this,ManagerOpenAllBoxesActivity.class);

        }else if(v==tv_record_box){

            IntentUtils.startAty(this,ManagerRecordActivity.class);
        }else if(v==tv_refresh_box){

        }else if(v==tv_statistic_box){
            IntentUtils.startAty(this,ManagerStatisticsActivity.class);
        }
    }
}
