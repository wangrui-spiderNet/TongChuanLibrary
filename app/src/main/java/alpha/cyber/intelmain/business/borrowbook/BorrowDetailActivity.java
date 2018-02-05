package alpha.cyber.intelmain.business.borrowbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.widget.CustomConfirmDialog;
import alpha.cyber.intelmain.widget.MyTableView;

/**
 * Created by wangrui on 2018/2/1.
 */

public class BorrowDetailActivity extends BaseActivity implements View.OnClickListener,CustomConfirmDialog.CustomDialogConfirmListener {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private LinearLayout layoutTableBorrowed,layoutTableBack,layoutTableWillBorrow;

    private CustomConfirmDialog customDialog;
    private int from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrow_detail);
    }

    @Override
    protected void findWidgets() {
        tvName= findView(R.id.tv_name);
        tvCardNumber = findView(R.id.tv_card_number);
        tvPermission = findView(R.id.tv_permission);
        layoutTableBorrowed = findView(R.id.layout_table_borrowed);
        layoutTableBack = findView(R.id.layout_table_back);
        layoutTableWillBorrow = findView(R.id.layout_table_will_borrow);


    }

    @Override
    protected void initComponent() {

        MyTableView tableBorrowed=new MyTableView(this,4);
        tableBorrowed.AddRow(new String[]{"已借图书"},true);
        tableBorrowed.AddRow(new String[]{"书名","借阅时间","到期归还","逾期天数"},false);
        tableBorrowed.AddRow(new Object[]{"笑傲江湖","2018-1-23","2019-01-24","5"},false);
        tableBorrowed.AddRow(new Object[]{"射雕英雄传","2018-1-23","2019-01-24","5"},false);
        layoutTableBorrowed.addView(tableBorrowed);

        MyTableView tableBack=new MyTableView(this,4);
        tableBack.AddRow(new String[]{"已还图书"},true);
        tableBack.AddRow(new String[]{"书名","还书时间","到期归还","逾期天数"},false);
        tableBack.AddRow(new Object[]{"倚天屠龙记","2018-1-23","2019-01-24","5"},false);
        tableBack.AddRow(new Object[]{"天龙八部","2018-1-23","2019-01-24","5"},false);
        layoutTableBack.addView(tableBack);

        MyTableView tableWillBorrow=new MyTableView(this,4);
        tableWillBorrow.AddRow(new String[]{"本次借阅"},true);
        tableWillBorrow.AddRow(new String[]{"书名","借阅时间","到期归还","逾期天数"},false);
        tableWillBorrow.AddRow(new Object[]{"神雕侠侣","2018-1-23","2019-01-24","5"},false);
        tableWillBorrow.AddRow(new Object[]{"碧血剑","2018-1-23","2019-01-24","5"},false);
        layoutTableWillBorrow.addView(tableWillBorrow);

    }

    @Override
    protected void getIntentData() {
        customDialog = new CustomConfirmDialog(this);
        customDialog.setConfirmListener(this);
        Intent intent=getIntent();
        from = intent.getIntExtra(Constant.BORROW_BACK,0);

        if(from==2){
            customDialog.setContent("请将书按照书标向外放置并关闭柜门，谢谢您的配合！");
            customDialog.setConfirmButtonText("确认");
            customDialog.setCancelable(false);
            customDialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        btnRightButton.setVisibility(View.VISIBLE);
        btnRightButton.setText(R.string.sure);
        btnRightButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==btnRightButton){
            customDialog.setContent("您还有未关闭的柜门，请先关闭柜门，再按确认按钮");
            customDialog.setConfirmButtonText("确认");
            customDialog.setCancelable(false);
            customDialog.show();
        }
    }

    @Override
    public void onButtonClick(View view) {


    }
}
