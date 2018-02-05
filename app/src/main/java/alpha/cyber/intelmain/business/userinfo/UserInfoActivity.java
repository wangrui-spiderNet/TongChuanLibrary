package alpha.cyber.intelmain.business.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.widget.MyTableView;

/**
 * Created by wangrui on 2018/2/2.
 */

public class UserInfoActivity extends BaseActivity {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private TextView tvOvertime;
    private LinearLayout layoutTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

    }

    @Override
    protected void findWidgets() {
        tvName= findView(R.id.tv_name);
        tvCardNumber = findView(R.id.tv_card_number);
        tvPermission = findView(R.id.tv_permission);
        tvOvertime = findView(R.id.tv_overtime);
        layoutTable = findView(R.id.layout_table_borrowed);

    }

    @Override
    protected void initComponent() {

        MyTableView stv1=new MyTableView(this,4);
        stv1.AddRow(new String[]{"已借图书"},true);
        stv1.AddRow(new String[]{"书名","借阅时间","到期归还","逾期天数"},false);
        stv1.AddRow(new Object[]{"笑傲江湖","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new Object[]{"射雕英雄传","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new Object[]{"倚天屠龙记","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new Object[]{"天龙八部","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new Object[]{"神雕侠侣","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new Object[]{"碧血剑","2018-1-23","2019-01-24","5"},false);

        layoutTable.addView(stv1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        btnRightButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void getIntentData() {

    }
}
