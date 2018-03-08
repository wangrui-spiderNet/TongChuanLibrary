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

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.business.mechine_helper.CheckBookHelper;
import alpha.cyber.intelmain.business.mechine_helper.LockHelper;
import alpha.cyber.intelmain.db.InventoryReportDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.ToastUtil;
import alpha.cyber.intelmain.widget.CustomConfirmDialog;
import alpha.cyber.intelmain.widget.MyTableView;

/**
 * Created by wangrui on 2018/2/1.
 */

public class BorrowDetailActivity extends BaseActivity implements View.OnClickListener
        , CustomConfirmDialog.CustomDialogConfirmListener, LockCallback {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private LinearLayout layoutTableBorrowed, layoutTableBack, layoutTableWillBorrow;

    private CustomConfirmDialog customDialog;
    private int from;//1、借书  2、还书

    private UserInfoBean userInfoBean;
    private List<CheckoutListBean> holdBookList;
    private List<CheckoutListBean> borrowBookList;
    private List<CheckoutListBean> backBookList;

    private List<InventoryReport> oldReportList;
    private List<InventoryReport> borrowList;
    private List<InventoryReport> backList;

    private BorrowBookPresenter presenter;

    private Handler mHandler = new MyHandler();

    private MyTableView tableBack;
    private MyTableView tableWillBorrow;

    private LockHelper lockHelper;
    private CheckBookHelper bookHelper;
    private InventoryReportDao reportDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrow_detail);

        lockHelper = new LockHelper(mHandler);

        customDialog = new CustomConfirmDialog(this);
        customDialog.setConfirmListener(this);
    }

    @Override
    protected void findWidgets() {
        tvName = findView(R.id.tv_name);
        tvCardNumber = findView(R.id.tv_card_number);
        tvPermission = findView(R.id.tv_permission);
        layoutTableBorrowed = findView(R.id.layout_table_borrowed);
        layoutTableBack = findView(R.id.layout_table_back);
        layoutTableWillBorrow = findView(R.id.layout_table_will_borrow);

        tvCardNumber.setText("卡号："+userInfoBean.getPatron_identifier());
        tvName.setText("姓名："+userInfoBean.getPersonal_name());
        tvPermission.setText("权限："+userInfoBean.getScreen_message());

    }

    @Override
    protected void initComponent() {

        borrowBookList = new ArrayList<CheckoutListBean>();
        backBookList = new ArrayList<CheckoutListBean>();

//        CheckoutListBean backBookBean = new CheckoutListBean();
//        backBookBean.setTitle_identifier(getString(R.string.book_name));
//        backBookBean.setHold_pickup_date(getString(R.string.back_date));
//        backBookBean.setDue_date(getString(R.string.end_date));
//        backBookBean.setOverdue_days(getString(R.string.late_days));
//        backBookList.add(backBookBean);

//        CheckoutListBean borrowBookBean = new CheckoutListBean();
//        borrowBookBean.setTitle_identifier(getString(R.string.book_name));
//        borrowBookBean.setHold_pickup_date(getString(R.string.borrow_date));
//        borrowBookBean.setDue_date(getString(R.string.end_date));
//        borrowBookBean.setOverdue_days(getString(R.string.late_days));
//        borrowBookList.add(borrowBookBean);

        MyTableView tableBorrowed = new MyTableView(this, 4);
        tableBorrowed.AddRow(new String[]{getString(R.string.has_borrowed)}, true);
        for (int i = 0; i < holdBookList.size(); i++) {
            tableBorrowed.AddRow(new Object[]{holdBookList.get(i).getTitle_identifier(), holdBookList.get(i).getHold_pickup_date(), holdBookList.get(i).getOverdue_days(), holdBookList.get(i).getOverdue_days()}, false);
        }
        layoutTableBorrowed.addView(tableBorrowed);

    }


    @Override
    protected void getIntentData() {

        Intent intent = getIntent();
        from = intent.getIntExtra(Constant.BORROW_BACK, 0);

        userInfoBean = AppSharedPreference.getInstance().getUserInfo();
        holdBookList = AppSharedPreference.getInstance().getbookInfos();

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

        if (from == Constant.BACK_BOOK) {
            showTipDialog("请将书按照书标向外放置并关闭柜门，谢谢您的配合！");
        } else if (from == Constant.BORROW_BOOK) {
            showTipDialog("请取走或者放回您借的书籍，关闭柜门后点击确认！");
        }
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CheckBookHelper.INVENTORY_MSG://盘点结束处理

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

                        bookHelper.requestBookInfos(borrowList, CheckBookHelper.ACTION_TYPE_BORROW);
                        bookHelper.requestBookInfos(backList, CheckBookHelper.ACTION_TYPE_BACK);

                        bookHelper.stopLoop();
                    }

                    break;
                case CheckBookHelper.INVENTORY_FAIL_MSG:

                    Log.e(Constant.TAG, ">>>>>>失败>>>>>>");
                    ToastUtil.showToast("失败");

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
                                        borrowBookList.get(i).getTitle_identifier()
                                        , borrowBookList.get(i).getHold_pickup_date()
                                        , borrowBookList.get(i).getDue_date()
                                        , borrowBookList.get(i).getOverdue_days()}
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
                                        backBookList.get(i).getTitle_identifier()
                                        , backBookList.get(i).getHold_pickup_date()
                                        , backBookList.get(i).getDue_date()
                                        , backBookList.get(i).getOverdue_days()}
                                , false);
                    }

                    layoutTableBack.addView(tableBack);
                    break;

                case LockHelper.STATE_LISTEN_MSG://查看所有所状态

                    lockHelper.getAllDoorState();

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onGetProtocalVerison(int version) {

    }

    @Override
    public void onGetLockState(int id, byte state) {

        Log.e(Constant.TAG,"id:"+id+"--->"+state);
    }

    @Override
    public void onGetAllLockState(byte[] state) {

        if (!isAllDoorClosed(state)) {
            showTipDialog("您还有未关闭的柜门，请先关闭柜门，再按确认按钮");
        } else {
            closeTipDialog();

            bookHelper.openDevice();
        }
    }

    private void closeTipDialog() {
        if (null != customDialog && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    private void showTipDialog(String content) {
        customDialog.setContent(content);
        customDialog.setConfirmButtonText("确认");
        customDialog.setCancelable(false);

        if(!isFinishing()){
            customDialog.show();
        }

    }

    private boolean isAllDoorClosed(byte[] state) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {

        if (v == btnRightButton) {
            getDoorState();
        }
    }

    @Override
    public void onButtonClick(View view) {

        getDoorState();
    }

    private void getDoorState() {
        if (lockHelper.open(this)) {
            lockHelper.getAllDoorState();
        }
    }

}
