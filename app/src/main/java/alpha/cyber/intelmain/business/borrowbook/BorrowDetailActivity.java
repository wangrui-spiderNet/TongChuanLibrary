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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        , CustomConfirmDialog.CustomDialogConfirmListener, LockCallback ,IBorrowBookView{

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

    private List<String> oldReportList;
    private List<String> borrowReportList;
    private List<String> backReportList;

    private BorrowBookPresenter presenter;

    private Handler mHandler = new MyHandler();

    private MyTableView tableBack;
    private MyTableView tableWillBorrow;

    private LockHelper lockHelper;
    private CheckBookHelper bookHelper;
    private InventoryReportDao reportDao;
    private boolean hasDoorOpen = true;
    //打开锁的ID
    private byte openedId;
    //============
    private boolean hasBorrowBook;

    public static final int ACTION_TYPE_BORROW = 1;
    public static final int ACTION_TYPE_BACK = 2;
    public static final int BORROW_BOOK_INVENTORY_FINISH = 5;
    public static final int BACK_BOOK_INVENTORY_FINISH = 6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrow_detail);

        lockHelper = new LockHelper(mHandler, this);

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

        borrowBookList = new ArrayList<CheckoutListBean>();
        backBookList = new ArrayList<CheckoutListBean>();

        MyTableView tableBorrowed = new MyTableView(this, 4);
        tableBorrowed.AddRow(new String[]{getString(R.string.has_borrowed)}, true);
        for (int i = 0; i < holdBookList.size(); i++) {
            tableBorrowed.AddRow(new Object[]{holdBookList.get(i).getTitle_identifier(), holdBookList.get(i).getHold_pickup_date(), holdBookList.get(i).getDue_date(), holdBookList.get(i).getOverdue_days()}, false);
        }
        layoutTableBorrowed.addView(tableBorrowed);
    }

    @Override
    protected void getIntentData() {

        Intent intent = getIntent();
        from = intent.getIntExtra(Constant.BORROW_BACK, 0);

        userInfoBean = AppSharedPreference.getInstance().getUserInfo();
        holdBookList = AppSharedPreference.getInstance().getHoldBookInfos();

        bookHelper = new CheckBookHelper(mHandler);
        presenter = new BorrowBookPresenter(this,bookHelper,this);
        reportDao = new InventoryReportDao(this);

        openedId = (byte) AppSharedPreference.getInstance().getOpenBoxId();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("boxid", openedId);
        reportDao.queryReportsByBoxId(params);
        oldReportList = new ArrayList<String>();
        borrowReportList = new ArrayList<String>();
        backReportList = new ArrayList<String >();

        List<InventoryReport> inventoryReports = reportDao.queryAllReports();
        for(int i=0;i<inventoryReports.size();i++){
            oldReportList.add(inventoryReports.get(i).getUidStr());
        }
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
                    hasBorrowBook = true;
                    ToastUtil.showToast(BorrowDetailActivity.this, "盘点结束，计算中...");
                    borrowReportList.clear();
                    borrowBookList.clear();
                    backBookList.clear();
                    backReportList.clear();
                    calculateBorrowOrBackBook(msg);
                    break;

                case BACK_BOOK_INVENTORY_FINISH:
                    //计算之后还书处理
                    setBackBookView();
                    closeDialog();
                    break;

                case BORROW_BOOK_INVENTORY_FINISH:
                    //计算之后借书处理
                    setBorrowBookView();
                    closeDialog();
                    break;

                case LockHelper.HAS_DOOR_NOT_CLOSED:
                    showTipDialog("您还有未关闭的柜门，请先关闭柜门，再按确认按钮");
                    break;

                case LockHelper.OPENED_CHECKING_BOOKS:
                    //盘点前面打开的柜门书籍
                    lockHelper.close();
//                    showDialog("正在盘点...");
                    bookHelper.startInventoryOneBox(openedId);

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void getBookInfoByCode(int type,int count,CheckoutListBean listBean) {
        if (type == ACTION_TYPE_BORROW) {//借书

            borrowBookList.add(listBean);
            if (borrowBookList.size() == count) {
                AppSharedPreference.getInstance().saveBorrowBookInfos(borrowBookList);
                borrowBookList.clear();

//                alpha.cyber.intelmain.util.Log.e(Constant.TAG, "借书>>>>>>>>");
//                Message msg = mHandler.obtainMessage();
//                msg.what = BORROW_BOOK_INVENTORY_FINISH;
//                mHandler.sendMessage(msg);
            }
        } else if (type == ACTION_TYPE_BACK) {//还书

            alpha.cyber.intelmain.util.Log.e(Constant.TAG, "还书>>>>>>>>");
            backBookList.add(listBean);
            if (backBookList.size() == count) {
                AppSharedPreference.getInstance().saveBackBookInfos(backBookList);
                backBookList.clear();

                Message msg = mHandler.obtainMessage();
                msg.what = BACK_BOOK_INVENTORY_FINISH;
//                        msg.obj = tagList;
//                        msg.arg1 = failedCnt;
                mHandler.sendMessage(msg);
            }
        }
    }

    private void setBackBookView() {
        backBookList = AppSharedPreference.getInstance().getBackBookInfos();
        tableBack = new MyTableView(BorrowDetailActivity.this, 4);
        tableBack.AddRow(new String[]{"已还图书"}, true);
        tableBack.AddRow(new String[]{"书名", "还书时间", "到期归还", "逾期天数"}, false);

        if (null != backReportList) {
            for (int i = 0; i < backBookList.size(); i++) {
                tableBack.AddRow(new String[]{
                                backBookList.get(i).getTitle_identifier()
                                , backBookList.get(i).getHold_pickup_date()
                                , backBookList.get(i).getDue_date()
                                , backBookList.get(i).getOverdue_days()}
                        , false);
            }
        }

        layoutTableBack.addView(tableBack);

    }

    private void setBorrowBookView() {
        borrowBookList = AppSharedPreference.getInstance().getBorrowBookInfos();

        tableWillBorrow = new MyTableView(BorrowDetailActivity.this, 4);
        tableWillBorrow.AddRow(new String[]{"本次借阅"}, true);
        tableWillBorrow.AddRow(new String[]{"书名", "借阅时间", "到期归还", "逾期天数"}, false);

        if (null != borrowReportList) {
            for (int i = 0; i < borrowBookList.size(); i++) {
                tableWillBorrow.AddRow(new String[]{
                                borrowBookList.get(i).getTitle_identifier()
                                , borrowBookList.get(i).getHold_pickup_date()
                                , borrowBookList.get(i).getDue_date()
                                , borrowBookList.get(i).getOverdue_days()}
                        , false);
            }
        }

        layoutTableWillBorrow.addView(tableWillBorrow);

    }

    private void calculateBorrowOrBackBook(Message msg) {

        List<InventoryReport> currentReportList = bookHelper.getInventoryList(msg);
        List<String> currentUidList = new ArrayList<>();
        for(InventoryReport report:currentReportList){
            currentUidList.add(report.getUidStr());
        }

        Log.e(Constant.TAG,"目前书架里的书："+currentUidList.toString());
        Log.e(Constant.TAG,"原来书架里的书："+oldReportList.toString());
        borrowReportList.clear();
        //
        if (null != oldReportList && oldReportList.size() > 0
                && null != currentUidList && currentUidList.size() > 0) {//在原来的里面有，在新的里面没有，说明是被借走了

            for (int i = 0; i < oldReportList.size(); i++) {
                if(!currentUidList.contains(oldReportList.get(i))){
                    borrowReportList.add(oldReportList.get(i));
                }
            }

            for (int i = 0; i < currentUidList.size(); i++) {
                if(!oldReportList.contains(currentReportList.get(i))){
                    backReportList.add(currentUidList.get(i));
                }
            }

            Log.e(Constant.TAG, ">>借走>>>：" + borrowReportList.toString());
            Log.e(Constant.TAG, ">>还回>>>：" + backReportList.toString());

            if (borrowReportList.size() > 0) {
                presenter.requestBookInfos(borrowReportList, ACTION_TYPE_BORROW);
            } else if (backReportList.size() > 0) {
                presenter.requestBookInfos(backReportList, ACTION_TYPE_BACK);
            } else {
                ToastUtil.showToast(this, "没有借还书");
            }

            closeDialog();
        }
    }

    @Override
    public void onGetProtocalVerison(int version) {

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
            mHandler.sendEmptyMessage(LockHelper.HAS_DOOR_NOT_CLOSED);
        } else {//打开的柜门已经关了，盘点书籍
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

        hasDoorOpen = hasDoorOpen(state);

        if (hasDoorOpen) {//没关柜门提示

            mHandler.sendEmptyMessage(LockHelper.HAS_DOOR_NOT_CLOSED);
            hasBorrowBook = false;
        } else {
            closeTipDialog();
//
            if(hasBorrowBook){
                finish();
            }else{
                mHandler.sendEmptyMessage(LockHelper.OPENED_CHECKING_BOOKS);
            }

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

    @Override
    public void onClick(View v) {
        //点击右下角，检查是否关闭所有柜门
        if (v == btnRightButton) {
            getAllDoorsState();
        }
    }

    @Override
    public void onButtonClick(View view) {
        //关闭Dialog提醒，检查打开的柜门是否已经关闭

//        getDoorState();
        getAllDoorsState();

    }

    /**
     * 查看是否所有的锁都关闭方法
     */
    private void getAllDoorsState() {
        if (lockHelper.open()) {
            lockHelper.getAllDoorState();
        }
    }

    /**
     * 查看之前打开的锁有没有打开
     */
    private void getDoorState() {
        if (lockHelper.open()) {
            lockHelper.getLockState(openedId);
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
        customDialog.setHasDoorOpen(hasDoorOpen);

        if (!isFinishing() && !customDialog.isShowing()) {
            customDialog.show();
        }

    }

}
