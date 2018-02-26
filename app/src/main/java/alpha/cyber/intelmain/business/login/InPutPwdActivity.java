package alpha.cyber.intelmain.business.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rfid.api.ADReaderInterface;
import com.rfid.api.GFunction;
import com.rfid.api.ISO14443AInterface;
import com.rfid.api.ISO14443ATag;
import com.rfid.api.ISO15693Interface;
import com.rfid.api.ISO15693Tag;
import com.rfid.def.ApiErrDefinition;
import com.rfid.def.RfidDef;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import alpha.cyber.intellib.utils.Logger;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.VoicePlayer;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.business.operation.OperatorActivity;
import alpha.cyber.intelmain.business.operation.OperatorPresenter;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.StringUtils;
import alpha.cyber.intelmain.util.ToastUtil;

/**
 * Created by wangrui on 2018/1/31.
 */

public class InPutPwdActivity extends BaseActivity implements View.OnClickListener, IUserView {

    private EditText etPWd, etAccount;
    private Button btnLogin;

    private ADReaderInterface m_reader = new ADReaderInterface();
    private ISO15693Interface mTag = new ISO15693Interface();
    private Thread m_inventoryThrd = null;
    private boolean b_inventoryThreadRun = false;
    private boolean bOnlyReadNew = false;
    private long mAntCfg = 0x000000;
    private long mLoopCnt = 0;
    private boolean bMathAFI = false;
    private byte mAFIVal = 0x00;
    private boolean bRealShowTag = false;
    private boolean bBuzzer = true;
    private boolean bUseISO15693 = false;
    private boolean bUseISO14443A = false;

    private static final int INVENTORY_MSG = 1;
    private static final int GETSCANRECORD = 2;
    private static final int INVENTORY_FAIL_MSG = 4;
    private static final int THREAD_END = 3;

    private OperatorPresenter presenter;

    private List<InventoryReport> inventoryList = new ArrayList<InventoryReport>();

    private BookDao bookDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pwd);
        bookDao = new BookDao(InPutPwdActivity.this);
    }

    @Override
    protected void findWidgets() {
        etPWd = findView(R.id.et_pwd);
        etAccount = findView(R.id.et_account);
        btnLogin = findView(R.id.btn_login);
        btnRightButton.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(this);

        etAccount.setText(Constant.cardnum);
        etPWd.setText(Constant.pwd);
        presenter = new OperatorPresenter(this, this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {

            if (StringUtils.isEmpty(etAccount.getText().toString())) {
                ToastUtil.showToast("账号不能为空");
                return;
            }

            if (StringUtils.isEmpty(etPWd.getText().toString())) {
                ToastUtil.showToast("密码不能为空");
                return;
            }

            finish();
            AppSharedPreference.getInstance().setLogIn(true);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.ACCOUNT, etAccount.getText().toString());
            bundle.putString(Constant.PASSWORD, etPWd.getText().toString());
            IntentUtils.startAty(this, OperatorActivity.class, bundle);



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //扫描全书柜，更新到本地数据库中
        openDevice();

//        String time = DateUtils.getSystemTime();
//        String bookcode = "00834470";
//        String bookcode2 = "00834472";
//
//        String request = "17001" + time + "AO|AB" + bookcode + "|AY0AZ";
//        String request2 = "17001" + time + "AO|AB" + bookcode2 + "|AY0AZ";
//        presenter.getBookInfo(request);
//        presenter.getBookInfo(request2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (m_reader.isReaderOpen()) {
            // If thread of inventory is running,stop the thread before exit the
            // application.
            if (m_inventoryThrd != null && m_inventoryThrd.isAlive()) {
                b_inventoryThreadRun = false;
                m_reader.RDR_SetCommuImmeTimeout();
                try {
                    m_inventoryThrd.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            closeDevice();
        }
    }

    private void openDevice() {
        String conStr = "";
        conStr = String.format("RDType=RD5100;CommType=COM;ComPath=/dev/ttyXRM0;Baund=38400;Frame=8E1;Addr=255");
        if (m_reader.RDR_Open(conStr) == ApiErrDefinition.NO_ERROR) {
            ToastUtils.showShortToast("打开设备成功");

            m_inventoryThrd = new Thread(new InventoryThrd());
            m_inventoryThrd.start();
        } else {
            ToastUtils.showShortToast("打开设备失败");
        }

    }

    private void closeDevice() {
        if (m_inventoryThrd != null && m_inventoryThrd.isAlive()) {
            ToastUtils.showShortToast(getString(R.string.tx_msg_stopInventory_tip));
            return;
        }
        m_reader.RDR_Close();

    }


    private class InventoryThrd implements Runnable {
        @Override
        public void run() {
            int failedCnt = 0;
            Object hInvenParamSpecList = null;
            byte newAI = RfidDef.AI_TYPE_NEW;
            byte useAnt[] = null;
            if (bOnlyReadNew) {
                newAI = RfidDef.AI_TYPE_CONTINUE;
            }
            if (mAntCfg != 0) {
                Vector<Byte> vAntList = new Vector<Byte>();
                for (int i = 0; i < 32; i++) {
                    if ((mAntCfg & (1 << i)) != 0) {
                        vAntList.add((byte) (i + 1));
                    }
                }

                useAnt = new byte[vAntList.size()];
                for (int i = 0; i < useAnt.length; i++) {
                    useAnt[i] = vAntList.get(i);
                }
            }

            if (bUseISO14443A || bUseISO15693) {
                hInvenParamSpecList = ADReaderInterface
                        .RDR_CreateInvenParamSpecList();
                if (bUseISO15693) {
                    ISO15693Interface.ISO15693_CreateInvenParam(
                            hInvenParamSpecList, (byte) 0, bMathAFI, mAFIVal,
                            (byte) 0);
                }
                if (bUseISO14443A) {
                    ISO14443AInterface.ISO14443A_CreateInvenParam(
                            hInvenParamSpecList, (byte) 0);
                }
            }

            mLoopCnt = 0;
            b_inventoryThreadRun = true;
            while (b_inventoryThreadRun) {
                if (mHandler.hasMessages(INVENTORY_MSG)) {
                    continue;
                }
                int iret = m_reader.RDR_TagInventory(newAI, useAnt, 0,
                        hInvenParamSpecList);

                Logger.d("iret = " + iret);
                if (iret == ApiErrDefinition.NO_ERROR
                        || iret == -ApiErrDefinition.ERR_STOPTRRIGOCUR) {
                    Vector<Object> tagList = new Vector<Object>();
                    newAI = RfidDef.AI_TYPE_NEW;
                    if (bOnlyReadNew
                            || iret == -ApiErrDefinition.ERR_STOPTRRIGOCUR) {
                        newAI = RfidDef.AI_TYPE_CONTINUE;
                    }

                    Object tagReport = m_reader
                            .RDR_GetTagDataReport(RfidDef.RFID_SEEK_FIRST);
                    Logger.d("tagReport = " + tagReport);
                    while (tagReport != null) {
                        ISO15693Tag ISO15693TagData = new ISO15693Tag();
                        iret = ISO15693Interface.ISO15693_ParseTagDataReport(
                                tagReport, ISO15693TagData);
                        Logger.d(" ISO15693_ParseTagDataReport iret = " + iret);
                        if (iret == ApiErrDefinition.NO_ERROR) {
                            // ISO15693 TAG
                            Logger.d("ApiErrDefinition");
                            tagList.add(ISO15693TagData);
                            tagReport = m_reader
                                    .RDR_GetTagDataReport(RfidDef.RFID_SEEK_NEXT);
                            continue;
                        }

                        ISO14443ATag ISO14444ATagData = new ISO14443ATag();
                        iret = ISO14443AInterface.ISO14443A_ParseTagDataReport(
                                tagReport, ISO14444ATagData);
                        if (iret == ApiErrDefinition.NO_ERROR) {
                            // ISO14443A TAG
                            tagList.add(ISO14444ATagData);
                            tagReport = m_reader
                                    .RDR_GetTagDataReport(RfidDef.RFID_SEEK_NEXT);
                            continue;
                        }
                    }
                    Logger.d("mLoopCnt = " + mLoopCnt);
                    mLoopCnt++;
                    Message msg = mHandler.obtainMessage();
                    msg.what = INVENTORY_MSG;
                    msg.obj = tagList;
                    msg.arg1 = failedCnt;
                    mHandler.sendMessage(msg);
                } else {
                    mLoopCnt++;
                    newAI = RfidDef.AI_TYPE_NEW;
                    if (b_inventoryThreadRun) {
                        failedCnt++;
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = INVENTORY_FAIL_MSG;
                    msg.arg1 = failedCnt;
                    mHandler.sendMessage(msg);
                }
            }
            b_inventoryThreadRun = false;
            m_reader.RDR_ResetCommuImmeTimeout();
            mHandler.sendEmptyMessage(THREAD_END);
        }

    }

    private Handler mHandler = new MyHandler(this);

    private class MyHandler extends Handler {
        private final WeakReference<InPutPwdActivity> mActivity;

        public MyHandler(InPutPwdActivity activity) {
            mActivity = new WeakReference<InPutPwdActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            InPutPwdActivity pt = mActivity.get();
            if (pt == null) {
                return;
            }
            boolean b_find = false;
            switch (msg.what) {
                case INVENTORY_MSG:

                    getInventoryList(pt, msg);

                    Log.e(Constant.TAG, pt.inventoryList.toString());

                    clearBookDb();

                    byte connectMode = 0;

                    for (int i = 0; i < inventoryList.size(); i++) {

                        byte[] connectUid = GFunction.decodeHex(inventoryList.get(i).getUidStr());
                        if (connectUid != null) {
                            int iret = mTag.ISO15693_Connect(m_reader,
                                    RfidDef.RFID_ISO15693_PICC_ICODE_SLI_ID, connectMode,
                                    connectUid);

                            Log.e(Constant.TAG, "连接：" + iret);

                            String time = DateUtils.getSystemTime();
                            String bookinfo_request = getResources().getString(R.string.bookinfo_request);

                            String bookCode = UiReadBlock(i, 2);
                            bookCode = bookCode.substring(6,14);

                            String bookinfo_format = String.format(bookinfo_request, time,bookCode);
                            presenter.getBookInfo(bookinfo_format);
                        }

                    }

                    stopLoop();

                    break;
                case INVENTORY_FAIL_MSG:

                    Log.e(Constant.TAG, "》》》》》失败》》》》》");

                    break;
                case THREAD_END:

                    break;
                default:
                    break;
            }
        }
    }

    private void stopLoop() {
        m_reader.RDR_SetCommuImmeTimeout();
        b_inventoryThreadRun = false;
    }

    private void getInventoryList(InPutPwdActivity pt, Message msg) {
        @SuppressWarnings("unchecked")
        Vector<Object> tagList = (Vector<Object>) msg.obj;
        if (pt.bRealShowTag && !pt.inventoryList.isEmpty()) {
            pt.inventoryList.clear();
        }
        if (!tagList.isEmpty() && pt.bBuzzer) {
            VoicePlayer.GetInst(pt).Play();
        }
        boolean b_find;
        for (int i = 0; i < tagList.size(); i++) {
            b_find = false;
            // ISO15693 TAG
            if (tagList.get(i) instanceof ISO15693Tag) {
                ISO15693Tag tagData = (ISO15693Tag) tagList.get(i);
                String uidStr = GFunction.encodeHexStr(tagData.uid);
                for (int j = 0; j < pt.inventoryList.size(); j++) {
                    InventoryReport mReport = pt.inventoryList.get(j);
                    if (mReport.getUidStr().equals(uidStr)) {
                        mReport.setFindCnt(mReport.getFindCnt() + 1);
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = pt.bRealShowTag ? 0 : 1;
                    String tagName = ISO15693Interface
                            .GetTagNameById(tagData.tag_id);
                    pt.inventoryList.add(new InventoryReport(uidStr,
                            tagName, mCnt));

                }
            } else if (tagList.get(i) instanceof ISO14443ATag) {
                ISO14443ATag tagData = (ISO14443ATag) tagList.get(i);
                String uidStr = GFunction.encodeHexStr(tagData.uid);
                for (int j = 0; j < pt.inventoryList.size(); j++) {
                    InventoryReport mReport = pt.inventoryList.get(j);
                    if (mReport.getUidStr().equals(uidStr)) {
                        mReport.setFindCnt(mReport.getFindCnt() + 1);
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = pt.bRealShowTag ? 0 : 1;
                    String tagName = ISO14443AInterface
                            .GetTagNameById(tagData.tag_id);
                    pt.inventoryList.add(new InventoryReport(uidStr,
                            tagName, mCnt));

                }
            }

        }
    }

    private void clearBookDb() {
        bookDao.deleteAll();
    }

    private String UiReadBlock(int blkAddr, int numOfBlksToRead) {
        if (blkAddr + numOfBlksToRead > 28) {// 数据块地址溢出
            numOfBlksToRead = 28 - blkAddr;
        }
        Integer numOfBlksRead = 0;
        Long bytesBlkDatRead = (long) 0;
        byte bufBlocks[] = new byte[4 * numOfBlksToRead];
        int iret = mTag.ISO15693_ReadMultiBlocks(false, blkAddr,
                numOfBlksToRead, numOfBlksRead, bufBlocks, bytesBlkDatRead);
        if (iret != ApiErrDefinition.NO_ERROR) {
            Log.e(Constant.TAG, "错误");
        }

        String strData = GFunction.encodeHexStr(bufBlocks);
        Log.e(Constant.TAG,"转换后的书码："+strData);

        return strData;
    }

    @Override
    public void getUserInfo(UserInfoBean userinfoBean) {

    }

    @Override
    public void getAllBoxBooks(BookInfoBean infoBean) {

        bookDao.insertBook(infoBean);
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

}
