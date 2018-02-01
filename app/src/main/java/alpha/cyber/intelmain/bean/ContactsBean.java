/**
 * Copyright (C) 2014 Guangzhou QTONE Technologies Ltd.
 * <p>
 * 本代码版权归广州全通教育股份有限公司所有，且受到相关的法律保护。没有经过版权所有者的书面同意，
 * 任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 *
 * @date ${date} ${time}
 * @version V1.0
 */
package alpha.cyber.intelmain.bean;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;

import alpha.cyber.intelmain.http.model.Result;

/**
 * 通讯录 通讯录需要本地化
 * request = { “cmd”:CMD_100901 “type”:1/2 //请求类型，1为离线通讯录，2为群组列表更新 }
 *
 * @author zhangxingsheng
 * @ClassName ContactsBean
 * @date 2016年07月05日10:08:39
 */
@DatabaseTable(tableName = "contacts_bean")
public class ContactsBean extends Result implements Serializable {
    private static final long serialVersionUID = 1L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int userId;
    @ForeignCollectionField
    private Collection<ContactsGroups> groups;//通讯录分组
    @ForeignCollectionField
    private Collection<ContactsInformation> contacts;//通讯录人员信息列表

    public ContactsBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<ContactsGroups> getGroups() {
        return groups;
    }

    public void setGroups(Collection<ContactsGroups> groups) {
        this.groups = groups;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Collection<ContactsInformation> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<ContactsInformation> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "ContactsBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", groups=" + groups +
                ", contacts=" + contacts +
                '}';
    }
}
