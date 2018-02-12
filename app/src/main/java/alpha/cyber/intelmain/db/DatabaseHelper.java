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
import alpha.cyber.intelmain.bean.ContactsBean;
import alpha.cyber.intelmain.bean.ContactsGroups;
import alpha.cyber.intelmain.bean.ContactsInformation;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "cicada_pos.db";

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
//            TableUtils.createTable(connectionSource, ContactsBean.class);
//            TableUtils.createTable(connectionSource, ContactsGroups.class);
//            TableUtils.createTable(connectionSource, ContactsInformation.class);
            TableUtils.createTable(connectionSource, BookInfoBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ContactsBean.class, true);
            TableUtils.dropTable(connectionSource, ContactsGroups.class, true);
            TableUtils.dropTable(connectionSource, ContactsInformation.class, true);
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
                if (instance == null){
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
