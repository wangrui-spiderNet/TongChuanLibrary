package alpha.cyber.intelmain.business;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intellib.lock.LockController;
import alpha.cyber.intellib.utils.Logger;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class LockTestActivity extends BaseActivity implements OnClickListener, LockCallback {

    private LockController mLockController = null;
    private EditText mEditId = null;
    private Button mOpen;
    private Button mClose;
    private Button mOpenDoor;
    private boolean b_stateThreadRun = false;
    private Thread mStateThrd = null;
    private ListView listView = null;
    private GridStateAdapter gridStateAdapter = null;
    private List<StateReport> stateList = new ArrayList<StateReport>();
    private byte[] grimState = new byte[24];
    private boolean firstInit = true;

    private static final int STATE_LISTEN_MSG = 1;
    private static final int FRESH_VIEW = 2;
    private static final byte BOARD_ADDRESS = 0x01;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        setupViews();
        listView = (ListView) findViewById(R.id.list_lock_state);
        gridStateAdapter = new GridStateAdapter(this, stateList);
        listView.setAdapter(gridStateAdapter);
    }

    private void setupViews() {
        mOpen = (Button) findViewById(R.id.open);
        mClose = (Button) findViewById(R.id.close);
        mOpenDoor = (Button) findViewById(R.id.opendoor);
        mEditId = (EditText) findViewById(R.id.id);
        mOpen.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mOpenDoor.setOnClickListener(this);
        disableFuncation();
    }

    @Override
    protected void findWidgets() {

    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

    private boolean checkFunctionEnable() {
        if (mLockController != null) {
            enableFunction();
            return true;
        }
        disableFuncation();
        return false;
    }


    private void enableFunction() {
        mOpenDoor.setEnabled(true);
        mClose.setEnabled(true);
    }

    private void disableFuncation() {
        mOpenDoor.setEnabled(false);
        mClose.setEnabled(false);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.open:
                open();
                break;
            case R.id.close:
                close();
                break;
            case R.id.opendoor:
                if (checkId()) {
                    int doorID = Integer.valueOf(mEditId.getText().toString());
                    mLockController.openGrid((byte) doorID, BOARD_ADDRESS);
                }
                break;
        }

    }

    private void open() {
        boolean lc = false;
        lc = initController();
        if (lc) {
            mLockController.start();
            mLockController.open();
            enableFunction();
            mStateThrd = new Thread(new StateThrd());
            mStateThrd.start();
            mOpen.setEnabled(false);

        } else {
            ToastUtils.showShortToast("打开设备失败");
        }

    }

    private void close() {
        b_stateThreadRun = false;
        mStateThrd.interrupt();
        mOpen.setEnabled(true);
        disableFuncation();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        b_stateThreadRun = false;
        mOpen.setEnabled(true);
        disableFuncation();
        if (mStateThrd != null) {
            mStateThrd.interrupt();
        }
    }

    private boolean checkId() {
        String text = mEditId.getText().toString();
        if (TextUtils.isEmpty(text)) {
            //提示Id不能为空
            ToastUtils.showShortToast("ID不能为空");
            return false;
        }

        int id = Integer.valueOf(text);
        if (id <= 0 || id >= 11) {
            // 提示id范围[1, 10]
            ToastUtils.showShortToast("ID必须为1-10");
            return false;
        }

        return true;
    }


    private Handler mHandler = new LockHandler(this);

    private static class LockHandler extends Handler {
        private final WeakReference<LockTestActivity> mActivity;

        public LockHandler(LockTestActivity activity) {
            mActivity = new WeakReference<LockTestActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LockTestActivity pt = mActivity.get();
            if (pt == null) {
                return;
            }
            switch (msg.what) {
                case STATE_LISTEN_MSG:
                    pt.mLockController.getAllDoorState(BOARD_ADDRESS);
                    Logger.d("Handler STATE_LISTEN_MSG ");
                    break;
                case FRESH_VIEW:
                    pt.gridStateAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    private class StateThrd implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            b_stateThreadRun = true;

            while (b_stateThreadRun) {
                try {
                    Thread.sleep(200);
                    Message msg = mHandler.obtainMessage();
                    msg.what = STATE_LISTEN_MSG;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    static public class GridStateAdapter extends BaseAdapter {
        private List<StateReport> list;
        private LayoutInflater inflater;

        public GridStateAdapter(Context context, List<StateReport> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StateReport stateReport = (StateReport) this
                    .getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater
                        .inflate(R.layout.locklist_tittle, null);
                viewHolder.mLockId = (TextView) convertView
                        .findViewById(R.id.lock_id);
                viewHolder.mLockState = (TextView) convertView
                        .findViewById(R.id.lock_state);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mLockId.setText(stateReport.getLockId());
            viewHolder.mLockState.setText(stateReport.getLockState());

            return convertView;
        }

        private class ViewHolder {
            public TextView mLockId;
            public TextView mLockState;
        }
    }


    public static class StateReport {

        private String lockId;
        private String lockState;

        public StateReport() {
            super();
        }

        public StateReport(String lockId, String lockState) {
            super();
            this.setLockId(lockId);
            this.setLockState(lockState);
        }


        public String getLockId() {
            return lockId;
        }

        public void setLockId(String lockId) {
            this.lockId = lockId;
        }

        public String getLockState() {
            return lockState;
        }

        public void setLockState(String lockState) {
            this.lockState = lockState;
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
        Logger.d("onGetAllLockState");
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
    }


}
