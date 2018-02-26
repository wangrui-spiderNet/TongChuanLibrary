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
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.bean.UserInfoBean;
import alpha.cyber.intelmain.business.borrowbook.CheckBookHelper;
import alpha.cyber.intelmain.business.login.IUserView;
import alpha.cyber.intelmain.business.operation.OperatorPresenter;
import alpha.cyber.intelmain.db.InventoryReportDao;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/26.
 */

public class CheckBookService extends Service implements IUserView {


    public List<InventoryReport> inventoryList;
    public boolean bRealShowTag = false;
    public boolean bBuzzer = true;
    private BookDao bookDao;
    private InventoryReportDao reportDao;
    private OperatorPresenter presenter;

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
        presenter = new OperatorPresenter(this, this);
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
    public void getUserInfo(UserInfoBean userinfoBean) {

    }

    @Override
    public void getAllBoxBooks(BookInfoBean infoBean) {
        bookDao.insertBook(infoBean);
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

                    Log.e(Constant.TAG, "》》》》》失败》》》》》");

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

            String time = DateUtils.getSystemTime();
            String bookinfo_request = getResources().getString(R.string.bookinfo_request);

            bookCode = bookCode.substring(6, 14);

            String bookinfo_format = String.format(bookinfo_request, time, bookCode);

            presenter.getBookInfo(bookinfo_format);

        }
    }

    private void clearBookTable() {
        bookDao.deleteAll();
    }

}
