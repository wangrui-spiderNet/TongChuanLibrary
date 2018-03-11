package alpha.cyber.intellib.lock;


import java.io.File;

import alpha.cyber.intellib.utils.Logger;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.util.Log;
import android_serialport_api.Command;
import android_serialport_api.SerialPortController;

import android.os.Handler;

public class LockController extends SerialPortController implements LockFunction, SerialPortController.OnDataReceiver {

    private static final String PATH = "/dev/ttyXRM1";
    Handler mHandler;
    private LockCallback mCallback;


    public void setCallBack(LockCallback callback) {
        mCallback = callback;
    }

    public LockController(int baudrate) {
        super(new File(PATH), baudrate);
        // TODO Auto-generated constructor stub
        setOnDataReceiver(this);
        mHandler = new Handler();

    }

    public LockController(File device, int baudrate) {
        super(device, baudrate);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onReceiverData(final Command cmd, byte[] data) {
        // TODO Auto-generated method stub
        //	Logger.i("onReceiverData data = " + HexTools.bytesToHexString(data) + "dataLenth = " + data.length);
        //	Logger.i("onReceiverData data last is " + data[data.length-1]);
        //ToastUtils.showLongToast("receiver: " + HexTools.bytesToHexString(data));

        Log.e(Constant.TAG,"回调：onReceiverData");
        Log.e(Constant.TAG,cmd.toString());
        Log.e(Constant.TAG,new String(data));

        switch (cmd.getCmdId()) {
            case LockCommand.CMD_GETBOARDADDRESS:
                break;
            case LockCommand.CMD_GETLOCKSTATE:
                if (data[2] == 0x00) {
                    Logger.d(" LockCommand.CMD_GETLOCKSTATE:");
                    byte[] dataState = new byte[24];
                    for (int i = 0; i < 24; i++) {
                        if (i > 15) {
                            dataState[i] = (byte) ((data[2] >> (i - 16)) & (0x01));
                        } else if (i > 7) {
                            dataState[i] = (byte) ((data[3] >> (i - 8)) & (0x01));
                        } else {
                            dataState[i] = (byte) ((data[4] >> i) & (0x01));
                        }
                    }
                    mCallback.onGetAllLockState(dataState);
                } else {
                    mCallback.onGetLockState(data[2], data[3]);
                }
                break;
            case LockCommand.CMD_GETPROID:
                mCallback.onGetProtocalVerison(data[3]);
                break;
            case LockCommand.CMD_OPENLOCK:
                if (data[3] == 0x00) {
                    ToastUtils.showLongToast("lockId : " + data[2] + "Opened Successed");
                } else {
                    ToastUtils.showLongToast("lockId : " + data[2] + "Opened Failed ");
                }

                break;
            default:
                break;

        }

    }

    @Override
    public void onTimeout(Command cmd) {
        // TODO Auto-generated method stub
        Logger.i("timeout cmd = " + cmd.toString());ToastUtils.showLongToast("onTimeout: " + cmd.getCmdId());
    }

    @Override
    public void onError(Command cmd, int errorNo) {
        // TODO Auto-generated method stub
        Logger.i("onError cmd = " + cmd.toString());
        ToastUtils.showLongToast("onError: " + cmd.getCmdId());
    }

    @Override
    public void open() {
        Logger.v("Lock open is called");
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        Logger.v("Lock open is called");

    }

    @Override
    public void openGrid(byte doorID, byte boardAddress) {
        // TODO Auto-generated method stub
        Logger.iLine("openGrid");
        LockCommand cmd = LockCommand.openGrid(doorID, boardAddress);
        cmd.setTimeout(100000);
        sendCommand(cmd);

        if(isOpen()){
            mCallback.onGetLockState(doorID,(byte) 1);
        }else{
            mCallback.onGetLockState(doorID,(byte) 0);
        }

    }

    @Override
    public int getBoardAddress() {
        // TODO Auto-generated method stub

        return 0;
    }

    @Override
    public int getDoorState(byte lockID, byte boardAddress) {
        // TODO Auto-generated method stub
        Logger.iLine("getDoorState");
        LockCommand cmd = LockCommand.getDoorState(lockID, boardAddress);
        cmd.setTimeout(100000);
        sendCommand(cmd);
        return 0;
    }

    @Override
    public int getProtocalID(int boardAddr) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getFixedDataLength(Command cmd) {
        // TODO Auto-generated method stub

        if (cmd.getCmdId() == LockCommand.CMD_GETLOCKSTATE) {
            return 7;
        } else {
            return 7;
        }
    }

    @Override
    public int getUnfixedDataLength(Command cmd, byte[] buffer) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getAllDoorState(byte boardAddress) {
        // TODO Auto-generated method stub
        getDoorState((byte) 0x00, boardAddress);
        return 0;
    }


}
