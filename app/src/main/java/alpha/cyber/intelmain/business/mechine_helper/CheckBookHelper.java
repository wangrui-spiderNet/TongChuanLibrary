package alpha.cyber.intelmain.business.mechine_helper;

import android.os.Handler;
import android.os.Message;

import com.rfid.api.ADReaderInterface;
import com.rfid.api.GFunction;
import com.rfid.api.ISO14443AInterface;
import com.rfid.api.ISO14443ATag;
import com.rfid.api.ISO15693Interface;
import com.rfid.api.ISO15693Tag;
import com.rfid.def.ApiErrDefinition;
import com.rfid.def.RfidDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.VoicePlayer;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/26.
 */

public class CheckBookHelper {

    private ADReaderInterface m_reader = new ADReaderInterface();
    private ISO15693Interface mTag = new ISO15693Interface();
    private Thread m_inventoryThrd = null;

    public boolean bUseISO15693 = false;
    public boolean bUseISO14443A = false;

    public static final int INVENTORY_MSG = 11;
    public static final int GETSCANRECORD = 2;
    public static final int INVENTORY_FAIL_MSG = 4;
    public static final int THREAD_END = 3;

    public static final int BORROW_BOOK_INVENTORY_FINISH = 5;
    public static final int BACK_BOOK_INVENTORY_FINISH = 6;

    public static final int ACTION_TYPE_BORROW = 1;
    public static final int ACTION_TYPE_BACK = 2;

    private boolean b_inventoryThreadRun = false;
    private boolean bOnlyReadNew = false;
    private long mAntCfg = 0x000000;
    private boolean bMathAFI = false;
    private byte mAFIVal = 0x00;
    public List<InventoryReport> inventoryList;
    public boolean bRealShowTag = false;
    public boolean bBuzzer = true;
    private int mLoopCnt;
    private Handler mHandler;

    public CheckBookHelper(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public boolean openDevice() {

        String conStr = "";
        conStr = String.format("RDType=RD5100;CommType=COM;ComPath=/dev/ttyXRM0;Baund=38400;Frame=8E1;Addr=255");
        if (!m_reader.isReaderOpen()) {
            if (m_reader.RDR_Open(conStr) == ApiErrDefinition.NO_ERROR) {
                startThread();
                return true;
            } else {
                Log.e(Constant.TAG, "打开设备失败");
            }

        } else {
            startThread();
            return true;
        }

        return false;
    }

    private void startThread() {
        m_inventoryThrd = new Thread(new InventoryThrd());
        m_inventoryThrd.start();
    }

    public void destroyService() {
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

                    while (tagReport != null) {
                        ISO15693Tag ISO15693TagData = new ISO15693Tag();
                        iret = ISO15693Interface.ISO15693_ParseTagDataReport(
                                tagReport, ISO15693TagData);

                        if (iret == ApiErrDefinition.NO_ERROR) {
                            // ISO15693 TAG
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

    public List<InventoryReport> getInventoryList(Message msg) {
        List<InventoryReport> reportList = new ArrayList<InventoryReport>();
        @SuppressWarnings("unchecked")
        Vector<Object> tagList = (Vector<Object>) msg.obj;

        boolean b_find;
        for (int i = 0; i < tagList.size(); i++) {
            b_find = false;
            // ISO15693 TAG
            if (tagList.get(i) instanceof ISO15693Tag) {
                ISO15693Tag tagData = (ISO15693Tag) tagList.get(i);
                String uidStr = GFunction.encodeHexStr(tagData.uid);
                for (int j = 0; j < reportList.size(); j++) {
                    InventoryReport mReport = reportList.get(j);
                    if (mReport.getUidStr().equals(uidStr)) {
                        mReport.setFindCnt(mReport.getFindCnt() + 1);
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = bRealShowTag ? 0 : 1;
                    String tagName = ISO15693Interface
                            .GetTagNameById(tagData.tag_id);
                    reportList.add(new InventoryReport(uidStr,
                            tagName, mCnt));

                }
            } else if (tagList.get(i) instanceof ISO14443ATag) {
                ISO14443ATag tagData = (ISO14443ATag) tagList.get(i);
                String uidStr = GFunction.encodeHexStr(tagData.uid);
                for (int j = 0; j < reportList.size(); j++) {
                    InventoryReport mReport = reportList.get(j);
                    if (mReport.getUidStr().equals(uidStr)) {
                        mReport.setFindCnt(mReport.getFindCnt() + 1);
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = bRealShowTag ? 0 : 1;
                    String tagName = ISO14443AInterface
                            .GetTagNameById(tagData.tag_id);
                    reportList.add(new InventoryReport(uidStr,
                            tagName, mCnt));

                }
            }
        }

        return reportList;
    }

    public String UiReadBlock(int blkAddr, int numOfBlksToRead, ISO15693Interface mTag) {

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

        for(int i=0;i<bufBlocks.length;i++){
            Log.e(Constant.TAG,"--》"+bufBlocks[i]+"《--");
            if(bufBlocks.length==i+1){
                Log.e(Constant.TAG,"-------------------------------------");
            }
        }

        String strData = GFunction.encodeHexStr(bufBlocks);

        return strData;
    }

    private void closeDevice() {
        if (m_inventoryThrd != null && m_inventoryThrd.isAlive()) {
            ToastUtils.showShortToast("正在盘点书籍，请稍后重试");
            return;
        }
        m_reader.RDR_Close();
    }

    public void stopLoop() {
        m_reader.RDR_SetCommuImmeTimeout();
        b_inventoryThreadRun = false;
    }

    public String getBookCode(int i, String uid) {

        byte[] connectUid = GFunction.decodeHex(uid);
        byte connectMode = 1;

        if (null != connectUid) {

            int disconnet = mTag.ISO15693_Disconnect();
            Log.e(Constant.TAG,"UID dis:"+disconnet);

            int iret = mTag.ISO15693_Connect(m_reader,
                    RfidDef.RFID_ISO15693_PICC_ICODE_SLI_ID, connectMode,
                    connectUid);

            Log.e(Constant.TAG, "UID 连接：" + iret);
            Log.e(Constant.TAG,"connect UID:"+uid);

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(Constant.TAG, "UID阻塞异常");
            }

            String bookCode = UiReadBlock(i, 2, mTag);

            return bookCode;
        }

        return null;
    }

    public int getmLoopCnt() {
        return mLoopCnt;
    }

    public void requestBookInfos(List<InventoryReport> reportList, int type) {

        inventoryList = reportList;

        for (int i = 0; i < reportList.size(); i++) {

            String bookCode = getBookCode(i, inventoryList.get(i).getUidStr());

            bookCode = bookCode.substring(6, 14);
            Log.e(Constant.TAG,"借走的书码:"+bookCode);

            getBookInfo(bookCode, type, reportList.size());

        }

    }

    private List<CheckoutListBean> borrowBookList = new ArrayList<CheckoutListBean>();
    private List<CheckoutListBean> backBookList = new ArrayList<CheckoutListBean>();


    private CheckBoxPresenter presenter = new CheckBoxPresenter(new CheckCallBack() {
        @Override
        public void getBookInfoByCode(CheckoutListBean infoBean) {
            if (type == ACTION_TYPE_BORROW) {//借书

                borrowBookList.add(infoBean);
                if (borrowBookList.size() == count) {
                    AppSharedPreference.getInstance().saveBorrowBookInfos(borrowBookList);
                    borrowBookList.clear();

                    Log.e(Constant.TAG,"借书>>>>>>>>");
                    Message msg = mHandler.obtainMessage();
                    msg.what = BORROW_BOOK_INVENTORY_FINISH;
//                        msg.obj = tagList;
//                        msg.arg1 = failedCnt;
                    mHandler.sendMessage(msg);
                }
            } else if (type == ACTION_TYPE_BACK) {//还书

                Log.e(Constant.TAG,"还书>>>>>>>>");
                backBookList.add(infoBean);
                if (backBookList.size() == count) {
                    AppSharedPreference.getInstance().saveBackBookInfos(backBookList);
                    backBookList.clear();

                    Message msg = mHandler.obtainMessage();
                    msg.what = BACK_BOOK_INVENTORY_FINISH;
//                        msg.obj = tagList;
//                        msg.arg1 = failedCnt;
                    mHandler.sendMessage(msg);
                }
            }

        }
    }, MyApplication.getAppContext());

    private int type;
    private int count;
    public void getBookInfo(String bookCode, final int type, final int count) {

        this.type = type;
        this.count = count;

        presenter.getBookInfoByCode(bookCode);

    }


}
