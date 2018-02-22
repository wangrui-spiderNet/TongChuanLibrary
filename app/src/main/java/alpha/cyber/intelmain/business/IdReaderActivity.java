package alpha.cyber.intelmain.business;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import alpha.cyber.intellib.utils.HexTools;
import alpha.cyber.intellib.utils.Logger;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.serial.SerialPort;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class IdReaderActivity extends BaseActivity {

    public SerialPort mIdReaderSerialPort = null;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private boolean b_IdThreadRun = false;
    private static final int NUMMER_READED = 1;
    private static final String PATH = "/dev/ttyS1";
    private Thread mIdThread = null;
    private File device = new File(PATH);
    private static final int baudrate = 9600;
    private byte[] IdNum = new byte[8];
    private TextView IdNumTxt = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_reader);
        IdNumTxt = (TextView) findViewById(R.id.nummber_display);
        IdNumTxt.setText(HexTools.bytesToHexString(IdNum));
        if (initSerialPort()) {
            mIdThread = new Thread(new idListenThrd());
            mIdThread.start();
        }
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    private boolean initSerialPort() {
        try {
            mIdReaderSerialPort = new SerialPort(device, baudrate, 0);
            if (mIdReaderSerialPort == null) {
                Logger.e("打开串口失败  ");
                return false;
            }
            mInputStream = mIdReaderSerialPort.getInputStream();
            mOutputStream = mIdReaderSerialPort.getOutputStream();

        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Handler mHandler = new IdReaderHandler(this);

    private static class IdReaderHandler extends Handler {
        private final WeakReference<IdReaderActivity> mActivity;

        public IdReaderHandler(IdReaderActivity activity) {
            mActivity = new WeakReference<IdReaderActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            IdReaderActivity pt = mActivity.get();
            if (pt == null) {
                return;
            }
            switch (msg.what) {
                case NUMMER_READED:
                    pt.IdNumTxt.setText(HexTools.bytesToHexString(pt.IdNum));
                    break;
                default:
                    break;
            }
        }
    }

    private class idListenThrd implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            b_IdThreadRun = true;
            int recvLen = 0;
            while (b_IdThreadRun) {
                try {
                    Thread.sleep(100);
                    byte[] buffer = new byte[8];
                    if (mInputStream.available() > 0) {
                        recvLen = mInputStream.read(buffer);
                        System.arraycopy(buffer, 0, IdNum, 0, recvLen);
                        Message msg = mHandler.obtainMessage();
                        msg.what = NUMMER_READED;
                        mHandler.sendMessage(msg);
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

}
