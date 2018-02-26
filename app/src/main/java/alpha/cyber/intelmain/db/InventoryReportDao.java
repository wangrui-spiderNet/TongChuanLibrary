package alpha.cyber.intelmain.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.BorrowBookBean;
import alpha.cyber.intelmain.bean.InventoryReport;

/**
 * Created by wangrui on 2018/2/12.
 */

public class InventoryReportDao {

    private Context context;
    private DatabaseHelper dbHelper;
    private static Dao<InventoryReport, Integer> inventoryDao;

    public InventoryReportDao(Context context) {
        this.context = context;
        try {
            dbHelper = DatabaseHelper.getHelper(context);
            inventoryDao = dbHelper.getDao(InventoryReport.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertBook(InventoryReport inventoryReport) {

        try {
            initDao();
            Dao.CreateOrUpdateStatus status = inventoryDao.createOrUpdate(inventoryReport);

            if (status.isCreated() || status.isUpdated()) {
                return true;
            }
            return false;

        } catch (SQLException e) {

        }

        return false;
    }

    private void initDao() throws SQLException {
        if (null == inventoryDao) {
            inventoryDao = dbHelper.getDao(BookInfoBean.class);
        }
    }

    public List<InventoryReport> queryAllBooks() {
        List<InventoryReport> allBooks = null;

        try {
            initDao();
            allBooks = inventoryDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allBooks;
    }

    public int deleteBookInfo(InventoryReport bookInfoBean) {
        try {
            initDao();
            return inventoryDao.delete(bookInfoBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void deleteAll() {
        try {
            initDao();
            inventoryDao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
