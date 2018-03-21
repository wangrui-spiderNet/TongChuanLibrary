package android_serialport_api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import alpha.cyber.intellib.utils.HexTools;
import alpha.cyber.intellib.utils.Logger;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.util.Log;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.qq.taf.jce.HexUtil;

public abstract class SerialPortController {

    public SerialPort mSerialPort = null;

    private InputStream mInputStream;

    private OutputStream mOutputStream;

    public interface OnDataReceiver {

        public static final int SUCCESS = 0;

        public static final int TIMEOUT = -1;

        public static final int ERROR = -2;

        public void onReceiverData(Command cmd, byte[] data);

        public void onTimeout(Command cmd);

        public void onError(Command cmd, int errorNo);

    }


    private OnDataReceiver mOnDataReceiver;

    public void setOnDataReceiver(OnDataReceiver receiver) {
        mOnDataReceiver = receiver;
    }

    public SerialPortController(File device, int baudrate) {
        try {
            mSerialPort = new SerialPort(device, baudrate, 0);
            if (mSerialPort == null) {
                Logger.e("打开串口失败  ");
                return;
            }
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public InputStream getInputStream() {
        return mInputStream;
    }


    public OutputStream getOutputStream() {
        return mOutputStream;
    }


    public void start() {
        mCmdThread = new CmdThread();
        mCmdThread.start();
    }

    public void stop() {
        if (isOpen()) {
            mCmdThread.quit();
            mCmdThread = null;
        }
    }

    public boolean isOpen() {
        return (mCmdThread != null && mCmdThread.isRunning());
    }

    public void sendCommand(Command cmd) {
        if (isOpen()) {
            Logger.iLine("sendCommand cmd " + cmd.toString());
            mCmdThread.sendCommand(cmd);
        }
    }

    public void clear() {
        byte buf[] = new byte[4096];
        try {
            while (null!=mInputStream&&mInputStream.available() > 0) {
                mInputStream.read(buf);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 固定数据长度
    abstract public int getFixedDataLength(Command cmd);

    // 通过协议头解析数据长度
    abstract public int getUnfixedDataLength(Command cmd, byte[] buffer);


    private CmdThread mCmdThread;


    class CmdThread extends Thread {
        private CmdHandler mCmdHandler;

        private boolean bRunning = false;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();
            bRunning = true;
            mCmdHandler = new CmdHandler();
            Looper.loop();
        }

        public boolean isRunning() {
            return bRunning;
        }

        public void quit() {
            mCmdHandler.sendEmptyMessage(CmdHandler.QUIT);
            bRunning = false;
        }

        public void sendCommand(Command cmd) {
            Message msg = mCmdHandler.obtainMessage();
            msg.what = CmdHandler.CMD;
            msg.obj = cmd;
            msg.sendToTarget();
        }
    }


    private void receiverData(Command cmd, int errorNo, byte[] data) {
        if (mOnDataReceiver != null) {
            if (errorNo == OnDataReceiver.SUCCESS) {
                mOnDataReceiver.onReceiverData(cmd, data);
            } else if (errorNo == OnDataReceiver.TIMEOUT) {
                mOnDataReceiver.onTimeout(cmd);
            } else {
                mOnDataReceiver.onError(cmd, errorNo);
            }
        }
    }


    class CmdHandler extends Handler {

        private static final int QUIT = 0x1001;

        private static final int CMD = 0x1002;

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case QUIT:
                    quit();
                    break;
                case CMD:
                    this.sendCommand((Command) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }


        private void quit() {
            Looper.myLooper().quit();
        }

        public void sendCommand(Command cmd) {
            Logger.i("CmdHandler sendCommand cmd = " + cmd.toString());
            clear();
            doSendCommand(cmd);
            doReceiverData(cmd);
        }

    }

    private void doSendCommand(Command cmd) {
        try {

            if(null==mOutputStream){
                return;
            }

            mOutputStream.write(cmd.getCmd());
            Logger.i("doSendCommand : " + HexTools.bytesToHexString(cmd.getCmd()));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void doReceiverData(Command cmd) {

        try {
            long start_time = System.currentTimeMillis();
            byte[] buffer = new byte[4096];
//			byte[] head = new byte[8];
            byte[] data = null;
            int dataPos = 0;
            if (cmd.getDataLenghtType() == Command.DataLengthType.FIXED) {
                data = new byte[getFixedDataLength(cmd)];
                Logger.i("dateLenghType >>>> is fixed 8");
            }
            int recvLen = 0;
            int dataLen = 0;
            long timeout = cmd.getTimeout();

            do {
//                sleep(5);
                recvLen = 0;
                if (null!=mInputStream&&mInputStream.available() > 0) {
                    recvLen = mInputStream.read(buffer);
                }
                //		Logger.eLine("recvLen = " + recvLen);
                if (recvLen <= 0) {
                    continue;
                }

                if (data == null) {

                    data = new byte[getUnfixedDataLength(cmd, buffer) + 11];
                    //System.arraycopy(buffer, 0, head, 0, 8);
//                    Log.e(" tu onReceiverData >>>> data = " + HexTools.bytesToHexString(data));
                    //break;
                }

                dataLen += recvLen;
//                for(int i=0;i<data.length;i++){
//                    Log.e(Constant.TAG,"data:"+Integer.toHexString(data[i]));
//                }
//                Log.e(Constant.TAG,"recvLen:"+recvLen);
//                Log.e(Constant.TAG,"dataPos:"+dataPos);

//                Log.e(Constant.TAG,"recvLen = " + recvLen + " buffer:" + HexTools.bytesToHexString(buffer));
                System.arraycopy(buffer, 0, data, dataPos, recvLen);
                //dataPos += recvLen;

//                Log.e(Constant.TAG,"recvLen = " + recvLen + " buffer:" + HexTools.bytesToHexString(buffer));

                if (data.length == dataLen) {
//                    Logger.i("DataLength Get is :" + dataLen);
                    break;
                }
            }while ((System.currentTimeMillis() - start_time) < timeout);

            if ((System.currentTimeMillis() - start_time) < timeout) {
                if (dataLen > 0 && data.length == dataLen) {
                    receiverData(cmd, OnDataReceiver.SUCCESS, data);
                } else {
                    receiverData(cmd, OnDataReceiver.ERROR, null);
                }

            } else {

                receiverData(cmd, OnDataReceiver.TIMEOUT, null);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            receiverData(cmd, OnDataReceiver.ERROR, null);
            e.printStackTrace();
        }

    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
