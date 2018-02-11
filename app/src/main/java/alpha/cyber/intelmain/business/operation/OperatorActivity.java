package alpha.cyber.intelmain.business.operation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.business.borrowbook.BorrowBookActivity;
import alpha.cyber.intelmain.business.borrowbook.BorrowDetailActivity;
import alpha.cyber.intelmain.business.login.LoginActivity;
import alpha.cyber.intelmain.business.search.SearchActivity;
import alpha.cyber.intelmain.business.userinfo.UserInfoActivity;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.widget.CustomConfirmDialog;
import alpha.cyber.intelmain.widget.MyTableView;

/**
 * Created by wangrui on 2018/1/31.
 */

public class OperatorActivity extends BaseActivity implements View.OnClickListener,CustomConfirmDialog.CustomDialogConfirmListener ,IUserView{

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private TextView tvBorrowBook;
    private TextView tvBackBook;
    private TextView tvSearchBook;
    private TextView tvReaderInfo;
    private LinearLayout layoutTable;

    private CustomConfirmDialog confirmDialog;
    private OperatorPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_operator);
    }

    @Override
    protected void findWidgets() {

        tvName= findView(R.id.tv_name);
        tvCardNumber = findView(R.id.tv_card_number);
        tvPermission = findView(R.id.tv_permission);
        layoutTable = findView(R.id.layout_table);
        tvBorrowBook = findView(R.id.tv_borrow_book);
        tvBackBook = findView(R.id.tv_back_book);
        tvReaderInfo = findView(R.id.tv_reader_info);
        tvSearchBook = findView(R.id.tv_search_book);

        tvBackBook.setOnClickListener(this);
        tvBorrowBook.setOnClickListener(this);
        tvSearchBook.setOnClickListener(this);
        tvReaderInfo.setOnClickListener(this);

        confirmDialog = new CustomConfirmDialog(this);



    }

    @Override
    protected void initComponent() {

        MyTableView stv1=new MyTableView(this,4);
        stv1.AddRow(new String[]{"已借图书"},true);
        stv1.AddRow(new String[]{"书名","借阅时间","到期归还","逾期天数"},false);
        stv1.AddRow(new String[]{"笑傲江湖","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new String[]{"射雕英雄传","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new String[]{"倚天屠龙记","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new String[]{"天龙八部","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new String[]{"神雕侠侣","2018-1-23","2019-01-24","5"},false);
        stv1.AddRow(new String[]{"碧血剑","2018-1-23","2019-01-24","5"},false);

        layoutTable.addView(stv1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnRightButton.setText(R.string.exit_login);
        btnRightButton.setVisibility(View.VISIBLE);
        btnRightButton.setOnClickListener(this);
        confirmDialog.setConfirmListener(this);
    }

    @Override
    public void onClick(View v){
        if(tvBorrowBook==v){
            IntentUtils.startAty(this, BorrowBookActivity.class);

        }else if(tvBackBook == v){
            IntentUtils.startAtyWithSingleParam(this, BorrowDetailActivity.class, Constant.BORROW_BACK,Constant.BACK_BOOK);
        }else if(tvReaderInfo == v){

            IntentUtils.startAty(this, UserInfoActivity.class);
        }else if(tvSearchBook == v){

            IntentUtils.startAty(this, SearchActivity.class);

        }else if(btnRightButton == v){
            confirmDialog.setContent(getString(R.string.box_not_closed_exit));
            confirmDialog.show();
        }
    }

    @Override
    public void onButtonClick(View view) {
        IntentUtils.startAty(this, LoginActivity.class);
    }

    @Override
    public void getUserInfo(UserInfoBean userinfoBean) {

        tvName.setText("姓名："+userinfoBean.getName());
        tvCardNumber.setText("卡号："+userinfoBean.getCardnum());
        tvPermission.setText("权限：最多可借"+userinfoBean.getMaxcount()+"册,已借"+userinfoBean.getBorrowcount()+"册");
    }

    @Override
    protected void getIntentData() {

        presenter = new OperatorPresenter(this,this);
        Intent intent=getIntent();

        String cardnum = intent.getStringExtra(Constant.ACCOUNT);
        String pwd = intent.getStringExtra(Constant.PASSWORD);

        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());
        Log.e(Constant.TAG, "time:" + time);

        //读者状态信息
//        String userstate_request = getResources().getString(R.string.userstate_request);
//        String userstate_format = String.format(userstate_request, time1, time2,"", cardnum, pwd);
//        presenter.getUserState(userstate_format);


        Log.e(Constant.TAG,cardnum);
        Log.e(Constant.TAG,pwd);

        //读者信息
        String userinfo_request = getResources().getString(R.string.userinfo_request);
        String userinfo_format = String.format(userinfo_request,time,cardnum,pwd);
        presenter.getUserInfo(userinfo_format);
    }
}
