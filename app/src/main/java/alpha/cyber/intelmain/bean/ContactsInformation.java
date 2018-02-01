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


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 通讯录人员信息
 *
 * @author zhangxingsheng
 * @ClassName ContactsList
 * @date 2016年07月05日10:33:26
 */

@DatabaseTable(tableName = "contacts_information")
public class ContactsInformation implements Serializable {
    private static final long serialVersionUID = 1L;
    @DatabaseField(generatedId = true)
    private int informationId;
    @DatabaseField
    private long id;//联系人ID  ==userId
    @DatabaseField
    private String name;//联系人名字
    @DatabaseField
    private String groupIds;//对应的groupId,多个分组用逗号隔开
    @DatabaseField
    private int type;//联系人用户类型
    @DatabaseField
    private String avatar;//联系人大图地址
    @DatabaseField
    private String avatarThumb;//联系人小图地址
    @DatabaseField
    private String phone;//联系人手机号码
    @DatabaseField
    private String relation;//关系，教师查看家长账号时显示
    @DatabaseField
    private String stuNumber;//学号，教师查看家长账号时显示
    @DatabaseField
    private String stuName;//学生姓名，教师查看家长账号时显示
    @DatabaseField
    private String remarks;//联系人备注，如果用户类型为公众号时，展示公众号简介
    @DatabaseField
    private int xxtEnable;// 1/0	,是否开通校讯通短信
    @DatabaseField
    private String signature;//签名
    @DatabaseField
    private int following;// 1为关注；0为未关注（可忽略）
    @DatabaseField
    private String className;//当前联系人所在的班级
    @DatabaseField
    private String ctd;//ctd地址，如果当前用户支持ctd则显示地址
    @DatabaseField
    private int studentId; //学生ID
    @DatabaseField
    private String shortPhone;//虚拟网短号
    @DatabaseField
    private String contactSort;//通讯录排序
    @DatabaseField
    private String jid;
    @DatabaseField(foreign = true, dataType = DataType.SERIALIZABLE)
    private ContactsBean informationBean;

    private String defineid;//分组唯一标示

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDefineid() {
        return defineid;
    }

    public void setDefineid(String defineid) {
        this.defineid = defineid;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    private boolean isChecked;//不入库,选中状态

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    private int  groupPosition;//不入库

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    private int  childPosition;//不入库

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public int getInformationId() {
        return informationId;
    }

    public void setInformationId(int informationId) {
        this.informationId = informationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAvatarThumb() {
        return avatarThumb;
    }

    public void setAvatarThumb(String avatarThumb) {
        this.avatarThumb = avatarThumb;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getXxtEnable() {
        return xxtEnable;
    }

    public void setXxtEnable(int xxtEnable) {
        this.xxtEnable = xxtEnable;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCtd() {
        return ctd;
    }

    public void setCtd(String ctd) {
        this.ctd = ctd;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getShortPhone() {
        return shortPhone;
    }

    public void setShortPhone(String shortPhone) {
        this.shortPhone = shortPhone;
    }

    public ContactsBean getInformationBean() {
        return informationBean;
    }

    public void setInformationBean(ContactsBean informationBean) {
        this.informationBean = informationBean;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContactSort() {
        return contactSort;
    }

    public void setContactSort(String contactSort) {
        this.contactSort = contactSort;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ContactsInformation{" +
                "informationId=" + informationId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", groupIds='" + groupIds + '\'' +
                ", type=" + type +
                ", avatar='" + avatar + '\'' +
                ", avatarThumb='" + avatarThumb + '\'' +
                ", phone='" + phone + '\'' +
                ", relation='" + relation + '\'' +
                ", stuNumber='" + stuNumber + '\'' +
                ", stuName='" + stuName + '\'' +
                ", remarks='" + remarks + '\'' +
                ", xxtEnable=" + xxtEnable +
                ", signature='" + signature + '\'' +
                ", following=" + following +
                ", className='" + className + '\'' +
                ", ctd='" + ctd + '\'' +
                ", studentId=" + studentId +
                ", shortPhone='" + shortPhone + '\'' +
                ", contactSort='" + contactSort + '\'' +
                ", jid='" + jid + '\'' +
                ", informationBean=" + informationBean +
                ", defineid='" + defineid + '\'' +
                ", isChecked=" + isChecked +
                ", groupPosition=" + groupPosition +
                ", childPosition=" + childPosition +
                '}';
    }
}
