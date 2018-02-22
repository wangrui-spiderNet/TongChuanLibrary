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
import alpha.cyber.intellib.lock.LockController;
import alpha.cyber.intellib.utils.Logger;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BoxBean;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/1.
 */
public class OpenBoxActivity extends BaseActivity implements AdapterView.OnItemClickListener, LockCallback {

    private GridView gvBoxes;
    private BoxesAdapter mAdapter;
    private List<BoxBean> boxBeans;
    private LockController mLockController = null;
    private Thread mStateThrd = null;

    private List<StateReport> stateList = new ArrayList<StateReport>();
    private byte[] grimState = new byte[24];
    private boolean firstInit = true;

    private static final int STATE_LISTEN_MSG = 1;
    private static final int FRESH_VIEW = 2;
    private static final byte BOARD_ADDRESS = 0x01;
    private boolean b_stateThreadRun = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_box);
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

        open();
        //TODO 开锁
        mLockController.openGrid((byte) (position + 1), BOARD_ADDRESS);

        IntentUtils.startAtyWithSingleParam(this, BorrowDetailActivity.class, Constant.BORROW_BACK, Constant.BORROW_BOOK);
    }

    private void open() {
        boolean lc = false;
        lc = initController();
        if (lc) {
            mLockController.start();
            mLockController.open();
            mStateThrd = new Thread(new StateThrd());
            mStateThrd.start();

        } else {
            ToastUtils.showShortToast("打开设备失败");
        }

    }

    private void close() {
        b_stateThreadRun = false;
        mStateThrd.interrupt();
    }

    private class StateThrd implements Runnable {

        @Override
        public void run() {
            b_stateThreadRun = true;

            while (b_stateThreadRun) {
                try {
                    Thread.sleep(200);
                    Message msg = mHandler.obtainMessage();
                    msg.what = STATE_LISTEN_MSG;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean initController() {
        mLockController = new LockController(9600);
        if (mLockController.mSerialPort == null) {
            ToastUtils.showShortToast("打开设备失败");
            mLockController = null;
            return false;
        }
        mLockController.setCallBack(this);
        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mStateThrd != null) {
            mStateThrd.interrupt();
        }
    }

    @Override
    public void onGetProtocalVerison(int version) {

    }

    @Override
    public void onGetLockState(int id, byte state) {

    }

    @Override
    public void onGetAllLockState(byte[] state) {
        System.arraycopy(state, 0, grimState, 0, state.length);

        for (int i = 0; i < state.length; i++) {
            if (firstInit == true) {
                StateReport sReport = null;
                if (grimState[i] == 0) {
                    sReport = new StateReport(String.valueOf(i + 1), "开");
                } else {
                    sReport = new StateReport(String.valueOf(i + 1), "关");

                }
                stateList.add(sReport);
            } else {
                StateReport sReport0 = stateList.get(i);
                if (grimState[i] == 0) {
                    sReport0.setLockState("开");
                } else {
                    sReport0.setLockState("关");
                }
            }
        }
        firstInit = false;
        //gridStateAdapter.notifyDataSetChanged();
        Message msg = mHandler.obtainMessage();
        msg.what = FRESH_VIEW;
        mHandler.sendMessage(msg);

        Log.e(Constant.TAG,"开锁状态:"+stateList.toString());
    }

    private Handler mHandler = new LockHandler(this);

    private static class LockHandler extends Handler {
        private final WeakReference<OpenBoxActivity> mActivity;

        public LockHandler(OpenBoxActivity activity) {
            mActivity = new WeakReference<OpenBoxActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            OpenBoxActivity pt = mActivity.get();
            if (pt == null) {
                return;
            }
            switch (msg.what) {
                case STATE_LISTEN_MSG:
                    pt.mLockController.getAllDoorState(BOARD_ADDRESS);
                    Logger.d("Handler STATE_LISTEN_MSG ");
                    break;
                case FRESH_VIEW:
                    pt.mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnRightButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void getIntentData() {

    }
}
