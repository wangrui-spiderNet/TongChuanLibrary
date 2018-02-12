package alpha.cyber.intelmain.business.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.business.operation.BorrowBookAdapter;
import alpha.cyber.intelmain.business.operation.OperatorPresenter;
import alpha.cyber.intelmain.business.operation.UserInfoBean;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.widget.MyTableView;

/**
 * Created by wangrui on 2018/2/2.
 */

public class UserInfoActivity extends BaseActivity {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private TextView tvOvertime;
    private UserInfoBean userInfoBean;
    private List<BookInfoBean> bookInfoBeanList;
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
        lvTable = findView(R.id.lv_table);

        tvName.setText(userInfoBean.getName());
        tvCardNumber.setText(userInfoBean.getCardnum());
        tvPermission.setText(userInfoBean.getPermission());
        View headView= LayoutInflater.from(this).inflate(R.layout.item_table_headview,null);
        lvTable.addHeaderView(headView);

        mAdapter = new BorrowBookAdapter(this,bookInfoBeanList);
        lvTable.setAdapter(mAdapter);
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

        userInfoBean = AppSharedPreference.getInstance().getUserInfo();
        bookInfoBeanList = AppSharedPreference.getInstance().getbookInfos();

        if(null==userInfoBean){

        }

    }
}
