package alpha.cyber.intelmain.business.mechine_helper;

import android.os.Handler;
import android.os.Message;

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intellib.lock.LockController;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/3/8.
 */

public class LockHelper {

    private LockController mLockController;
    private boolean lc;
    private Thread mStateThrd = null;
    private boolean b_stateThreadRun = false;
    private Handler mHandler;
    public static final int STATE_LISTEN_MSG = 1;
    public static final byte BOARD_ADDRESS = 0x01;
    public static final int BOX_OPEN = 102;
    public static final int HAS_DOOR_NOT_CLOSED = 103;
    public static final int OPENED_CHECKING_BOOKS = 104;
    private LockCallback callback;

    public LockHelper(Handler mHandler, LockCallback callback) {
        this.mHandler = mHandler;
        this.callback = callback;
        initController();
    }

    private LockController initController() {

        if (null == mLockController) {
            mLockController = new LockController(9600);
        }

        if (mLockController.mSerialPort == null) {
            lc = false;
            return mLockController;
        } else {
            lc = true;
        }

        mLockController.setCallBack(callback);

        return mLockController;
    }

    public void getAllDoorState() {
        mLockController.getAllDoorState(LockHelper.BOARD_ADDRESS);
    }

    public int getLockState(byte lockID){
        if(open()){
            initController();
        }
        return mLockController.getDoorState(lockID,BOARD_ADDRESS);
    }

    public boolean open() {
        startOpen();
        return true;
    }

    private void startOpen() {
        mLockController.start();
        mLockController.open();
    }

    public void openGride(int position) {
        mLockController.openGrid((byte) (position), BOARD_ADDRESS);

    }

    public void close() {
        b_stateThreadRun = false;
        if (null != mStateThrd) {
            mStateThrd.interrupt();
        }
    }

    public boolean checkBoxOpen(byte[] state) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) {

                Log.e(Constant.TAG, i + ":开");

                Message msg = mHandler.obtainMessage();
                msg.what = BOX_OPEN;
                msg.obj = i;
                mHandler.sendMessage(msg);

                return true;
            } else {
            }
        }

        return false;
    }

}
