package alpha.cyber.intelmain.business.operation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BookInfoBean;
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

public class OperatorActivity extends BaseActivity implements View.OnClickListener, CustomConfirmDialog.CustomDialogConfirmListener, IUserView {

    private TextView tvName;
    private TextView tvCardNumber;
    private TextView tvPermission;
    private TextView tvBorrowBook;
    private TextView tvBackBook;
    private TextView tvSearchBook;
    private TextView tvReaderInfo;
    private LinearLayout layoutTable;
    private MyTableView tableView;

    private CustomConfirmDialog confirmDialog;
    private OperatorPresenter presenter;
    private UserInfoBean userInfo;
    private List<BookInfoBean> bookInfoBeanList=new ArrayList<BookInfoBean>();

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
    public void onClick(View v) {
        if (tvBorrowBook == v) {
            IntentUtils.startAty(this, BorrowBookActivity.class);

        } else if (tvBackBook == v) {
            IntentUtils.startAtyWithSingleParam(this, BorrowDetailActivity.class, Constant.BORROW_BACK, Constant.BACK_BOOK);
        } else if (tvReaderInfo == v) {

            IntentUtils.startAty(this, UserInfoActivity.class);
        } else if (tvSearchBook == v) {

            IntentUtils.startAty(this, SearchActivity.class);

        } else if (btnRightButton == v) {
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

        userInfo = userinfoBean;
        tvName.setText("姓名：" + userinfoBean.getName());
        tvCardNumber.setText("卡号：" + userinfoBean.getCardnum());
        tvPermission.setText("权限：最多可借" + userinfoBean.getMaxcount() + "册,已借" + userinfoBean.getBorrowcount() + "册");

        String time = DateUtils.getSystemTime();
        String bookinfo_request = getResources().getString(R.string.bookinfo_request);

        for (int i = 0; i < userinfoBean.getBookcodes().size(); i++) {
            String bookinfo_format = String.format(bookinfo_request, time,
                    userinfoBean.getBookcodes().get(i));
            presenter.getBookInfo(bookinfo_format);
        }

    }


    @Override
    public void getBorrowedBookInfo(BookInfoBean infoBean) {

        bookInfoBeanList.add(infoBean);

        tableView.AddRow(new String[]{infoBean.getBookname(), infoBean.getBorrowtime(), infoBean.getEndtime(), "5"}, false);

        if(userInfo.getBookcodes().size()==bookInfoBeanList.size()){
            layoutTable.addView(tableView);
        }
    }

    @Override
    protected void getIntentData() {

        tableView = new MyTableView(this);
        tableView.AddRow(new String[]{"已借图书"}, true);
        tableView.AddRow(new String[]{"书名", "借阅时间", "到期归还", "逾期天数"}, false);

        presenter = new OperatorPresenter(this, this);
        Intent intent = getIntent();

        String cardnum = intent.getStringExtra(Constant.ACCOUNT);
        String pwd = intent.getStringExtra(Constant.PASSWORD);

        String time = DateUtils.getSystemTime();

        //读者状态信息
//        String userstate_request = getResources().getString(R.string.userstate_request);
//        String userstate_format = String.format(userstate_request, time1, time2,"", cardnum, pwd);
//        presenter.getUserState(userstate_format);

        //读者信息
        String userinfo_request = getResources().getString(R.string.userinfo_request);
        String userinfo_format = String.format(userinfo_request, time, cardnum, pwd);
        presenter.getUserInfo(userinfo_format);
    }
}
