package alpha.cyber.intelmain.business.home;

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
import alpha.cyber.intelmain.business.mechine_helper.CheckBookHelper;
import alpha.cyber.intelmain.db.InventoryReportDao;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.ToastUtil;

/**
 * Created by wangrui on 2018/2/26.
 */

public class CheckBookService extends Service implements CheckCallBack {

    public List<InventoryReport> inventoryList;
    private BookDao bookDao;
    private InventoryReportDao reportDao;
    private CheckBoxPresenter presenter;

    private CheckBookHelper helper;
    private boolean deviceOpen = false;

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
        inventoryList = new ArrayList<InventoryReport>();
        helper = new CheckBookHelper(mHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        deviceOpen = helper.openDevice();

        Log.e(Constant.TAG,"设备打开:"+deviceOpen);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.destroyService();
    }

    @Override
    public void getBookInfoByCode(CheckoutListBean checkoutListBean) {
        bookDao.insertBook(checkoutListBean);

        Log.e(Constant.TAG,"盘点书柜中的书："+checkoutListBean.toString());

        ToastUtil.showToast("盘点书柜里面的书："+checkoutListBean.getTitle_identifier());
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

                    pt.inventoryList = helper.getInventoryList(msg);

                    Log.e(Constant.TAG, pt.inventoryList.toString());

                    if(null!=inventoryList&&inventoryList.size()>0){

                        reportDao.deleteAll();

                        for(int i=0;i<inventoryList.size();i++){
                            reportDao.insertBook(inventoryList.get(i));
                        }

                        clearBookTable();

                        requestBookInfo();
                    }

                    if(helper.getmLoopCnt()>0){
                        helper.stopLoop();
                    }

                    break;
                case CheckBookHelper.INVENTORY_FAIL_MSG:

                    Log.e(Constant.TAG, "》》》》》盘点失败》》》》》");
                    ToastUtil.showToast("盘点失败");

                    break;
                case CheckBookHelper.THREAD_END:

                    break;
                default:
                    break;
            }
        }
    }

    private void requestBookInfo() {

        for (int i = 0; i < inventoryList.size(); i++) {

            String bookCode = helper.getBookCode(i, inventoryList.get(i).getUidStr());
            presenter.getBookInfoByCode(bookCode);

        }
    }

    private void clearBookTable() {
        bookDao.deleteAll();
    }

}
