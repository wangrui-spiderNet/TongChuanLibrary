package alpha.cyber.intelmain.db;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import alpha.cyber.intelmain.bean.ContactsBean;
import alpha.cyber.intelmain.bean.ContactsGroups;
import alpha.cyber.intelmain.bean.ContactsInformation;

public class ContactsDao {
    private Context context;
    private String TAG="ContactsDao";
    private static Dao<ContactsBean, Integer> contactsBeanDao;// 通讯录，存的信息很少，只存了基本字段，id,userid, dt
    private static Dao<ContactsGroups, Integer> contactsGroupDao;//群组
    private static Dao<ContactsInformation, Integer> contactsInformationsDao;//通讯录人员信息列表
    private static DatabaseHelper dbHelper;

    public ContactsDao(Context context) {
        this.context = context;
        try {
            dbHelper = DatabaseHelper.getHelper(context);
            contactsBeanDao = dbHelper.getDao(ContactsBean.class);
            contactsGroupDao = dbHelper.getDao(ContactsGroups.class);
            contactsInformationsDao = dbHelper.getDao(ContactsInformation.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入联系人信息
     *
     * @param contactsBean
     */
    public void insertContactsInfo(final ContactsBean contactsBean) {
        if (null == contactsBean) {
            Log.i(TAG, "contactsBean值为空！");
            return;
        }
        try {
            this.deleteContactsGroup();
            this.deleteContactsInformations();
            this.deleteContactsBean();

            long isInsertSuccess = contactsBeanDao.create(contactsBean);
            if (isInsertSuccess >= 1) {
                Log.i(TAG, "插入成功了！");
            } else {
                Log.i(TAG, "插入失败了！");
            }

            // 通讯录群组消息处理
            Iterator<ContactsGroups> groupsList = null != contactsBean.getGroups() ? contactsBean.getGroups().iterator() : null;
            ContactsGroups contactsGroups;
            HashMap<String, List<ContactsGroups>> map = new HashMap<String, List<ContactsGroups>>();
            String type = "";
            List<ContactsGroups> listGroupsTemp;
            if (null != groupsList) {
                while (groupsList.hasNext()) {
                    contactsGroups = groupsList.next();
                    if (contactsGroups.getBelong().equals("0")) {
                        continue;
                    }
                    contactsGroups.setGroupsBean(contactsBean);
                    type = String.valueOf(contactsGroups.getType());
                    /**
                     * belong=0表示此组属于一级分组，没有所从属的组；belong!=0表示此组为二级分组，
                     * 有从属的父级组，belong的值为父级组的id。比如，组“我的同事”id=1，则“我的同事”的二级分组的belong值就为1.
                     */
                    if (!"0".equals(contactsGroups.getBelong()) || contactsGroups.getBelong() != null) {
                        if (map.containsKey(type)) {
                            map.get(type).add(contactsGroups);
                        } else {
                            listGroupsTemp = new ArrayList<ContactsGroups>();
                            listGroupsTemp.add(contactsGroups);
                            map.put(type, listGroupsTemp);
                        }
                    }

                    contactsGroupDao.create(contactsGroups);
                }
            }

            // 批量安插数据
            TransactionManager.callInTransaction(dbHelper.getConnectionSource(), new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    contactsInformationsDao.setObjectCache(true);
                    Iterator<ContactsInformation> infromationList = null != contactsBean.getContacts() ? contactsBean.getContacts().iterator() : null;
                    ContactsInformation mcontactsInformation;
                    if (infromationList != null) {
                        while (infromationList.hasNext()) {
                            mcontactsInformation = infromationList.next();
                            if (mcontactsInformation.getType() == 20) {
                                Log.d("班级组", JSON.toJSONString(mcontactsInformation));
                            }
                            if (mcontactsInformation != null && mcontactsInformation.getName() != null && !mcontactsInformation.getName().equals("null") && !mcontactsInformation.getName().equals("")) {
                                mcontactsInformation.setInformationBean(contactsBean);
                                contactsInformationsDao.create(mcontactsInformation);
                            }
                        }
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除联系人信息
     *
     * @return
     */
    private boolean deleteContactsGroup() {
        try {
            int groupDeleteTag = this.contactsGroupDao.deleteBuilder().delete();
            if (groupDeleteTag != -1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * deleteContactsInformations
     *
     * @return
     */
    private boolean deleteContactsInformations() {
        int contactsInfoDeleteTag = 0;
        try {
            contactsInfoDeleteTag = contactsInformationsDao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (contactsInfoDeleteTag != -1) {
            return true;
        }
        return false;
    }

    /**
     * deleteContactsBean
     *
     * @return
     */
    private boolean deleteContactsBean() {
        int contactsBeanDeleteTag = 0;
        try {
            contactsBeanDeleteTag = this.contactsBeanDao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (contactsBeanDeleteTag != -1) {
            return true;
        }
        return false;
    }

    /**
     * 根据群组类型查询
     *
     * @param type
     * @return
     */
    public List<ContactsGroups> queryGroups(int type) {
        List<ContactsGroups> listGroups = null;
        try {
            if (contactsGroupDao == null || contactsInformationsDao == null) {
                contactsGroupDao = dbHelper.getDao(ContactsGroups.class);
                contactsInformationsDao = dbHelper.getDao(ContactsInformation.class);
            }
            QueryBuilder<ContactsGroups, Integer> queryBuilder = contactsGroupDao.queryBuilder();
            listGroups = queryBuilder.where().eq("type", type).and().ne("count",0).query();
//            listGroups = queryBuilder.where().eq("type", type).query();

            List<ContactsGroups> listGrouptemp = null;
            List<ContactsInformation> listT = null;
            listGrouptemp = listGroups;
            // 二级分组数据
            List<ContactsGroups> listGrouptemp2 = new ArrayList<ContactsGroups>();
            if (null != listGrouptemp && listGrouptemp.size() > 0) {
                for (ContactsGroups contactsGroups : listGrouptemp) {
                    // belong=0表示此组属于一级分组，没有所从属的组；belong!=0表示此组为二级分组，
                    if (!contactsGroups.getBelong().equals("0")) {
                        listGrouptemp2.add(contactsGroups);
                    }
                }
            }
            listGroups = listGrouptemp2;
            if (null != listGroups && listGroups.size() > 0) {
                for (ContactsGroups contactsGroups : listGroups) {
                    QueryBuilder<ContactsInformation, Integer> builder = contactsInformationsDao.queryBuilder();
                    Where<ContactsInformation, Integer> where = builder.where();
                    where.or(
                            where.like("groupIds", "%," + contactsGroups.getId() + ",%"),
                            where.like("groupIds", contactsGroups.getId() + ",%"),
                            where.like("groupIds", "%," + contactsGroups.getId()),
                            where.eq("groupIds", contactsGroups.getId()));
                    builder.orderBy("contactSort", true);
                    listT = builder.query();
                    contactsGroups.setContactsGroupsList(listT);
                }
            }
            return listGroups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询通讯录
     * 如果数据库没有通讯录返回NULL 请求注意空指针
     *
     * @return
     */
    public ContactsBean queryContacts() {
        ContactsBean bean = null;
        try {
            if (null == contactsBeanDao) {
                contactsBeanDao = dbHelper.getDao(ContactsBean.class);
            }
            List<ContactsBean> listBean = contactsBeanDao.queryForAll();
            if (listBean != null && listBean.size() > 0) {
                bean = listBean.get(listBean.size() - 1);
            }
            listBean.clear();
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据关键字搜索
     *
     * @param keys
     * @return
     */
    public List<ContactsInformation> searchContactsByKeys(String keys) {
        try {
            if (contactsInformationsDao == null) {
                contactsInformationsDao = dbHelper.getDao(ContactsInformation.class);
            }
            QueryBuilder<ContactsInformation, Integer> builder = contactsInformationsDao.queryBuilder();
            Where<ContactsInformation, Integer> where = builder.where();
            where.or(
                    where.like("name", "%" + keys + "%"),
                    where.like("phone", "%" + keys + "%")
            );
            builder.orderBy("contactSort", true);
            List<ContactsInformation> listT = builder.query();
            return listT;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据关键字查询群组信息
     *
     * @param keys
     * @return
     */
    public List<ContactsGroups> searchGroupByKeys(String keys) {
        List<ContactsGroups> listGroups = null;
        try {
            if (contactsGroupDao == null) {
                contactsGroupDao = dbHelper.getDao(ContactsGroups.class);
            }
            QueryBuilder<ContactsGroups, Integer> build = contactsGroupDao.queryBuilder();
            Where<ContactsGroups, Integer> where = build.where();
            where.like("name", "%" + keys + "%");
            where.and();
            where.eq("type", 20);
            listGroups = build.query();
            return listGroups;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /*
     * 查看用户通讯录里面所有班级
	 */
    public List<ContactsGroups> querygroup3() {

        try {
            if (contactsGroupDao == null) {
                contactsGroupDao = dbHelper.getDao(ContactsGroups.class);
            }

            QueryBuilder<ContactsGroups, Integer> build = contactsGroupDao.queryBuilder();
            Where<ContactsGroups, Integer> where = build.where();
            where.in("type", 20, 21);
            List<ContactsGroups> listT = build.query();
            return listT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<ContactsGroups>();
    }

    public ContactsInformation queryOneInfromationNew(String id, String type) {
        try {
            if (contactsInformationsDao == null) {
                contactsInformationsDao = dbHelper.getDao(ContactsInformation.class);
            }

            QueryBuilder<ContactsInformation, Integer> build = contactsInformationsDao.queryBuilder();
            Where<ContactsInformation, Integer> where = build.where();
            where.and(where.eq("id", id), where.eq("type", type));
            List<ContactsInformation> listT = build.query();
            if (null != listT && listT.size() > 0) {
                return listT.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有教师
     *
     * @param type
     * @return
     */
    public List<ContactsInformation> queryInfromationList(int type) {
        try {
            if (contactsInformationsDao == null) {
                contactsInformationsDao = dbHelper.getDao(ContactsInformation.class);
            }

            QueryBuilder<ContactsInformation, Integer> build = contactsInformationsDao.queryBuilder();
            Where<ContactsInformation, Integer> where = build.where();
            where.eq("type", type);
            List<ContactsInformation> listT = build.query();
            if (null != listT && listT.size() > 0) {
                return listT;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有成员
     *
     * @return
     */
    public List<ContactsInformation> queryInfromationListByGroupid(String groupid) {
        List<ContactsInformation> listT = new ArrayList<>();
        try {
            QueryBuilder<ContactsInformation, Integer> builder = contactsInformationsDao.queryBuilder();
            Where<ContactsInformation, Integer> where = builder.where();
            where.or(
                    where.like("groupIds", "%," + groupid + ",%"),
                    where.like("groupIds", groupid + ",%"),
                    where.like("groupIds", "%," + groupid),
                    where.eq("groupIds", groupid));
            listT = builder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listT;
    }

    /*
         * 获取单条通讯录记录
         */
    public ContactsInformation queryOneInfromation(String id) {
        try {
            if (contactsInformationsDao == null) {
                contactsInformationsDao = dbHelper.getDao(ContactsInformation.class);
            }

            QueryBuilder<ContactsInformation, Integer> build = contactsInformationsDao.queryBuilder();
            Where<ContactsInformation, Integer> where = build.where();
            where.eq("id", id);
            List<ContactsInformation> listT = build.query();

            if (null != listT && listT.size() > 0) {
                return listT.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据群ID查询
     *
     * @param id
     * @param
     * @return ContactsGroups
     * @throws
     * @Title: queryContactsGroupsById
     */
    public ContactsGroups queryContactsGroupsById(String id) {
        try {
            HashMap<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("id", id);
            List<ContactsGroups> detailsList = contactsGroupDao.queryForFieldValuesArgs(fieldValues);
            if (detailsList == null || detailsList.size() == 0) {
                return null;
            } else {
                return detailsList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
