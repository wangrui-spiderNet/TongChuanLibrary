package alpha.cyber.intelmain.business.operation;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.business.borrowbook.BorrowBookActivity;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.widget.SimpleTableView;
import alpha.cyber.intelmain.widget.TitleTableView;

/**
 * Created by wangrui on 2018/1/31.
 */

public class OperatorActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private TextView tvBorrowBook;
    private TextView tvBackBook;
    private TextView tvSearchBook;
    private TextView tvReaderInfo;
    private LinearLayout layoutTable;

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

    }

    @Override
    protected void initComponent() {


        TitleTableView stv1=new TitleTableView(this,4);
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
        btnRightButton.setText(R.string.exit_login);
        btnRightButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v){
        if(tvBorrowBook==v){
            IntentUtils.startAty(this, BorrowBookActivity.class);

        }else if(tvBackBook == v){

        }else if(tvReaderInfo == v){

        }else if(tvSearchBook == v){

        }
    }

    @Override
    protected void getIntentData() {

    }
}
