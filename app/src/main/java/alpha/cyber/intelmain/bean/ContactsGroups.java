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
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * 通讯录分组
 */
@DatabaseTable(tableName = "contacts_groups")
public class ContactsGroups implements Serializable {
    private static final long serialVersionUID = 1L;
    @DatabaseField(generatedId = true)
    private int groupsId;//自增Id
    @DatabaseField
    private String id;//分组ID
    @DatabaseField
    private String belong;//从属的分组ID
    @DatabaseField
    private String name;//分组名称
    @DatabaseField
    private String desc;//分组描述
    @DatabaseField
    private int type; //1老师2家长3班级
    @DatabaseField
    private long creater;//群组创建者ID
    @DatabaseField
    private int createrType;//群组创建者类型
    @DatabaseField
    private int count;//群组成员数
    @DatabaseField
    private String thumb;//群组缩略图地址
    @DatabaseField
    private boolean isCheckGroup;//群组成员数
    @DatabaseField
    private long classId;//班级id，type=20的时候使用
    @DatabaseField
    private int status;//是否屏蔽该班消息 0为正常；1为被屏蔽
    @DatabaseField(foreign=true)
    private ContactsBean groupsBean;
    @DatabaseField
    private String jid;


    private boolean isChecked;//不入库,选中状态
//  private boolean isChangeChild;//不入库,选中状态


    private List<ContactsInformation> contactsGroupsList;//当前分组联系人集合

    public ContactsGroups() {
        super();
    }

    public int getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(int groupsId) {
        this.groupsId = groupsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreater() {
        return creater;
    }

    public void setCreater(long creater) {
        this.creater = creater;
    }

    public int getCreaterType() {
        return createrType;
    }

    public void setCreaterType(int createrType) {
        this.createrType = createrType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public boolean isCheckGroup() {
        return isCheckGroup;
    }

    public void setCheckGroup(boolean checkGroup) {
        isCheckGroup = checkGroup;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ContactsInformation> getContactsGroupsList() {
        return contactsGroupsList;
    }

    public void setContactsGroupsList(List<ContactsInformation> contactsGroupsList) {
        this.contactsGroupsList = contactsGroupsList;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public ContactsBean getGroupsBean() {
        return groupsBean;
    }

    public void setGroupsBean(ContactsBean groupsBean) {
        this.groupsBean = groupsBean;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


}
