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
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.db.InventoryReportDao;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.ToastUtil;

/**
 * Created by wangrui on 2018/2/26.
 */

public class CheckBookService extends Service implements CheckCallBack {

    public List<InventoryReport> allBoxInventoryList;
    private BookDao bookDao;
    private InventoryReportDao reportDao;
    private CheckBoxPresenter presenter;

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
        presenter = new CheckBoxPresenter(this, this);
        allBoxInventoryList = new ArrayList<InventoryReport>();
        helper = new CheckBookHelper(mHandler);
        helper.openDevice();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        helper.startInventoryAllBoxes();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.destroyService();
    }

    @Override
    public void getBookInfoByCode(CheckoutListBean checkoutListBean,String uid) {

        if (null != checkoutListBean) {
            checkoutListBean.setUid(uid);
            Log.e(Constant.TAG, "盘点书柜中的书：" + checkoutListBean.toString());
            Log.e(Constant.TAG, "保存到书柜：" + bookDao.insertBook(checkoutListBean));

        } else {
            ToastUtil.showToast(this, "没有找到书");
        }
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
                case CheckBookHelper.INVENTORY_MSG:

                    if (helper.getmLoopCnt() > 0) {
                        helper.stopLoop();
                    }
                    pt.allBoxInventoryList = helper.getInventoryList(msg);
                    Log.e(Constant.TAG, "盘点到的书：" + pt.allBoxInventoryList.toString());

                    break;
                case CheckBookHelper.INVENTORY_FAIL_MSG:
                    Log.e(Constant.TAG, "》》》》》盘点失败》》》》》");

                    break;
                case CheckBookHelper.THREAD_END:
                    Log.e(Constant.TAG, "盘点结束");

                    if (null != allBoxInventoryList && allBoxInventoryList.size() > 0) {
                        reportDao.deleteAll();

                        for (int i = 0; i < allBoxInventoryList.size(); i++) {
                            reportDao.insertBook(allBoxInventoryList.get(i));
                        }

                        clearBookTable();

                        requestBookInfo();
                    }

                    break;

                case CheckBookHelper.INVENTORY_SINGLE_BOX:

                    List<InventoryReport> inventoryList = helper.getInventoryList(msg);
                    int address = msg.arg1;
                    for (int i = 0; i < inventoryList.size(); i++) {
                        inventoryList.get(i).setBoxid(address);
                        reportDao.insertBook(inventoryList.get(i));
                    }

//                    boxInventoryList.add(address,inventoryList);
                    Log.e(Constant.TAG, "第" + address + "个箱子里有：" + inventoryList.size() + "本书");

                    break;
                default:
                    break;
            }
        }
    }

    private void requestBookInfo() {

        ArrayList<String> bookcodes = new ArrayList<String>();

        for (int i = 0; i < allBoxInventoryList.size(); i++) {
            String uid = allBoxInventoryList.get(i).getUidStr();
            String bookCode = helper.getBookCode(0, uid);
            bookcodes.add(bookCode);
            Log.e(Constant.TAG, "UID:" + uid + "盘点出来的书码：" + bookCode);
            presenter.getBookInfoByCode(bookCode.substring(6, 14),uid);
        }

    }

    private void clearBookTable() {
        bookDao.deleteAll();
    }

}
