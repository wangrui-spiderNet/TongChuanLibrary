package alpha.cyber.intelmain.business.borrowbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.db.InventoryReportDao;
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
    private LinearLayout layoutTableBorrowed, layoutTableBack, layoutTableWillBorrow;

    private CustomConfirmDialog customDialog;
    private int from;//1、借书  2、还书

    private UserInfoBean userInfoBean;
    private List<CheckoutListBean> holdBookList;
    private List<BookInfoBean> borrowBookList;
    private List<BookInfoBean> backBookList;

    private List<InventoryReport> oldReportList;
    private List<InventoryReport> borrowList;
    private List<InventoryReport> backList;

    private BorrowBookPresenter presenter;
    private CheckBookHelper bookHelper;
    private InventoryReportDao reportDao;

    private Handler mHandler = new MyHandler();

    private MyTableView tableBack;
    private MyTableView tableWillBorrow;

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CheckBookHelper.INVENTORY_MSG:

                    List<InventoryReport> currentReportList = bookHelper.getInventoryList(msg);
                    //
                    if (null != oldReportList && oldReportList.size() > 0
                            && null != currentReportList && currentReportList.size() > 0) {//在原来的里面有，在新的里面没有，说明是被借走了

                        for (int i = 0; i < oldReportList.size(); i++) {
                            if (!currentReportList.contains(oldReportList.get(i))) {
                                borrowList.add(oldReportList.get(i));
                            }
                        }

                        for (int j = 0; j < currentReportList.size(); j++) {//在最新的里面有，在原来的里面没有，说明是还书
                            if (!oldReportList.contains(currentReportList.get(j))) {
                                backList.add(currentReportList.get(j));
                            }
                        }

                        Log.e(Constant.TAG, ">>借走>>>：" + borrowList.toString());
                        Log.e(Constant.TAG, ">>还回>>>：" + backList.toString());

                        bookHelper.requestBookInfos(borrowList, 1);
                        bookHelper.requestBookInfos(backList, 2);

                        bookHelper.stopLoop();
                    }

                    break;
                case CheckBookHelper.INVENTORY_FAIL_MSG:

                    Log.e(Constant.TAG, ">>>>>>失败>>>>>>");

                    break;
                case CheckBookHelper.THREAD_END:

                    break;

                case CheckBookHelper.BACK_BOOK_INVENTORY_FINISH:
                    borrowBookList = AppSharedPreference.getInstance().getBorrowBookInfos();

                    tableWillBorrow = new MyTableView(BorrowDetailActivity.this, 4);
                    tableWillBorrow.AddRow(new String[]{"本次借阅"}, true);
                    tableWillBorrow.AddRow(new String[]{"书名", "借阅时间", "到期归还", "逾期天数"}, false);

                    for (int i = 0; i < borrowBookList.size(); i++) {
                        tableWillBorrow.AddRow(new String[]{
                                        borrowBookList.get(i).getBookname()
                                        , borrowBookList.get(i).getBorrowtime()
                                        , borrowBookList.get(i).getEndtime()
                                        , borrowBookList.get(i).getLatedays()}
                                , false);
                    }

                    layoutTableWillBorrow.addView(tableWillBorrow);

                    break;
                case CheckBookHelper.BORROW_BOOK_INVENTORY_FINISH:
                    backBookList = AppSharedPreference.getInstance().getBackBookInfos();
                    tableBack = new MyTableView(BorrowDetailActivity.this, 4);
                    tableBack.AddRow(new String[]{"已还图书"}, true);
                    tableBack.AddRow(new String[]{"书名", "还书时间", "到期归还", "逾期天数"}, false);

                    for (int i = 0; i < backBookList.size(); i++) {
                        tableBack.AddRow(new String[]{
                                        backBookList.get(i).getBookname()
                                        , backBookList.get(i).getBorrowtime()
                                        , backBookList.get(i).getEndtime()
                                        , backBookList.get(i).getLatedays()}
                                , false);
                    }

                    layoutTableBack.addView(tableBack);
                    break;

                default:
                    break;
            }
        }
    }

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

        tvCardNumber.setText(userInfoBean.getPatron_identifier());
        tvName.setText(userInfoBean.getPersonal_name());
        tvPermission.setText(userInfoBean.getScreen_message());

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

        MyTableView tableBorrowed = new MyTableView(this, 4);
        tableBorrowed.AddRow(new String[]{getString(R.string.has_borrowed)}, true);
        for (int i = 0; i < holdBookList.size(); i++) {
            tableBorrowed.AddRow(new Object[]{holdBookList.get(i).getItem_identifier(), holdBookList.get(i).getHold_pickup_date(), holdBookList.get(i).getOverdue_days(), holdBookList.get(i).getOverdue_days()}, false);
        }
        layoutTableBorrowed.addView(tableBorrowed);

    }


    @Override
    protected void getIntentData() {
        customDialog = new CustomConfirmDialog(this);
        customDialog.setConfirmListener(this);
        Intent intent = getIntent();
        from = intent.getIntExtra(Constant.BORROW_BACK, 0);

        if (from == Constant.BACK_BOOK) {
            customDialog.setContent("请将书按照书标向外放置并关闭柜门，谢谢您的配合！");
            customDialog.setConfirmButtonText("确认");
            customDialog.setCancelable(false);
            customDialog.show();
        } else if (from == Constant.BORROW_BOOK) {
            customDialog.setContent("请取走您要借的书籍，关闭柜门后点击确认！");
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

        bookHelper = new CheckBookHelper(mHandler);
        reportDao = new InventoryReportDao(this);
        oldReportList = reportDao.queryAllReports();
        borrowList = new ArrayList<InventoryReport>();
        backList = new ArrayList<InventoryReport>();

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

        if (checkBoxClosed()) {
            customDialog.dismiss();
            bookHelper.openDevice();

        } else {
            customDialog.setContent("您还有未关闭的柜门，请先关闭柜门，再按确认按钮");
            customDialog.setConfirmButtonText("确认");
            customDialog.setCancelable(false);
            customDialog.show();
        }
    }

    private boolean checkBoxClosed() {
        Random rd = new Random();

        return rd.nextBoolean();
    }

    private void borrowBook(String cardnum, String bookcode) {
        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());
        Log.e(Constant.TAG, "time:" + time);

//        借书
        String borrow_book_request = getResources().getString(R.string.borrowbook_reuqest);
        String borrow_book_format = String.format(borrow_book_request, time1, time2, time1, time2, cardnum, bookcode);
        presenter.borrowBook(borrow_book_format);

    }

    private void reBorrow(String bookcode, String pwd) {

        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());

//        续借
        String continue_borrow_request = getResources().getString(R.string.re_borrow_book_request);
        String continue_borrow_format = String.format(continue_borrow_request, time1, time2, time1, time2, pwd, bookcode);
        presenter.continueBorrowBook(continue_borrow_format);

    }

    private void backBook(String bookcode) {
        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());
//        还书
        String back_book_request = getResources().getString(R.string.back_book_request);
        String back_book_format = String.format(back_book_request, time1, time2, bookcode);
        presenter.backBook(back_book_format);


    }

}
