package alpha.cyber.intelmain.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.UserBorrowInfo;
import alpha.cyber.intelmain.bean.UserInfoBean;

/**
 * Created by huxin on 16/6/6.
 */
public class AppSharedPreference extends AppSharedPreferenceConfig {

    private static AppSharedPreference instance = null;
    private SharedPreferences app = null;
    private SharedPreferences.Editor editor;

    public AppSharedPreference() {
        app = MyApplication.getInstance().getSharedPreferences(NAME_PREFERENCES, Activity.MODE_PRIVATE);
        editor = app.edit();
    }

    public static AppSharedPreference getInstance() {
        if (instance == null) {
            instance = new AppSharedPreference();
        }

        return instance;
    }

    public String getClientXgToken() {
        return app.getString(LOGIN_TOEN, "");
    }

    public boolean setClientXgToken(String string) {
        return editor.putString(LOGIN_TOEN, string).commit();
    }

    public String getUserMac() {
        return app.getString(USER_MAC, "");
    }

    public boolean setUserMac(String mac) {
        return editor.putString(USER_MAC, mac).commit();
    }

    public boolean setLogIn(boolean hasLogoutIm){
        return editor.putBoolean(HAS_LOGOUT_IM, hasLogoutIm).commit();
    }

    public boolean isLogin(){
        return app.getBoolean(HAS_LOGOUT_IM,false);
    }

    public void saveBoxBookCodes(String allbooks) {
        editor.putString(ALL_BOX_BOOKS, allbooks).commit();
    }

    public String getBoxBookCodes() {
        return app.getString(ALL_BOX_BOOKS,"");
    }

    public void clear() {
        editor.clear().commit();
    }

    public void setServerTimeStamp(long serverTs) {
        editor.putLong("server_timestamp", serverTs).commit();
    }

    public long getServerTimeStamp() {
        return app.getLong("server_timestamp", System.currentTimeMillis());
    }

    public void saveAccount(String account_id){
        editor.putString(AppSharedPreferenceConfig.ACCOUNT_ID,account_id).commit();
    }

    public String getAccount(){
        return app.getString(AppSharedPreferenceConfig.ACCOUNT_ID,"");
    }

    public void saveOpenBoxId(int boxid){
        editor.putInt(AppSharedPreferenceConfig.OPEN_BOX_ID,boxid).commit();
    }

    public int getOpenBoxId(){
        return app.getInt(AppSharedPreferenceConfig.OPEN_BOX_ID,0);
    }


    public String getLogo() {
        return app.getString(LOGO_URL, "");
    }

    public boolean saveLogo(String mac) {
        return editor.putString(LOGO_URL, mac).commit();
    }


    /**
     * 保存List
     * @param datalist
     */
    public void saveHoldBookInfos(List<CheckoutListBean> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(AppSharedPreferenceConfig.HOLD_BOOKS, strJson).commit();
    }

    /**
     * 获取List
     * @return
     */
    public List<CheckoutListBean> getHoldBookInfos() {
        List<CheckoutListBean> datalist=new ArrayList<CheckoutListBean>();
        String strJson = app.getString(AppSharedPreferenceConfig.HOLD_BOOKS, null);
        if (null == strJson) {
            return null;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<CheckoutListBean>>() {
        }.getType());

        return datalist;
    }

    /**
     * 保存用户信息
     * @param infoBean
     */
    public void saveUserInfo(UserInfoBean infoBean){
        Gson gson=new Gson();

        String userinfo=gson.toJson(infoBean);

        editor.putString(AppSharedPreferenceConfig.USER_INFO,userinfo).commit();

    }

    /**
     * 获取用户信息
     * @return
     */
    public UserInfoBean getUserInfo(){
        String userJson=app.getString(AppSharedPreferenceConfig.USER_INFO,"");

        if(!StringUtils.isEmpty(userJson)){
            UserInfoBean userInfoBean=new Gson().fromJson(userJson,UserInfoBean.class);
            return userInfoBean;
        }

        return null;
    }

    /**
     * 保存用户借书信息
     * @param infoBean
     */
    public void saveBorrowBookUserInfo(UserBorrowInfo infoBean){
        Gson gson=new Gson();

        String userinfo=gson.toJson(infoBean);

        editor.putString(AppSharedPreferenceConfig.USER_BORROW_BOOK_INFO,userinfo).commit();

    }

    /**
     * 获取用户借书信息
     * @return
     */
    public UserBorrowInfo getBorrowBookUserInfo(){
        String userJson=app.getString(AppSharedPreferenceConfig.USER_BORROW_BOOK_INFO,"");

        if(!StringUtils.isEmpty(userJson)){
            UserBorrowInfo userInfoBean=new Gson().fromJson(userJson,UserBorrowInfo.class);
            return userInfoBean;
        }

        return null;
    }



}
