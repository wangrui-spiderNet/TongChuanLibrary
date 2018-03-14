package alpha.cyber.intelmain.business.borrowbook;

import android.content.Context;

import com.rfid.api.GFunction;
import com.rfid.def.RfidDef;

import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.base.AppException;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.business.mechine_helper.CheckBookHelper;
import alpha.cyber.intelmain.http.DefaultSubscriber;
import alpha.cyber.intelmain.http.model.EmptyResponse;
import alpha.cyber.intelmain.http.model.Request;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.http.utils.RetrofitUtils;
import alpha.cyber.intelmain.util.Log;
import alpha.cyber.intelmain.util.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangrui on 2018/2/12.
 */

public class BorrowBookPresenter {

    private Context context;
    private BorrowBookModule borrowBookModule;
    private CheckBookHelper bookHelper;
    private IBorrowBookView bookView;

    public BorrowBookPresenter(Context context,CheckBookHelper helper,IBorrowBookView bookView){
        this.context = context;
        borrowBookModule = RetrofitUtils.createService(BorrowBookModule.class);
        bookHelper = helper;
        this.bookView = bookView;
    }

    public void checkOutBook(String item_ids){
        borrowBookModule.checkOutBook(new Request.Builder()
                .withParam("item_ids",item_ids)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<EmptyResponse>() {
                    @Override
                    public void onSuccess(EmptyResponse response) {

                        ToastUtil.showToast(context,response.getMsg());
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        AppException.handleException(context, errorCode, errorMessage);
                    }
                });
    }

    public void checkInBook(String item_ids,String patron_id){
        borrowBookModule.checkInBook(new Request.Builder()
                .withParam("item_ids",item_ids)
                .withParam("patron_id",patron_id)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<EmptyResponse>() {
                    @Override
                    public void onSuccess(EmptyResponse response) {

                        ToastUtil.showToast(context,response.getMsg());
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(context, errorCode, errorMessage);
                    }
                });
    }

    public void requestBookInfos(List<String> reportList, int type) {

        for (int i = 0; i < reportList.size(); i++) {

            String bookCode =  bookHelper.getBookCode(i, reportList.get(i));

            bookCode = bookCode.substring(6, 14);

            getBookInfo(bookCode, type, reportList.size());

        }
    }

    public void getBookInfo(String bookCode, final int type, final int count) {

        getBookInfoByCode(type,count,bookCode);

    }

    public void getBookInfoByCode( final int type, final int count,String item_id) {

        Log.e(Constant.TAG,"请求书码："+item_id);

        borrowBookModule.getBookInfoByCode(new Request.Builder()
                .withParam("item_id", item_id)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<CheckoutListBean>() {
                    @Override
                    public void onSuccess(CheckoutListBean response) {
                        if (null != response) {
                            bookView.getBookInfoByCode(type,count,response);
                        } else {
                            ToastUtil.showToast(context, "未查到此书！");
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(context, errorCode, errorMessage);
                        Log.e(Constant.TAG, "错误信息：" + errorCode + errorMessage);
                    }
                });
    }

}
