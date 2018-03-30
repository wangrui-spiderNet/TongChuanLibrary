package alpha.cyber.intelmain.business.borrowbook;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.base.AppException;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.business.mechine_helper.CheckBookHelper;
import alpha.cyber.intelmain.http.DefaultSubscriber;
import alpha.cyber.intelmain.http.model.EmptyResponse;
import alpha.cyber.intelmain.http.model.Request;
import alpha.cyber.intelmain.http.utils.RetrofitUtils;
import alpha.cyber.intelmain.util.AppSharedPreference;
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

    /**
     * 借书
     * @param item_ids
     */
    public void checkOutBook(String item_ids){
        borrowBookModule.checkOutBook(new Request.Builder()
                .withParam("item_ids",item_ids)
                .withParam("patron_id", AppSharedPreference.getInstance().getUserInfo().getPatron_identifier())
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<CheckoutListBean>>() {
                    @Override
                    public void onSuccess(List<CheckoutListBean> response) {

                        bookView.checkOutBookSuccess(response);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

//                        AppException.handleException(context, errorCode, errorMessage);
                        bookView.checkOutFail(errorCode,errorMessage);
                    }
                });
    }

    /**
     * 超出借阅的接口
     * @param patron_id
     * @param item_ids
     */
    public void overCheckout(String patron_id,String item_ids){
        borrowBookModule.overCheckOut(new Request.Builder().withParam("",item_ids).withParam("",patron_id).build()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<EmptyResponse>() {
                    @Override
                    public void onSuccess(EmptyResponse response) {

                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        AppException.handleException(context, errorCode, errorMessage);
                    }
                });
    }

    /**
     * 还书
     * @param item_ids
     */
    public void checkInBook(String item_ids){
        borrowBookModule.checkInBook(new Request.Builder()
                .withParam("item_ids",item_ids)
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<CheckoutListBean>>() {
                    @Override
                    public void onSuccess(List<CheckoutListBean> response) {

                        bookView.checkInBookSuccess(response);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(context, errorCode, errorMessage);
                        bookView.checkInFial(errorCode,errorMessage);
                    }
                });
    }

    public List<String > requestBookCodes(List<String> reportList) {

        List<String > bookcodes=new ArrayList<>();
        for (int i = 0; i < reportList.size(); i++) {

            String bookCode =  bookHelper.getBookCode(0, reportList.get(i));
            bookCode = bookCode.substring(6, 14);
            bookcodes.add(bookCode);
        }

        return bookcodes;
    }


}
