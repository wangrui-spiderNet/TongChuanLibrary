package alpha.cyber.intelmain.business.mechine_helper;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intellib.lock.LockController;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.business.borrowbook.OpenBoxActivity;
import alpha.cyber.intelmain.business.borrowbook.StateReport;
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
    public static final int STATE_LISTEN_MSG = 101;
    public static final byte BOARD_ADDRESS = 0x01;

    private List<StateReport> stateList = new ArrayList<StateReport>();
    private byte[] grimState = new byte[24];
    private boolean firstInit = true;

    public LockHelper(Handler mHandler){
        this.mHandler = mHandler;
    }

    public LockController initController(LockCallback callback) {
        mLockController = new LockController(9600);

        if (mLockController.mSerialPort == null) {
            ToastUtils.showShortToast("打开设备失败");
            lc = false;
            return mLockController;
        }else{
            lc = true;
            ToastUtils.showShortToast("打开设备成功");
        }
        mLockController.setCallBack(callback);

        return mLockController;
    }

    public void getAllDoorState(){
        mLockController.getAllDoorState(LockHelper.BOARD_ADDRESS);
    }

    public boolean open(LockCallback back) {

        if (lc) {
            startOpen();
            return true;
        } else {
            initController(back);
            startOpen();
        }

        return false;
    }

    private void startOpen() {
        mLockController.start();
        mLockController.open();
        mStateThrd = new Thread(new StateThrd());
        mStateThrd.start();
    }

    public void openGride(int position){
        mLockController.openGrid((byte) (position + 1), BOARD_ADDRESS);
    }

    public void close() {
        b_stateThreadRun = false;
        if(null!=mStateThrd){
            mStateThrd.interrupt();
        }
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


}
