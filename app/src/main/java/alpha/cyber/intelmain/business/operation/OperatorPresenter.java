package alpha.cyber.intelmain.business.operation;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.http.socket.SocketConstants;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/7.
 */

public class OperatorPresenter {
    private Context context;
    private IUserView userView;

    public OperatorPresenter(Context context,IUserView userView) {
        this.context = context;
        this.userView = userView;
    }

    public void getUserInfo(final String request) {

        new MyAsyncTask(new MyAsyncTask.OnSocketRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {

                userView.getUserInfo(parseUserInfo(result));

            }

            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onFinish() {

            }
        }).execute(request);

    }

    private UserInfoBean parseUserInfo(String result) {
        if (null != result) {
            String[] results = result.split("\\|");

            UserInfoBean infoBean=new UserInfoBean();
            List<String > bookcodes=new ArrayList<String>();
            for (int i = 0; i < results.length; i++) {
                Log.e(Constant.TAG, results[i]);

                String temp=results[i];

                if(temp.startsWith(SocketConstants.personal_name_ae)){
                    infoBean.setName(temp.substring(SocketConstants.personal_name_ae.length()));
                }else if(temp.startsWith(SocketConstants.hold_items_limit_bz)){
                    infoBean.setMaxcount(Integer.parseInt(temp.substring(SocketConstants.hold_items_limit_bz.length())));
                }else if(temp.startsWith(SocketConstants.patron_identifier_aa)){
                    infoBean.setCardnum(temp.substring(SocketConstants.patron_identifier_aa.length()));
                }else if(temp.startsWith(SocketConstants.hold_items_as)){
                    bookcodes.add(temp.substring(SocketConstants.hold_items_as.length()));
                }

            }

            if(bookcodes.size()>0){
                infoBean.setBorrowcount(bookcodes.size());
                infoBean.setBookcodes(bookcodes);

            }else {
                infoBean.setBorrowcount(0);
            }

            return infoBean;

        }

        return null;
    }

    public void getUserState(String request) {
        new MyAsyncTask(new MyAsyncTask.OnSocketRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {
                parseUserInfo(result);
            }

            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onFinish() {

            }
        }).execute(request);
    }

    public void getBookInfo(String request) {
        new MyAsyncTask(new MyAsyncTask.OnSocketRequestListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {

                BookInfoBean infoBean = parseBooks(result);

                userView.getBorrowedBookInfo(parseBooks(result));
            }

            @Override
            public void onFail(String errorMessage) {

            }

            @Override
            public void onFinish() {

            }
        }).execute(request);
    }

    private BookInfoBean parseBooks(String result) {
        if (null != result) {
            String[] results = result.split("\\|");

            BookInfoBean infoBean=new BookInfoBean();
            for (int i = 0; i < results.length; i++) {
                Log.e(Constant.TAG, results[i]);

                String temp=results[i];

                if(temp.startsWith(SocketConstants.title_identifier_aj)){
                    infoBean.setBookname(temp.substring(SocketConstants.title_identifier_aj.length()));
                }else if(temp.startsWith(SocketConstants.hold_items_limit_bz)){
                    infoBean.setBookcode(temp.substring(SocketConstants.item_identifier_ab.length()));
                }else if(temp.startsWith(SocketConstants.due_date_ah)){
                    infoBean.setEndtime(temp.substring(SocketConstants.due_date_ah.length()));
                }else if(temp.startsWith(SocketConstants.hold_pickup_date_cm)){
                    infoBean.setBorrowtime(temp.substring(SocketConstants.hold_pickup_date_cm.length()));
                }else if(temp.startsWith(SocketConstants.over_time_re)){
                    infoBean.setLatedays(temp.substring(SocketConstants.over_time_re.length()));
                }

            }

            return infoBean;
        }

        return null;
    }
}
