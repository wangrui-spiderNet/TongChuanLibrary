package alpha.cyber.intelmain.business.mechine_helper;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.rfid.api.GFunction;
import com.rfid.def.RfidDef;

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

        if(null!=checkoutListBean){
            Log.e(Constant.TAG,"保存到书柜："+bookDao.insertBook(checkoutListBean));
            Log.e(Constant.TAG,"盘点书柜中的书："+checkoutListBean.toString());
        }else{
            ToastUtil.showToast(this,"没有找到书");
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

                    if(helper.getmLoopCnt()>0){
                        helper.stopLoop();
                    }

                    pt.inventoryList = helper.getInventoryList(msg);

                    Log.e(Constant.TAG,"盘点到的书："+pt.inventoryList.toString());

                    if(null!=inventoryList&&inventoryList.size()>0){
                        reportDao.deleteAll();

                        for(int i=0;i<inventoryList.size();i++){
                            reportDao.insertBook(inventoryList.get(i));
                        }

                        clearBookTable();

                        requestBookInfo();
                    }

                    break;
                case CheckBookHelper.INVENTORY_FAIL_MSG:

                    Log.e(Constant.TAG, "》》》》》盘点失败》》》》》");
//                    ToastUtil.showToast("盘点失败");
                    break;
                case CheckBookHelper.THREAD_END:

                    break;
                default:
                    break;
            }
        }
    }

    private void requestBookInfo() {

        ArrayList<String > bookcodes=new ArrayList<String>();

        for (int i = 0; i < inventoryList.size(); i++) {
            String uid=inventoryList.get(i).getUidStr();
            String bookCode = helper.getBookCode(0, uid);
            bookcodes.add(bookCode);
            Log.e(Constant.TAG,"UID:"+uid+"盘点出来的书码："+bookCode);
            presenter.getBookInfoByCode(bookCode.substring(6,14));
        }

    }

    private void clearBookTable() {
        bookDao.deleteAll();
    }

}
