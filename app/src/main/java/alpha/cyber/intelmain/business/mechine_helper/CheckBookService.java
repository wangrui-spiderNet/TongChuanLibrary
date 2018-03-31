package alpha.cyber.intelmain.business.mechine_helper;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.db.InventoryReportDao;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/26.
 */

public class CheckBookService extends Service {

    public List<InventoryReport> allBoxInventoryList;
    private BookDao bookDao;
    private InventoryReportDao reportDao;
    private CheckBookHelper helper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Handler mHandler = new MyHandler(this);

    @Override
    public void onCreate() {
        super.onCreate();
        bookDao = new BookDao(this);
        reportDao = new InventoryReportDao(this);
        allBoxInventoryList = new ArrayList<InventoryReport>();
        helper = new CheckBookHelper(mHandler);
        helper.openDevice();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int openid= AppSharedPreference.getInstance().getOpenBoxId();
        helper.startInventoryOneBox((byte) openid);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.destroyService();
    }

    private class MyHandler extends Handler {
        private final WeakReference<CheckBookService> mService;

        public MyHandler(CheckBookService activity) {
            mService = new WeakReference<CheckBookService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CheckBookService pt = mService.get();
            if (pt == null) {
                return;
            }

            switch (msg.what) {
                case CheckBookHelper.INVENTORY_ALL_BOX:

                    pt.allBoxInventoryList = helper.getInventoryList(msg);
                    Log.e(Constant.TAG, "全柜盘点到的书：" + pt.allBoxInventoryList.toString());

                    helper.stopLoop();

                    break;
                case CheckBookHelper.INVENTORY_FAIL_MSG:
                    Log.e(Constant.TAG, "》》》》》盘点失败》》》》》");
                    helper.stopLoop();
                    break;
                case CheckBookHelper.THREAD_END:
                    Log.e(Constant.TAG, "全柜盘点结束");
                    helper.stopLoop();

                    if (null != allBoxInventoryList && allBoxInventoryList.size() > 0) {
                        getAllBookInfo();
                    }
                    helper.destroyService();
                    break;

                case CheckBookHelper.INVENTORY_SINGLE_BOX:

                    List<InventoryReport> inventoryList = helper.getInventoryList(msg);
                    int address = msg.arg1;
                    saveSingleBoxReportInfo(address, inventoryList);

                    Log.e(Constant.TAG, "第" + address + "个箱子里有：" + inventoryList.size() + "本书");

                    helper.startInventoryAllBoxes();
                    break;
                default:
                    break;
            }
        }
    }

    private void getAllBookInfo() {

        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < allBoxInventoryList.size(); i++) {
            String uid = allBoxInventoryList.get(i).getUidStr();
            String bookCode = helper.getBookCode(0, uid);
            sb.append(bookCode.substring(6,14));
            sb.append("]]");
        }

        AppSharedPreference.getInstance().saveBoxBookCodes(sb.toString());

    }

    private void saveSingleBoxReportInfo(int boxid, List<InventoryReport> reportList) {

        for (int i = 0; i < reportList.size(); i++) {

            String uid = reportList.get(i).getUidStr();
            String bookCode = helper.getBookCode(0, uid);
            Log.e(Constant.TAG, "UID:" + uid + "盘点出来的书码：" + bookCode);
            reportList.get(i).setBoxid(boxid);
            reportList.get(i).setBookcode(bookCode.substring(6, 14));
            reportDao.insertBook(reportList.get(i));
        }

    }

    private void clearBookTable() {
        bookDao.deleteAll();
    }

}
