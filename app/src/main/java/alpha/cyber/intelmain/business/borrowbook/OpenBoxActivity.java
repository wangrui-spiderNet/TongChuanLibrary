package alpha.cyber.intelmain.business.borrowbook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BoxBean;
import alpha.cyber.intelmain.business.mechine_helper.LockHelper;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/1.
 */
public class OpenBoxActivity extends BaseActivity implements AdapterView.OnItemClickListener, LockCallback {

    private GridView gvBoxes;
    private BoxesAdapter mAdapter;
    private List<BoxBean> boxBeans;

    private LockHelper lockHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_box);
        lockHelper = new LockHelper(mHandler);
    }

    @Override
    protected void findWidgets() {
        gvBoxes = findView(R.id.gv_boxes);
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

        if (lockHelper.open(this)) {
            lockHelper.openGride(position);
        }

        IntentUtils.startAtyWithSingleParam(this, BorrowDetailActivity.class, Constant.BORROW_BACK, Constant.BORROW_BOOK);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        lockHelper.close();
    }

    @Override
    public void onGetProtocalVerison(int version) {

        Log.e(Constant.TAG, "version:" + version);
    }

    @Override
    public void onGetLockState(int id, byte state) {

        if (state == 0) {
            IntentUtils.startAtyWithSingleParam(this, BorrowDetailActivity.class, Constant.BORROW_BACK, Constant.BORROW_BOOK);
        } else {

        }
    }

    @Override
    public void onGetAllLockState(byte[] state) {

        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) {
                Log.e(Constant.TAG,i+":开");
            } else {
                Log.e(Constant.TAG,i+":关");
            }
        }
    }

    private Handler mHandler = new  Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case LockHelper.STATE_LISTEN_MSG://查看所有所状态

                    lockHelper.getAllDoorState();

                    break;

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

    }
}
