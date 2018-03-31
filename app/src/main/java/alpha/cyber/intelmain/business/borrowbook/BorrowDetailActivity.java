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
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.business.home.HomeActivity;
import alpha.cyber.intelmain.business.mechine_helper.CheckBookHelper;
import alpha.cyber.intelmain.business.mechine_helper.LockHelper;
import alpha.cyber.intelmain.db.InventoryReportDao;
import alpha.cyber.intelmain.db.UserDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.LogSaveUtils;
import alpha.cyber.intelmain.widget.CustomConfirmDialog;
import alpha.cyber.intelmain.widget.MyTableView;

/**
 * Created by wangrui on 2018/2/1.
 */

public class BorrowDetailActivity extends BaseActivity implements View.OnClickListener
        , CustomConfirmDialog.CustomDialogConfirmListener, LockCallback, IBorrowBookView {

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

    private List<String> borrowBookCodes;
    private List<String> backBookCodes;

    private List<String> boxReportList;
    private List<String> borrowReportList;
    private List<String> backReportList;

    private BorrowBookPresenter presenter;

    private Handler mHandler = new MyHandler();

    private MyTableView tableBack;
    private MyTableView tableWillBorrow;

    private LockHelper lockHelper;
    private CheckBookHelper bookHelper;
    private InventoryReportDao reportDao;

    //打开锁的ID
    private byte openedId;
    private ScheduledExecutorService scheduledExecutorService;
    private TimerTask refreshTask;
    private int countTime;
    private boolean hasDoorOpen = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrow_detail);

        lockHelper = new LockHelper(mHandler, this);
        lockHelper.open();

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

        tvCardNumber.setText("卡号：" + userInfoBean.getPatron_identifier());
        tvName.setText("姓名：" + userInfoBean.getPersonal_name());
        tvPermission.setText("权限：" + userInfoBean.getScreen_message());

    }

    @Override
    protected void initComponent() {

        if (null != holdBookList) {
            MyTableView tableBorrowed = new MyTableView(this, 4);
            tableBorrowed.AddRow(new String[]{getString(R.string.has_borrowed)}, true);
            for (int i = 0; i < holdBookList.size(); i++) {
                tableBorrowed.AddRow(new Object[]{holdBookList.get(i).getTitle_identifier(), holdBookList.get(i).getHold_pickup_date(), holdBookList.get(i).getDue_date(), holdBookList.get(i).getOverdue_days()}, false);
            }
            layoutTableBorrowed.addView(tableBorrowed);
        } else {
            ToastUtils.showShortToast("您还没有借书！");
        }

    }

    @Override
    protected void getIntentData() {

        Intent intent = getIntent();
        from = intent.getIntExtra(Constant.BORROW_BACK, 0);

        userInfoBean = AppSharedPreference.getInstance().getUserInfo();
        holdBookList = AppSharedPreference.getInstance().getHoldBookInfos();

        bookHelper = new CheckBookHelper(mHandler);
        presenter = new BorrowBookPresenter(this, bookHelper, this);
        reportDao = new InventoryReportDao(this);

        boxReportList = new ArrayList<String>();
        borrowReportList = new ArrayList<String>();
        backReportList = new ArrayList<String>();

        borrowBookList = new ArrayList<CheckoutListBean>();
        backBookList = new ArrayList<CheckoutListBean>();

        openedId = (byte) AppSharedPreference.getInstance().getOpenBoxId();


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
                case CheckBookHelper.INVENTORY_SINGLE_BOX:
                    //书籍盘点结束处理
//                    hasBorrowBook = true;
                    ToastUtils.showShortToast("盘点结束，计算中...");
                    borrowReportList.clear();
                    borrowBookList.clear();
                    backBookList.clear();
                    backReportList.clear();

                    //数据库查询已经打开柜子里面的书籍
                    List<InventoryReport> inventoryReports = reportDao.queryReportsByBoxId(openedId);

                    if (null != inventoryReports) {
                        for (int i = 0; i < inventoryReports.size(); i++) {
                            boxReportList.add(inventoryReports.get(i).getUidStr());
                        }
                    }

                    calculateBorrowOrBackBook(msg);
                    break;

                case LockHelper.HAS_DOOR_NOT_CLOSED:
                    showTipDialog("您还有未关闭的柜门，请先关闭柜门，再按确认按钮");
                    break;

                case LockHelper.OPENED_CHECKING_BOOKS:
                    //盘点前面打开的柜门书籍
//                    lockHelper.close();
//                    showDialog("正在盘点...");
                    bookHelper.startInventoryOneBox(openedId);

                    break;

                default:
                    break;
            }
        }
    }

    private void calculateBorrowOrBackBook(Message msg) {

        List<InventoryReport> currentReportList = bookHelper.getInventoryList(msg);
        List<String> currentUidList = new ArrayList<>();
        for (InventoryReport report : currentReportList) {
            currentUidList.add(report.getUidStr());
        }

        Log.e(Constant.TAG, "目前书架里的书：" + currentUidList.toString());
        Log.e(Constant.TAG, "原来书架里的书：" + boxReportList.toString());
        //

        if (null != boxReportList
                && null != currentUidList) {//在原来的里面有，在新的里面没有，说明是被借走了

            for (int i = 0; i < boxReportList.size(); i++) {
                if (!currentUidList.contains(boxReportList.get(i))) {
                    borrowReportList.add(boxReportList.get(i));
                }
            }

            for (int i = 0; i < currentUidList.size(); i++) {
                if (!boxReportList.contains(currentUidList.get(i))) {
                    backReportList.add(currentUidList.get(i));
                }
            }

            Log.e(Constant.TAG, ">>借走>>>：" + borrowReportList.toString());
            Log.e(Constant.TAG, ">>还回>>>：" + backReportList.toString());

            if (borrowReportList.size() > 0) {//借走的书从本地查
                borrowBookCodes = new ArrayList<>();
                for (int i = 0; i < borrowReportList.size(); i++) {
                    borrowBookCodes.addAll(reportDao.queryBookCodesByUid(borrowReportList.get(i)));
                }

                if (null != borrowBookCodes && borrowBookCodes.size() > 0) {
                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < borrowBookCodes.size(); i++) {
                        builder.append(borrowBookCodes.get(i));
                        builder.append("]]");
                    }
                    Log.e(Constant.TAG, "借书码拼接：" + builder.toString());
                    presenter.checkOutBook(builder.toString());
                }

            }

            if (backReportList.size() > 0) {
                backBookCodes = presenter.requestBookCodes(backReportList);
                Log.e(Constant.TAG, "要还的书码：" + backBookCodes.toString());

                if (null != backBookCodes && backBookCodes.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < backBookCodes.size(); i++) {
                        builder.append(backBookCodes.get(i));
                        builder.append("]]");

                    }
                    Log.e(Constant.TAG, "还书码拼接：" + builder.toString());
                    presenter.checkInBook(builder.toString());
                }
            }

            if (backReportList.size() == 0 && borrowReportList.size() == 0) {
                ToastUtils.showShortToast("没有借还书");
            }

            closeDialog();
        }
    }

    @Override
    public void checkInBookSuccess(List<CheckoutListBean> checkoutListBeans) {
        backBookList = checkoutListBeans;
        Log.e(Constant.TAG, "还书列表：" + checkoutListBeans.toString());
        setBackBookView();
    }

    @Override
    public void checkOutBookSuccess(List<CheckoutListBean> checkoutListBeans) {
        borrowBookList = checkoutListBeans;
        setBorrowBookView();

    }

    @Override
    public void checkOutFail(String errorcode, String msg) {
        showTipDialog(msg);
        hasDoorOpen = true;
        lockHelper.openGride(openedId, false);
        setErrorTime();
    }

    @Override
    public void checkInFail(String errorcode, String msg) {
//        showTipDialog("还书失败，请取出您放进去的书，关好柜门，点击确认！");
    }

    private void setBackBookView() {
        tableBack = new MyTableView(BorrowDetailActivity.this, 4);
        tableBack.AddRow(new String[]{"已还图书"}, true);
        tableBack.AddRow(new String[]{"书名", "还书时间", "到期归还", "逾期天数"}, false);

        if (null != backBookList) {
            for (int i = 0; i < backBookList.size(); i++) {
                String book_name = backBookList.get(i).getTitle_identifier();
                String pick_up_date = backBookList.get(i).getHold_pickup_date();
                String due_date = backBookList.get(i).getDue_date();
                String over_days = backBookList.get(i).getOverdue_days();

                tableBack.AddRow(new String[]{book_name, pick_up_date, due_date, over_days}, false);
            }
        }

        layoutTableBack.addView(tableBack);

    }

    private void setBorrowBookView() {
        tableWillBorrow = new MyTableView(BorrowDetailActivity.this, 4);
        tableWillBorrow.AddRow(new String[]{"本次借阅"}, true);
        tableWillBorrow.AddRow(new String[]{"书名", "借阅时间", "到期归还", "逾期天数"}, false);

        if (null != borrowBookList) {
            for (int i = 0; i < borrowBookList.size(); i++) {
                String book_name = borrowBookList.get(i).getTitle_identifier();
                String pick_up_date = borrowBookList.get(i).getHold_pickup_date();
                String due_date = borrowBookList.get(i).getDue_date();

                tableWillBorrow.AddRow(new String[]{book_name, pick_up_date, due_date, "0"}, false);
            }
        }

        layoutTableWillBorrow.addView(tableWillBorrow);

    }

    @Override
    public void onGetProtocalVerison(int version) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        lockHelper.close();
        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    /**
     * 查看之前打开的柜门是否关闭
     *
     * @param id
     * @param state
     */
    @Override
    public void onGetLockState(int id, byte state) {

        Log.e(Constant.TAG, "id:" + id + "--->" + state);

        if (id == openedId && state == 0) {
            hasDoorOpen = true;
            mHandler.sendEmptyMessage(LockHelper.HAS_DOOR_NOT_CLOSED);
        } else {//打开的柜门已经关了，盘点书籍
            closeTipDialog();
            hasDoorOpen = false;
            mHandler.sendEmptyMessage(LockHelper.OPENED_CHECKING_BOOKS);
        }
    }

    /**
     * 查看锁关闭的回调
     *
     * @param state
     */
    @Override
    public void onGetAllLockState(byte[] state) {

        scheduledExecutorService.shutdownNow();
        lockHelper.stop();

        hasDoorOpen = hasDoorOpen(state);

        if (hasDoorOpen) {//没关柜门提示
            mHandler.sendEmptyMessage(LockHelper.HAS_DOOR_NOT_CLOSED);
//            hasBorrowBook = false;
        } else {
            closeTipDialog();
            mHandler.sendEmptyMessage(LockHelper.OPENED_CHECKING_BOOKS);
        }
    }



    @Override
    public void onClick(View v) {
        //点击右下角，检查是否关闭所有柜门
        if (v == btnRightButton) {
            if (hasDoorOpen) {
                mHandler.sendEmptyMessage(LockHelper.HAS_DOOR_NOT_CLOSED);
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    @Override
    public void onButtonClick(View view) {
        //关闭Dialog提醒，检查打开的柜门是否已经关闭
        getDoorState();
        if(null!=refreshTask){
            refreshTask.cancel();
        }

        if(null!=scheduledExecutorService){
            scheduledExecutorService.shutdownNow();
        }

    }

    /**
     * 查看是否所有的锁都关闭方法
     */
    private void getAllDoorsState() {

        scheduledExecutorService = Executors.newScheduledThreadPool(2);

        TimerTask refreshTask = new TimerTask() {
            @Override
            public void run() {
                countTime = countTime + 1;
                lockHelper.getAllDoorState();
                if (countTime % 5 == 0) {
                    lockHelper.open();
                }
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(refreshTask, 0, 200, TimeUnit.MILLISECONDS);
    }
    /**
     * 查看是否所有的锁都关闭方法
     */
    private void setErrorTime() {

        scheduledExecutorService = Executors.newScheduledThreadPool(2);

        refreshTask = new TimerTask() {
            @Override
            public void run() {
                countTime = countTime + 1;

                if (countTime  == 120) {
                    cancel();

//                    presenter.overCheckout();

                    Intent intent=new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(BorrowDetailActivity.this,HomeActivity.class);
                    intent.putExtra(Constant.HAS_DOOR_OPEN,hasDoorOpen);
                    startActivity(intent);

                    logout();

                }
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(refreshTask, 0, 1000, TimeUnit.MILLISECONDS);
    }


    /**
     * 查看之前打开的锁有没有打开
     */
    private void getDoorState() {
        lockHelper.getLockState(openedId);
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
        customDialog.setHasDoorOpen(hasDoorOpen);

        if (!isFinishing() && !customDialog.isShowing()) {
            customDialog.show();
        }

    }

    private boolean hasDoorOpen(byte[] state) {
        for (int i = 0; i < 1; i++) {
            if (state[i] == 0) {
                hasDoorOpen = true;
                return true;

            } else {
                hasDoorOpen = false;
            }
        }

        return false;
    }

    private void logout(){
        String clientXgTocken = AppSharedPreference.getInstance().getClientXgToken();
        AppSharedPreference.getInstance().clear();
        AppSharedPreference.getInstance().saveOpenBoxId(openedId);
        AppSharedPreference.getInstance().setClientXgToken(clientXgTocken);
        AppSharedPreference.getInstance().saveHoldBookInfos(null);
        AppSharedPreference.getInstance().saveBorrowBookUserInfo(null);

        LogSaveUtils.deleteLogFiles();

        new InventoryReportDao(this).deleteAll();
        new UserDao().deleteAll();
    }

}
