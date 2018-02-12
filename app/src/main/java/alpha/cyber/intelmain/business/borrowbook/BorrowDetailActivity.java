package alpha.cyber.intelmain.business.borrowbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.business.operation.BorrowBookAdapter;
import alpha.cyber.intelmain.business.operation.UserInfoBean;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.widget.CustomConfirmDialog;
import alpha.cyber.intelmain.widget.MyTableView;

/**
 * Created by wangrui on 2018/2/1.
 */

public class BorrowDetailActivity extends BaseActivity implements View.OnClickListener, CustomConfirmDialog.CustomDialogConfirmListener {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private LinearLayout layoutTableBorrowed,layoutTableBack,layoutTableWillBorrow;

    private CustomConfirmDialog customDialog;
    private int from;

    private UserInfoBean userInfoBean;
    private List<BookInfoBean> holdBookList;
    private List<BookInfoBean> borrowBookList;
    private List<BookInfoBean> backBookList;

    private BorrowBookPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrow_detail);
    }

    @Override
    protected void findWidgets() {
        tvName = findView(R.id.tv_name);
        tvCardNumber = findView(R.id.tv_card_number);
        tvPermission = findView(R.id.tv_permission);
        layoutTableBorrowed = findView(R.id.layout_table_borrowed);
        layoutTableBack = findView(R.id.layout_table_back);
        layoutTableWillBorrow = findView(R.id.layout_table_will_borrow);

        tvCardNumber.setText(userInfoBean.getCardnum());
        tvName.setText(userInfoBean.getName());
        tvPermission.setText(userInfoBean.getPermission());

    }

    @Override
    protected void initComponent() {

        borrowBookList = new ArrayList<BookInfoBean>();
        backBookList = new ArrayList<BookInfoBean>();

        BookInfoBean backBookBean = new BookInfoBean();
        backBookBean.setBookname(getString(R.string.book_name));
        backBookBean.setBorrowtime(getString(R.string.back_date));
        backBookBean.setEndtime(getString(R.string.end_date));
        backBookBean.setLatedays(getString(R.string.late_days));
        backBookList.add(backBookBean);

        BookInfoBean borrowBookBean = new BookInfoBean();
        borrowBookBean.setBookname(getString(R.string.book_name));
        borrowBookBean.setBorrowtime(getString(R.string.borrow_date));
        borrowBookBean.setEndtime(getString(R.string.end_date));
        borrowBookBean.setLatedays(getString(R.string.late_days));
        borrowBookList.add(borrowBookBean);

        MyTableView tableBorrowed=new MyTableView(this,4);
        tableBorrowed.AddRow(new String[]{getString(R.string.has_borrowed)},true);
        for(int i=0;i<holdBookList.size();i++){
            tableBorrowed.AddRow(new Object[]{holdBookList.get(i).getBookname(),holdBookList.get(i).getBorrowtime(),holdBookList.get(i).getEndtime(),holdBookList.get(i).getLatedays()},false);
        }

        layoutTableBorrowed.addView(tableBorrowed);

        MyTableView tableBack=new MyTableView(this,4);
        tableBack.AddRow(new String[]{"已还图书"},true);
        tableBack.AddRow(new String[]{"书名","还书时间","到期归还","逾期天数"},false);
        tableBack.AddRow(new Object[]{"倚天屠龙记","2018-01-23","2019-01-24","5"},false);
        tableBack.AddRow(new Object[]{"天龙八部","2018-01-23","2019-01-24","5"},false);
        layoutTableBack.addView(tableBack);

        MyTableView tableWillBorrow=new MyTableView(this,4);
        tableWillBorrow.AddRow(new String[]{"本次借阅"},true);
        tableWillBorrow.AddRow(new String[]{"书名","借阅时间","到期归还","逾期天数"},false);
        tableWillBorrow.AddRow(new Object[]{"神雕侠侣","2018-01-23","2019-01-24","5"},false);
        tableWillBorrow.AddRow(new Object[]{"碧血剑","2018-01-23","2019-01-24","5"},false);
        layoutTableWillBorrow.addView(tableWillBorrow);

    }

    @Override
    protected void getIntentData() {
        customDialog = new CustomConfirmDialog(this);
        customDialog.setConfirmListener(this);
        Intent intent = getIntent();
        from = intent.getIntExtra(Constant.BORROW_BACK, 0);

        if (from == 2) {
            customDialog.setContent("请将书按照书标向外放置并关闭柜门，谢谢您的配合！");
            customDialog.setConfirmButtonText("确认");
            customDialog.setCancelable(false);
            customDialog.show();
        }

        userInfoBean = AppSharedPreference.getInstance().getUserInfo();
        holdBookList = AppSharedPreference.getInstance().getbookInfos();

        if (userInfoBean == null) {

        }

        if (holdBookList == null) {

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

        if (v == btnRightButton) {
            customDialog.setContent("您还有未关闭的柜门，请先关闭柜门，再按确认按钮");
            customDialog.setConfirmButtonText("确认");
            customDialog.setCancelable(false);
            customDialog.show();
        }
    }

    @Override
    public void onButtonClick(View view) {

    }

    private void borrowBook(String cardnum,String bookcode){
        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());
        Log.e(Constant.TAG, "time:" + time);

//        借书
        String borrow_book_request =getResources().getString(R.string.borrowbook_reuqest);
        String borrow_book_format = String.format(borrow_book_request,time1,time2,time1,time2,cardnum,bookcode);
        presenter.borrowBook(borrow_book_format);

    }

    private void reBorrow(String bookcode,String pwd){

        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());

//        续借
        String continue_borrow_request = getResources().getString(R.string.re_borrow_book_request);
        String continue_borrow_format = String.format(continue_borrow_request,time1,time2,time1,time2,pwd,bookcode);
        presenter.continueBorrowBook(continue_borrow_format);

    }

    private void backBook(String bookcode){
        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());
//        还书
        String back_book_request = getResources().getString(R.string.back_book_request);
        String back_book_format = String.format(back_book_request,time1,time2,bookcode);
        presenter.backBook(back_book_format);
    }

}
