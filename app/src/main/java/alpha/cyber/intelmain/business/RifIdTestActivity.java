package alpha.cyber.intelmain.business;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.rfid.api.ADReaderInterface;
import com.rfid.api.GFunction;
import com.rfid.api.ISO14443AInterface;
import com.rfid.api.ISO14443ATag;
import com.rfid.api.ISO15693Interface;
import com.rfid.api.ISO15693Tag;
import com.rfid.def.ApiErrDefinition;
import com.rfid.def.RfidDef;

import alpha.cyber.intellib.utils.Logger;
import alpha.cyber.intellib.utils.ToastUtils;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.VoicePlayer;
import alpha.cyber.intelmain.base.BaseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class RifIdTestActivity extends BaseActivity implements OnClickListener {

    private Button mRifidOpen = null;
    private Button mRifidClose = null;
    private Button mResetFactory = null;
    private Button mReadWrite = null;
    private Button mStartInventory = null;
    private Button mStopInventory = null;
    private Button mSetInventoryPara = null;
    private Button mClearInventoryRecord = null;

    private static final int INVENTORY_MSG = 1;
    private static final int GETSCANRECORD = 2;
    private static final int INVENTORY_FAIL_MSG = 4;
    private static final int THREAD_END = 3;

    static int INVENTORY_REQUEST_CODE = 1;// requestCode

    private TextView tv_inventoryInfo = null;
    private ListView list_inventory_record = null;// inventory list
    private InventoryAdapter inventoryAdapter = null;
    private List<InventoryReport> inventoryList = new ArrayList<InventoryReport>();

    private boolean bOnlyReadNew = false;
    private long mAntCfg = 0x000000;
    private long mLoopCnt = 0;
    private boolean bMathAFI = false;
    private byte mAFIVal = 0x00;
    private boolean bRealShowTag = false;
    private boolean bBuzzer = true;
    private boolean bUseISO15693 = false;
    private boolean bUseISO14443A = false;

    private Thread m_inventoryThrd = null;// The thread of inventory

    static ADReaderInterface m_reader = new ADReaderInterface();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rifid);
        setupViews();
        inventoryAdapter = new InventoryAdapter(this, inventoryList);
        list_inventory_record.setAdapter(inventoryAdapter);
    }

    private void setupViews() {
        mRifidOpen = (Button) findViewById(R.id.rifid_open);
        mRifidClose = (Button) findViewById(R.id.rifid_close);
        mResetFactory = (Button) findViewById(R.id.reset);
        mReadWrite = (Button) findViewById(R.id.read_write);

        mStartInventory = (Button) findViewById(R.id.btn_startInventory);
        mStopInventory = (Button) findViewById(R.id.btn_stopInventory);
        mClearInventoryRecord = (Button) findViewById(R.id.btn_clearList);
        mSetInventoryPara = (Button) findViewById(R.id.btn_paraInventory);

        list_inventory_record = (ListView) findViewById(R.id.list_inventory_record);
        tv_inventoryInfo = (TextView) findViewById(R.id.tv_inventoryInfo);
        mRifidOpen.setOnClickListener(this);
        mRifidClose.setOnClickListener(this);
        mResetFactory.setOnClickListener(this);
        mStartInventory.setOnClickListener(this);
        mStopInventory.setOnClickListener(this);
        mClearInventoryRecord.setOnClickListener(this);
        mSetInventoryPara.setOnClickListener(this);
        mReadWrite.setOnClickListener(this);

        mRifidOpen.setEnabled(true);
        mRifidClose.setEnabled(false);
        mResetFactory.setEnabled(false);
        mStartInventory.setEnabled(false);
        mStopInventory.setEnabled(false);
        mClearInventoryRecord.setEnabled(false);
        mSetInventoryPara.setEnabled(false);
        mReadWrite.setEnabled(false);

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
            case R.id.rifid_open:
                mRifidOpen.setEnabled(false);
                mRifidClose.setEnabled(true);
                mResetFactory.setEnabled(true);
                mStartInventory.setEnabled(true);
                mStopInventory.setEnabled(true);
                mClearInventoryRecord.setEnabled(true);
                mSetInventoryPara.setEnabled(true);
                mReadWrite.setEnabled(true);
                openDevice();
                break;
            case R.id.rifid_close:
                closeDevice();
                break;
            case R.id.reset:
                resetFactory();
                break;
            case R.id.btn_startInventory:
                mStartInventory.setEnabled(false);
                mStopInventory.setEnabled(true);
                mSetInventoryPara.setEnabled(false);
                mClearInventoryRecord.setEnabled(false);
                inventoryList.clear();
                inventoryAdapter.notifyDataSetChanged();
                tv_inventoryInfo.setText(getString(R.string.tv_inventoryInfo));
                m_inventoryThrd = new Thread(new InventoryThrd());
                m_inventoryThrd.start();
                break;
            case R.id.btn_stopInventory:
                mStartInventory.setEnabled(true);
                mStopInventory.setEnabled(false);
                mSetInventoryPara.setEnabled(true);
                mClearInventoryRecord.setEnabled(true);
                m_reader.RDR_SetCommuImmeTimeout();
                b_inventoryThreadRun = false;
                break;
            case R.id.btn_paraInventory:
                Intent intent = new Intent(this, InventoryParaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("bUseISO15693", this.bUseISO15693);
                bundle.putBoolean("bUseISO14443A", this.bUseISO14443A);
                bundle.putBoolean("OnlyReadNew", this.bOnlyReadNew);
                bundle.putBoolean("MathAFI", this.bMathAFI);
                bundle.putByte("AFI", this.mAFIVal);
                bundle.putBoolean("bBuzzer", this.bBuzzer);
                bundle.putLong("mAntCfg", mAntCfg);
                bundle.putBoolean("bRealShowTag", bRealShowTag);
                intent.putExtras(bundle);
                startActivityForResult(intent, INVENTORY_REQUEST_CODE);
                break;
            case R.id.btn_clearList:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
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
        super.onDestroy();
    }

    private void openDevice() {
        String conStr = "";
        conStr = String.format("RDType=RD5100;CommType=COM;ComPath=/dev/ttyXRM0;Baund=38400;Frame=8E1;Addr=255");
        if (m_reader.RDR_Open(conStr) == ApiErrDefinition.NO_ERROR) {
            ToastUtils.showShortToast("打开设备成功");
        } else{
            ToastUtils.showShortToast("打开设备失败");
        }

    }

    private void resetFactory() {
        int nret = m_reader.RDR_LoadFactoryDefault();
        if (nret == ApiErrDefinition.NO_ERROR) {
            ToastUtils.showShortToast("恢复出厂设置成功");
            return;
        }
        ToastUtils.showShortToast("恢复出厂设置失败");
    }

    private void closeDevice() {
        if (m_inventoryThrd != null && m_inventoryThrd.isAlive()) {
            ToastUtils.showShortToast(getString(R.string.tx_msg_stopInventory_tip));
            return;
        }
        m_reader.RDR_Close();
        mRifidOpen.setEnabled(true);
        mRifidClose.setEnabled(false);
        mResetFactory.setEnabled(false);
        mStartInventory.setEnabled(false);
        mStopInventory.setEnabled(false);
        mClearInventoryRecord.setEnabled(false);
        mSetInventoryPara.setEnabled(false);
        mReadWrite.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode != INVENTORY_REQUEST_CODE) {
            return;
        }
        if (resultCode != InventoryParaActivity.RESULT_OK) {
            ToastUtils.showShortToast("Activity Result Error");
            return;
        }
        Bundle bundle = data.getExtras();
        bUseISO15693 = bundle.getBoolean("bUseISO15693");
        bUseISO14443A = bundle.getBoolean("bUseISO14443A");
        bOnlyReadNew = bundle.getBoolean("OnlyReadNew");
        bMathAFI = bundle.getBoolean("MathAFI");
        mAFIVal = bundle.getByte("AFI");
        bBuzzer = bundle.getBoolean("bBuzzer");
        mAntCfg = bundle.getLong("mAntCfg");
        bRealShowTag = bundle.getBoolean("bRealShowTag");

        saveHistory("AntCfg", mAntCfg);
        saveHistory("bRealShowTag", bRealShowTag);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveHistory(String sKey, String val) {
        @SuppressWarnings("deprecation")
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(sKey, val);
        editor.commit();
    }

    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    private void saveHistory(String sKey, int val) {
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(sKey, val);
        editor.commit();
    }

    private int GetHistoryInt(String sKey) {
        @SuppressWarnings("deprecation")
        @SuppressLint("WorldReadableFiles")
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        return preferences.getInt(sKey, -1);
    }

    private void saveHistory(String sKey, long val) {
        @SuppressWarnings("deprecation")
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(sKey, val);
        editor.commit();
    }

    private void saveHistory(String sKey, boolean val) {
        @SuppressWarnings("deprecation")
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(sKey, val);
        editor.commit();
    }

    private boolean GetHistoryBool(String sKey) {
        @SuppressWarnings("deprecation")
        @SuppressLint("WorldReadableFiles")
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        return preferences.getBoolean(sKey, false);
    }

    private long GetHistoryLong(String sKey) {
        @SuppressWarnings("deprecation")
        @SuppressLint("WorldReadableFiles")
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        return preferences.getLong(sKey, 0);
    }

    private String GetHistoryString(String sKey) {
        @SuppressWarnings("deprecation")
        @SuppressLint("WorldReadableFiles")
        SharedPreferences preferences = this.getSharedPreferences(sKey,
                Context.MODE_WORLD_READABLE);
        return preferences.getString(sKey, "");
    }

    private boolean b_inventoryThreadRun = false;

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

    private static class MyHandler extends Handler {
        private final WeakReference<RifIdTestActivity> mActivity;

        public MyHandler(RifIdTestActivity activity) {
            mActivity = new WeakReference<RifIdTestActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RifIdTestActivity pt = mActivity.get();
            if (pt == null) {
                return;
            }
            boolean b_find = false;
            switch (msg.what) {
                case INVENTORY_MSG:
                    // if (pt.bBuzzer)
                    // {
                    // VoicePlayer.GetInst(pt).Play();
                    // }

                    @SuppressWarnings("unchecked")
                    Vector<Object> tagList = (Vector<Object>) msg.obj;
                    if (pt.bRealShowTag && !pt.inventoryList.isEmpty()) {
                        pt.inventoryList.clear();
                    }
                    if (!tagList.isEmpty() && pt.bBuzzer) {
                        VoicePlayer.GetInst(pt).Play();
                    }
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
                    pt.tv_inventoryInfo.setText(pt
                            .getString(R.string.tx_info_tagCnt)
                            + pt.inventoryList.size()
                            + pt.getString(R.string.tx_info_loopCnt) + pt.mLoopCnt
                            + pt.getString(R.string.tx_info_failCnt) + msg.arg1);
                    pt.inventoryAdapter.notifyDataSetChanged();
                    break;
                case INVENTORY_FAIL_MSG:
                    pt.tv_inventoryInfo.setText(pt
                            .getString(R.string.tx_info_tagCnt)
                            + pt.inventoryList.size()
                            + pt.getString(R.string.tx_info_loopCnt) + pt.mLoopCnt
                            + pt.getString(R.string.tx_info_failCnt) + msg.arg1);
                    break;
                case THREAD_END:
                    pt.FinishInventory();
                    break;
                default:
                    break;
            }
        }
    }

    private void FinishInventory() {
        mStartInventory.setEnabled(true);
        mStopInventory.setEnabled(false);
        mSetInventoryPara.setEnabled(true);
        mClearInventoryRecord.setEnabled(true);
    }


    static public class InventoryAdapter extends BaseAdapter {
        private List<InventoryReport> list;
        private LayoutInflater inflater;

        public InventoryAdapter(Context context, List<InventoryReport> list) {
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
            InventoryReport inventoryReport = (InventoryReport) this
                    .getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater
                        .inflate(R.layout.inventorylist_tittle, null);
                viewHolder.mTextUid = (TextView) convertView
                        .findViewById(R.id.tv_inventoryUid);
                viewHolder.mTextTagType = (TextView) convertView
                        .findViewById(R.id.tv_inventoryTagType);
                viewHolder.mTextFindCnt = (TextView) convertView
                        .findViewById(R.id.tv_inventoryCnt);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            long mCnt = inventoryReport.getFindCnt();
            String strCnt = mCnt > 0 ? (mCnt + "") : "---";
            viewHolder.mTextUid.setText(inventoryReport.getUidStr());
            viewHolder.mTextTagType.setText(inventoryReport.getTagTypeStr());
            viewHolder.mTextFindCnt.setText(strCnt);

            return convertView;
        }

        private class ViewHolder {
            public TextView mTextUid;
            public TextView mTextTagType;
            public TextView mTextFindCnt;
        }
    }

    public static class InventoryReport {
        private String uidStr;
        private String TagTypeStr;
        private long findCnt = 0;

        public InventoryReport() {
            super();
        }

        public InventoryReport(String uid, String tayType, long cnt) {
            super();
            this.setUidStr(uid);
            this.setTagTypeStr(tayType);
            this.setFindCnt(cnt);
        }

        public String getUidStr() {
            return uidStr;
        }

        public void setUidStr(String uidStr) {
            this.uidStr = uidStr;
        }

        public String getTagTypeStr() {
            return TagTypeStr;
        }

        public void setTagTypeStr(String tagTypeStr) {
            TagTypeStr = tagTypeStr;
        }

        public long getFindCnt() {
            return findCnt;
        }

        public void setFindCnt(long findCnt) {
            this.findCnt = findCnt;
        }
    }

}
