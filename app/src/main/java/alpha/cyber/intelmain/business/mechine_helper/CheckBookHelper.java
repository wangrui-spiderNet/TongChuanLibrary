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

import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.bean.InventoryReport;
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

    public static final int INVENTORY_SINGLE_BOX = 10;
    public static final int INVENTORY_MSG = 11;
    public static final int GETSCANRECORD = 2;
    public static final int INVENTORY_FAIL_MSG = 4;
    public static final int THREAD_END = 3;


    private boolean b_inventoryThreadRun = false;
    private boolean bOnlyReadNew = false;
    private long mAntCfg = 0x000000;
    private boolean bMathAFI = false;
    private byte mAFIVal = 0x00;
    public boolean bRealShowTag = false;
    public boolean bBuzzer = true;
    private int mLoopCnt;
    private Handler mHandler;

    public CheckBookHelper(Handler mHandler) {
        this.mHandler = mHandler;
    }

    private boolean openDevice() {
        String conStr = "";
        conStr = String.format("RDType=RD5100;CommType=COM;ComPath=/dev/ttyXRM0;Baund=38400;Frame=8E1;Addr=255");
        if (!m_reader.isReaderOpen()) {
            if (m_reader.RDR_Open(conStr) == ApiErrDefinition.NO_ERROR) {
                return true;
            } else {
                Log.e(Constant.TAG, "打开设备失败");
            }

        } else {
            return false;
        }

        return false;
    }

    public void startInventoryAllBoxes() {
        if (openDevice()) {
            m_inventoryThrd = new Thread(new InventoryThrd());
            m_inventoryThrd.start();
        }
    }

    /**
     * 盘点某根天线的柜子
     *
     * @param address
     */
    private int mAntCnt = 1;//天线数量

    public void startInventoryOneBox(byte address) {

        if (openDevice()) {
            int iret = m_reader.RDR_SetAcessAntenna(address);
            Vector<Object> tagList = new Vector<Object>();
            if (iret == ApiErrDefinition.NO_ERROR) {//设置天线成功

                byte[] mAntId = null;
                mAntCnt = m_reader.RDR_GetAntennaInterfaceCount();
                if (mAntCnt > 1) {
                    mAntId = new byte[1];
                    mAntId[0] = (address);
                }

                Object hInvenParamSpecList = ADReaderInterface.RDR_CreateInvenParamSpecList();
                ISO15693Interface.ISO15693_CreateInvenParam(hInvenParamSpecList, (byte) 0, false, (byte) 0,
                        (byte) 0);

                iret = m_reader.RDR_TagInventory(RfidDef.AI_TYPE_NEW,
                        mAntId, 0, hInvenParamSpecList);
                if (iret != ApiErrDefinition.NO_ERROR) {
                    return;
                }

                Object tagReport = m_reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_FIRST);
                while (tagReport != null) {

                    ISO15693Tag tagData = new ISO15693Tag();
                    iret = ISO15693Interface.ISO15693_ParseTagDataReport(tagReport, tagData);
                    if (iret == ApiErrDefinition.NO_ERROR) {
                        tagList.add(tagData);
                    }

                    tagReport = m_reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_NEXT);
                }

                Message msg = mHandler.obtainMessage();
                msg.what = INVENTORY_SINGLE_BOX;
                msg.arg1 = address;
                msg.obj = tagList;
                mHandler.sendMessage(msg);

            }
        }

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

    private final int BOX_COUNT = 10;

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
                for (int i = 0; i < BOX_COUNT; i++) {
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
                hInvenParamSpecList = ADReaderInterface.RDR_CreateInvenParamSpecList();
                if (bUseISO15693) {
                    ISO15693Interface.ISO15693_CreateInvenParam(hInvenParamSpecList,
                            (byte) 0, bMathAFI, mAFIVal, (byte) 0);
                }
                if (bUseISO14443A) {
                    ISO14443AInterface.ISO14443A_CreateInvenParam(hInvenParamSpecList, (byte) 0);
                }
            }

            mLoopCnt = 0;
            b_inventoryThreadRun = true;
            while (b_inventoryThreadRun) {
                if (mHandler.hasMessages(INVENTORY_MSG)) {
                    continue;
                }
                int iret = m_reader.RDR_TagInventory(newAI, useAnt, 0, hInvenParamSpecList);

                if (iret == ApiErrDefinition.NO_ERROR || iret == -ApiErrDefinition.ERR_STOPTRRIGOCUR) {
                    Vector<Object> tagList = new Vector<Object>();

                    newAI = RfidDef.AI_TYPE_NEW;

                    if (bOnlyReadNew || iret == -ApiErrDefinition.ERR_STOPTRRIGOCUR) {
                        newAI = RfidDef.AI_TYPE_CONTINUE;
                    }

                    Object tagReport = m_reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_FIRST);

                    while (tagReport != null) {
                        ISO15693Tag ISO15693TagData = new ISO15693Tag();

                        iret = ISO15693Interface.ISO15693_ParseTagDataReport(tagReport, ISO15693TagData);

                        if (iret == ApiErrDefinition.NO_ERROR) {
                            // ISO15693 TAG
                            tagList.add(ISO15693TagData);
                            tagReport = m_reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_NEXT);
                            continue;
                        }

                        ISO14443ATag ISO14444ATagData = new ISO14443ATag();
                        iret = ISO14443AInterface.ISO14443A_ParseTagDataReport(tagReport, ISO14444ATagData);
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
                    msg.arg1 = (mLoopCnt - failedCnt);
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
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = bRealShowTag ? 0 : 1;
                    String tagName = ISO15693Interface
                            .GetTagNameById(tagData.tag_id);
                    reportList.add(new InventoryReport(uidStr,
                            tagName, msg.arg1));

                }
            } else if (tagList.get(i) instanceof ISO14443ATag) {
                ISO14443ATag tagData = (ISO14443ATag) tagList.get(i);
                String uidStr = GFunction.encodeHexStr(tagData.uid);
                for (int j = 0; j < reportList.size(); j++) {
                    InventoryReport mReport = reportList.get(j);
                    if (mReport.getUidStr().equals(uidStr)) {
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = bRealShowTag ? 0 : 1;
                    String tagName = ISO14443AInterface
                            .GetTagNameById(tagData.tag_id);
                    reportList.add(new InventoryReport(uidStr,
                            tagName, msg.arg1));

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

        for (int i = 0; i < bufBlocks.length; i++) {
            Log.e(Constant.TAG, "--》" + bufBlocks[i] + "《--");
            if (bufBlocks.length == i + 1) {
                Log.e(Constant.TAG, "-------------------------------------");
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
            Log.e(Constant.TAG, "UID dis:" + disconnet);

            int iret = mTag.ISO15693_Connect(m_reader,
                    RfidDef.RFID_ISO15693_PICC_ICODE_SLI_ID, connectMode,
                    connectUid);

            Log.e(Constant.TAG, "UID 连接：" + iret);
            Log.e(Constant.TAG, "connect UID:" + uid);

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

}
