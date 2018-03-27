package alpha.cyber.intelmain.business.borrowbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BoxBean;
import alpha.cyber.intelmain.business.login.LoginActivity;
import alpha.cyber.intelmain.business.mechine_helper.CheckBookService;
import alpha.cyber.intelmain.business.mechine_helper.LockHelper;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.ToastUtil;

/**
 * Created by wangrui on 2018/2/1.
 */
public class OpenBoxActivity extends BaseActivity implements AdapterView.OnItemClickListener, LockCallback {

    private GridView gvBoxes;
    private BoxesAdapter mAdapter;
    private List<BoxBean> boxBeans;
    private TextView tvTip;

    private LockHelper lockHelper;
    private byte lockId;
    private int from=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_box);
        lockHelper = new LockHelper(mHandler, this);
        lockHelper.open();
    }

    @Override
    protected void findWidgets() {
        tvTip = findView(R.id.tv_tip);
        gvBoxes = findView(R.id.gv_boxes);

        if(from==Constant.BORROW_BOOK){
            tvTip.setText(R.string.borrow_book);
        }else if(from==Constant.BACK_BOOK){
            tvTip.setText(R.string.back_book);
        }
    }

    @Override
    protected void initComponent() {
        boxBeans = new ArrayList<BoxBean>();
        for (int i = 0; i < 10; i++) {
            BoxBean boxBean = new BoxBean();
            if (i != 9) {
                boxBean.setName("0" + (i + 1));
            } else {
                boxBean.setName("" + (i + 1));
            }

            boxBeans.add(boxBean);
        }

        mAdapter = new BoxesAdapter(this, boxBeans);

        gvBoxes.setAdapter(mAdapter);

        gvBoxes.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        lockId = (byte) (position + 1);
        lockHelper.openGride(position + 1);
        AppSharedPreference.getInstance().saveOpenBoxId(position + 1);

        Intent intent = new Intent(this, CheckBookService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onGetProtocalVerison(int version) {

        Log.e(Constant.TAG, "version:" + version);
    }

    @Override
    public void onGetLockState(int id, byte state) {

        Log.e(Constant.TAG, "id:" + id + "||state:" + state);

        if (id == lockId && state == 1) {
            IntentUtils.startAtyWithSingleParam(OpenBoxActivity.this, BorrowDetailActivity.class, Constant.BORROW_BACK, Constant.BORROW_BOOK);
            lockHelper.close();
            finish();
        }
    }

    @Override
    public void onGetAllLockState(byte[] state) {

        if (lockHelper.checkBoxOpen(state)) {
            IntentUtils.startAty(this, LoginActivity.class);
        }

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
//                case LockHelper.STATE_LISTEN_MSG://查看所有所状态

//                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        btnRightButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void getIntentData() {

        Intent intent=getIntent();
        from= intent.getExtras().getInt(Constant.BORROW_BACK);

    }

}
