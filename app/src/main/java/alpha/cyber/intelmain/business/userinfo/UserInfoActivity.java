package alpha.cyber.intelmain.business.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.UserBorrowInfo;
import alpha.cyber.intelmain.business.operation.BorrowBookAdapter;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.util.AppSharedPreference;

/**
 * Created by wangrui on 2018/2/2.
 */

public class UserInfoActivity extends BaseActivity {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private TextView tvOvertime;
    private TextView tvTips1,tvTips2;
    private UserBorrowInfo borrowInfo;
    private ListView lvTable;
    private BorrowBookAdapter mAdapter;

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
        tvTips1 = findView(R.id.tv_tips);
        tvTips2 = findView(R.id.tv_tips2);
        lvTable = findView(R.id.lv_table);

        tvName.setText(borrowInfo.getPersonal_name());
        tvCardNumber.setText(borrowInfo.getPatron_identifier());
        tvPermission.setText(borrowInfo.getScreen_message());
        View headView= LayoutInflater.from(this).inflate(R.layout.item_table_headview,null);
        lvTable.addHeaderView(headView);

        mAdapter = new BorrowBookAdapter(this,borrowInfo.getCheckoutList());
        lvTable.setAdapter(mAdapter);


        if(borrowInfo.getFee()>0){
            tvOvertime.setText("逾期滞纳金："+borrowInfo.getFee()+",请尽快缴纳");
        }

        tvTips1.setText(borrowInfo.getWarning().get(0));
        tvTips2.setText(borrowInfo.getWarning().get(1));
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        btnRightButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void getIntentData() {
        borrowInfo = AppSharedPreference.getInstance().getBorrowBookUserInfo();

    }
}
