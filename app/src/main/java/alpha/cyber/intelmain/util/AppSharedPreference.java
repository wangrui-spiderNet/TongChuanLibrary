package alpha.cyber.intelmain.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.MyApplication;

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

    public String getLoginToken() {
        return app.getString(LOGIN_TOEN, "");
    }

    public boolean setLoginToken(String string) {
        return editor.putString(LOGIN_TOEN, string).commit();
    }

    public long getUserId() {
        return app.getLong(USER_ID, 0L);
    }

    public boolean setUserId(long userId) {
        return editor.putLong(USER_ID, userId).commit();
    }

    public String getUserName() {
        return app.getString(USER_NAME, "");
    }

    public boolean setUserName(String userName) {
        return editor.putString(USER_NAME, userName).commit();
    }

    public String getUserIcon() {
        return app.getString(USER_ICON, "");
    }

    public boolean setUserIcon(String userIcon) {
        return editor.putString(USER_ICON, userIcon).commit();
    }

    public String getUserSchoolName() {
        return app.getString(USER_SCHOOL_NAME, "");
    }

    public boolean setUserSchoolName(String userSchoolName) {
        return editor.putString(USER_SCHOOL_NAME, userSchoolName).commit();
    }

    public boolean setUserPhone(String userPhone){
        return editor.putString(USER_PHONE,userPhone).commit();
    }

    public String getUserPhone (){
        return app.getString(USER_PHONE,"");
    }

    public long getUserSchoolId() {
        return app.getLong(USER_SCHOOL_ID, 0);
    }

    public boolean  setUserSchoolId(long schoolid){
        return editor.putLong(USER_SCHOOL_ID, schoolid).commit();
    }

    public boolean setSchoolAddress(String address){
        return editor.putString(SCHOOL_ADDRESS,address).commit();
    }

    public String getSchoolAddress(){
        return app.getString(SCHOOL_ADDRESS,"");
    }

    public String getUserMac() {
        return app.getString(USER_MAC, "");
    }

    public boolean setUserMac(String mac) {
        return editor.putString(USER_MAC, mac).commit();
    }

    public boolean getIsBindOk() {
        return app.getBoolean(IS_BIND_OK, false);
    }

    public boolean setIsBindOk(boolean isLoginOk) {
        return editor.putBoolean(IS_BIND_OK, isLoginOk).commit();
    }


    public boolean setHasLogoutIm(boolean hasLogoutIm){
        return editor.putBoolean(HAS_LOGOUT_IM, hasLogoutIm).commit();
    }

    public boolean hasLogoutIm(){
        return app.getBoolean(HAS_LOGOUT_IM,false);
    }


    //设置是否第一次登陆
    public void setFirstEnter(Boolean isFirstEnter) {
        editor.putBoolean(IS_FIRST_ENTER, isFirstEnter).commit();
    }

    //判断是否第一次登陆
    public boolean getIsFirstEnter() {
        return app.getBoolean(IS_FIRST_ENTER, true);
    }


    public void setFirstActive(boolean isFirstActive) {
        editor.putBoolean(IS_FIRST_ACTIVE, isFirstActive).commit();
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

    public void setStatisticTime(long serverTs) {
        editor.putLong("statistic_time", serverTs).commit();
    }

    public long getStatisticTime() {
        return app.getLong("statistic_time", 0);
    }

    public void setUserType(int type){
        editor.putInt(AppSharedPreferenceConfig.USER_TYPE,type).commit();
    }

    public int getUserType(){
        return app.getInt(AppSharedPreferenceConfig.USER_TYPE,0);
    }

    //1体验包用户，2年费用户，3明星课程用户,
    public void setCustomType(int customType){
        editor.putInt(AppSharedPreferenceConfig.CustomType,customType).commit();
    }

    public int getCustomType(){
        return app.getInt(AppSharedPreferenceConfig.CustomType,0);
    }

    /**
     * 保存List
     * @param datalist
     */
    public void setLogedUserList(String key, List<String> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear().putString(key, strJson).commit();
    }

    /**
     * 获取List
     * @return
     */
    public List<String> getAppVersionList(String key) {
        List<String> datalist=new ArrayList<String>();
        String strJson = app.getString(key, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<String>>() {
        }.getType());

        return datalist;
    }



}
