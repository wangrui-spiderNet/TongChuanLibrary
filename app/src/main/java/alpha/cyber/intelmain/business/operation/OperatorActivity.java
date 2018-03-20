package alpha.cyber.intelmain.business.operation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.UserBorrowInfo;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.business.borrowbook.OpenBoxActivity;
import alpha.cyber.intelmain.business.borrowbook.BorrowDetailActivity;
import alpha.cyber.intelmain.business.mechine_helper.CheckBookService;
import alpha.cyber.intelmain.business.search.SearchActivity;
import alpha.cyber.intelmain.business.userinfo.UserInfoActivity;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.db.InventoryReportDao;
import alpha.cyber.intelmain.db.UserDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.LogSaveUtils;
import alpha.cyber.intelmain.util.ToastUtil;
import alpha.cyber.intelmain.widget.CustomConfirmDialog;

/**
 * Created by wangrui on 2018/1/31.
 */

public class OperatorActivity extends BaseActivity implements View.OnClickListener, CustomConfirmDialog.CustomDialogConfirmListener, IOperatorView {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private TextView tvBorrowBook;
    private TextView tvBackBook;
    private TextView tvSearchBook;
    private TextView tvReaderInfo;
    private ListView lvTable;

    private CustomConfirmDialog confirmDialog;
    private OperatorPresenter presenter;
    private UserInfoBean userInfo;
    private List<CheckoutListBean> bookInfoBeanList = new ArrayList<>();
    private BorrowBookAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_operator);

    }

    @Override
    protected void findWidgets() {

        tvName = findView(R.id.tv_name);
        tvCardNumber = findView(R.id.tv_card_number);
        tvPermission = findView(R.id.tv_permission);
        tvBorrowBook = findView(R.id.tv_borrow_book);
        tvBackBook = findView(R.id.tv_back_book);
        tvReaderInfo = findView(R.id.tv_reader_info);
        tvSearchBook = findView(R.id.tv_search_book);
        lvTable = findView(R.id.lv_table);

        tvBackBook.setOnClickListener(this);
        tvBorrowBook.setOnClickListener(this);
        tvSearchBook.setOnClickListener(this);
        tvReaderInfo.setOnClickListener(this);

        View headView = LayoutInflater.from(this).inflate(R.layout.item_table_headview, null);
        lvTable.addHeaderView(headView);

        confirmDialog = new CustomConfirmDialog(this);
        initFirstLine();
        mAdapter = new BorrowBookAdapter(this, bookInfoBeanList);
        lvTable.setAdapter(mAdapter);

        ivQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDao bookDao = new BookDao(OperatorActivity.this);
                Log.e(Constant.TAG, "书柜里的所有书：" + bookDao.queryAllBooks());
            }
        });
    }

    private void initFirstLine() {
        CheckoutListBean infoBean = new CheckoutListBean();
        infoBean.setTitle_identifier(getString(R.string.book_name));
        infoBean.setHold_pickup_date(getString(R.string.borrow_date));
        infoBean.setDue_date(getString(R.string.end_date));
        infoBean.setOverdue_days(getString(R.string.late_days));
        bookInfoBeanList.add(infoBean);
    }

    @Override
    protected void initComponent() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        new InventoryReportDao(this).deleteAll();
        tvBack.setVisibility(View.INVISIBLE);
        btnRightButton.setText(R.string.exit_login);
        btnRightButton.setVisibility(View.VISIBLE);
        btnRightButton.setOnClickListener(this);
        confirmDialog.setConfirmListener(this);
        presenter.getBorrowBookInfo(AppSharedPreference.getInstance().getAccount());
    }

    @Override
    public void onBackPressed() {

        AppSharedPreference.getInstance().setLogIn(false);
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        if (tvBorrowBook == v) {
            IntentUtils.startAty(this, OpenBoxActivity.class);
        } else if (tvBackBook == v) {
            IntentUtils.startAty(this, OpenBoxActivity.class);
        } else if (tvReaderInfo == v) {
            IntentUtils.startAty(this, UserInfoActivity.class);
        } else if (tvSearchBook == v) {
            IntentUtils.startAty(this, SearchActivity.class);
        } else if (btnRightButton == v) {

            logout();
            finish();

//            if (!confirmDialog.isShowing() && !isFinishing()) {
//                confirmDialog.setContent(getString(R.string.box_not_closed_exit));
//                confirmDialog.show();
//            }
        }
    }

    @Override
    public void onButtonClick(View view) {

        logout();
        finish();
    }

    private void logout(){
        String clientXgTocken = AppSharedPreference.getInstance().getClientXgToken();
        AppSharedPreference.getInstance().clear();
        AppSharedPreference.getInstance().setClientXgToken(clientXgTocken);
        AppSharedPreference.getInstance().saveHoldBookInfos(null);
        AppSharedPreference.getInstance().saveBorrowBookUserInfo(null);

        LogSaveUtils.deleteLogFiles();

        new InventoryReportDao(this).deleteAll();
        new UserDao().deleteAll();
    }

    @Override
    public void showLoadingDialog() {
        showDialog("正在加载");
    }

    @Override
    public void hideLoadingDialog() {
        closeDialog();
    }

    @Override
    public void showErrorMsg(String msg) {

        ToastUtil.showToast(this, msg);
    }

    @Override
    public void getAllBorrowBookInfo(UserBorrowInfo infoBean) {
        bookInfoBeanList.clear();
        initFirstLine();
        tvName.setText("姓名:" + infoBean.getPersonal_name());
        tvCardNumber.setText("卡号:" + infoBean.getPatron_identifier());
        tvPermission.setText("权限:" + infoBean.getScreen_message());

        bookInfoBeanList.addAll(infoBean.getCheckoutList());
        mAdapter.notifyDataSetChanged();
        AppSharedPreference.getInstance().saveHoldBookInfos(bookInfoBeanList);
        AppSharedPreference.getInstance().saveBorrowBookUserInfo(infoBean);


    }

    @Override
    protected void getIntentData() {

        presenter = new OperatorPresenter(this, this);

    }

}
