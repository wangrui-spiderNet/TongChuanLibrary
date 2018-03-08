package alpha.cyber.intelmain.bean;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wangrui on 2018/2/11.
 */
@DatabaseTable(tableName = "user_info_table")
public class UserInfoBean {

    /**
     * personal_birthday : 1992-09-29
     * print_line : 鏌ヨ璇昏�呬俊鎭垚鍔�!
     * patron_identifier : 6101008880085324
     * remark :
     * personal_name : 闊╂棴杈墊AMXAPL
     * screen_message : 鏌ヨ璇昏�呬俊鎭垚鍔�!
     * startdate : 男
     * Email_address :
     * prepay : 0.0
     * ID_number : 610424199209292671
     * personal_address : 闄曡タ鐪佷咕鍘垮ぇ鏉ㄤ埂涓贩鏉戜腑涓滅粍
     * fee_amount : 100.0
     * patron_identifier_startdate : 2016-08-08
     * enddate : 2043-12-24
     * personal_place :
     * readertype : 002
     * phone_number :
     * personal_nation :
     * mobile_number : 13299065916
     */
    @DatabaseField(generatedId = true)
    private long userid;
    @DatabaseField
    private String personal_birthday;
    @DatabaseField
    private String print_line;
    @DatabaseField
    private String patron_identifier;
    @DatabaseField
    private String remark;
    @DatabaseField
    private String personal_name;
    @DatabaseField
    private String screen_message;
    @DatabaseField
    private String startdate;
    @DatabaseField
    private String Email_address;
    @DatabaseField
    private String prepay;
    @DatabaseField
    private String ID_number;
    @DatabaseField
    private String personal_address;
    @DatabaseField
    private String fee_amount;
    @DatabaseField
    private String patron_identifier_startdate;
    @DatabaseField
    private String enddate;
    @DatabaseField
    private String personal_place;
    @DatabaseField
    private String readertype;
    @DatabaseField
    private String phone_number;
    @DatabaseField
    private String personal_nation;
    @DatabaseField
    private String mobile_number;

    public static UserInfoBean objectFromData(String str) {

        return new Gson().fromJson(str, UserInfoBean.class);
    }

    public String getPersonal_birthday() {
        return personal_birthday;
    }

    public void setPersonal_birthday(String personal_birthday) {
        this.personal_birthday = personal_birthday;
    }

    public String getPrint_line() {
        return print_line;
    }

    public void setPrint_line(String print_line) {
        this.print_line = print_line;
    }

    public String getPatron_identifier() {
        return patron_identifier;
    }

    public void setPatron_identifier(String patron_identifier) {
        this.patron_identifier = patron_identifier;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }

    public String getScreen_message() {
        return screen_message;
    }

    public void setScreen_message(String screen_message) {
        this.screen_message = screen_message;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEmail_address() {
        return Email_address;
    }

    public void setEmail_address(String Email_address) {
        this.Email_address = Email_address;
    }

    public String getPrepay() {
        return prepay;
    }

    public void setPrepay(String prepay) {
        this.prepay = prepay;
    }

    public String getID_number() {
        return ID_number;
    }

    public void setID_number(String ID_number) {
        this.ID_number = ID_number;
    }

    public String getPersonal_address() {
        return personal_address;
    }

    public void setPersonal_address(String personal_address) {
        this.personal_address = personal_address;
    }

    public String getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(String fee_amount) {
        this.fee_amount = fee_amount;
    }

    public String getPatron_identifier_startdate() {
        return patron_identifier_startdate;
    }

    public void setPatron_identifier_startdate(String patron_identifier_startdate) {
        this.patron_identifier_startdate = patron_identifier_startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getPersonal_place() {
        return personal_place;
    }

    public void setPersonal_place(String personal_place) {
        this.personal_place = personal_place;
    }

    public String getReadertype() {
        return readertype;
    }

    public void setReadertype(String readertype) {
        this.readertype = readertype;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPersonal_nation() {
        return personal_nation;
    }

    public void setPersonal_nation(String personal_nation) {
        this.personal_nation = personal_nation;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "personal_birthday='" + personal_birthday + '\'' +
                ", print_line='" + print_line + '\'' +
                ", patron_identifier='" + patron_identifier + '\'' +
                ", remark='" + remark + '\'' +
                ", personal_name='" + personal_name + '\'' +
                ", screen_message='" + screen_message + '\'' +
                ", startdate='" + startdate + '\'' +
                ", Email_address='" + Email_address + '\'' +
                ", prepay='" + prepay + '\'' +
                ", ID_number='" + ID_number + '\'' +
                ", personal_address='" + personal_address + '\'' +
                ", fee_amount='" + fee_amount + '\'' +
                ", patron_identifier_startdate='" + patron_identifier_startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", personal_place='" + personal_place + '\'' +
                ", readertype='" + readertype + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", personal_nation='" + personal_nation + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                '}';
    }
}
