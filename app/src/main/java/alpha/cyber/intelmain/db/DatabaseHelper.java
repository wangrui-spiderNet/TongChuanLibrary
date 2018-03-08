package alpha.cyber.intelmain.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.BorrowBookBean;
import alpha.cyber.intelmain.bean.CheckoutListBean;
import alpha.cyber.intelmain.bean.ContactsBean;
import alpha.cyber.intelmain.bean.ContactsGroups;
import alpha.cyber.intelmain.bean.ContactsInformation;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.bean.UserInfoBean;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "tongchuan_lib.db";

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, BookInfoBean.class);
            TableUtils.createTable(connectionSource, BorrowBookBean.class);
            TableUtils.createTable(connectionSource, InventoryReport.class);
            TableUtils.createTable(connectionSource, UserInfoBean.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, BorrowBookBean.class, true);
            TableUtils.dropTable(connectionSource, CheckoutListBean.class, true);
            TableUtils.dropTable(connectionSource, InventoryReport.class, true);
            TableUtils.dropTable(connectionSource, UserInfoBean.class, true);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }

            }
        }

        return instance;
    }

    @Override
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
