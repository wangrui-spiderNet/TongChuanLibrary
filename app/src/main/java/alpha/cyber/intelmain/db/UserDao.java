package alpha.cyber.intelmain.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.bean.UserInfoBean;

/**
 * Created by wangrui on 2018/3/8.
 */

public class UserDao {

    private DatabaseHelper dbHelper;
    private static Dao<UserInfoBean, Integer> userDao;

    public UserDao () {
        try {
            dbHelper = DatabaseHelper.getHelper(MyApplication.getAppContext());
            userDao = dbHelper.getDao(UserInfoBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean insertBook(UserInfoBean userInfoBean) {

        try {
            initDao();
            Dao.CreateOrUpdateStatus status= userDao.createOrUpdate(userInfoBean);

            if(status.isCreated()||status.isUpdated()){
                return true;
            }
            return false;

        } catch (SQLException e) {

        }

        return false;
    }

    private void initDao() throws SQLException {
        if (null == userDao) {
            userDao = dbHelper.getDao(UserInfoBean.class);
        }
    }

    public List<UserInfoBean> queryAllBooks(){
        List<UserInfoBean> allBooks =null;

        try {
            initDao();
            allBooks = userDao.queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return allBooks;
    }

    public int deleteBookInfo(UserInfoBean userInfoBean){
        try {
            initDao();
            return userDao.delete(userInfoBean);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public void deleteAll(){
        try {
            initDao();
            userDao.deleteBuilder().delete();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
