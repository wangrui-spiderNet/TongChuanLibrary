package alpha.cyber.intelmain.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.BorrowBookBean;

/**
 * Created by wangrui on 2018/2/12.
 */

public class HoldBookDao {

    private Context context;
    private DatabaseHelper dbHelper;
    private static Dao<BorrowBookBean, Integer> bookDao;

    public HoldBookDao(Context context) {
        this.context = context;
        try {
            dbHelper = DatabaseHelper.getHelper(context);
            bookDao = dbHelper.getDao(BookInfoBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean insertBook(BorrowBookBean bookInfoBean) {

        try {
            initDao();
            Dao.CreateOrUpdateStatus status= bookDao.createOrUpdate(bookInfoBean);

            if(status.isCreated()||status.isUpdated()){
                return true;
            }
            return false;

        } catch (SQLException e) {

        }

        return false;
    }

    private void initDao() throws SQLException {
        if (null == bookDao) {
            bookDao = dbHelper.getDao(BookInfoBean.class);
        }
    }

    public List<BorrowBookBean> queryAllBooks(){
        List<BorrowBookBean> allBooks =null;

        try {
            initDao();
            allBooks =bookDao.queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return allBooks;
    }

    public int deleteBookInfo(BorrowBookBean bookInfoBean){
        try {
            initDao();
            return bookDao.delete(bookInfoBean);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public void deleteAll(){
        try {
            initDao();
            bookDao.deleteBuilder().delete();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
