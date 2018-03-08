package alpha.cyber.intelmain.bean;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wangrui on 2018/3/8.
 */
@DatabaseTable(tableName = "box_book_table")
public class CheckoutListBean {
    /**
     * item_properties : 62.9/62.9
     * permanent_location : 905
     * overdue_days : 0
     * hold_pickup_date : 2018-03-07
     * item_author : (缇�)甯冭幈鎭┞烽樋鐟�(W. Brian Arthur)钁梶AK978-7-213-05998-8
     * callno : N0/3
     * patron_identifier : 6101008880085324
     * due_date : 2018-04-06
     * title_identifier : 鎶�鏈殑鏈川锛氭妧鏈槸浠�涔堬紝瀹冩槸濡備綍杩涘寲鐨勶細what it is and how it evolves
     * screen_message :
     * item_identifier : 00834503
     * institution_id : XAPL
     * Page_num : 17,259椤祙KP
     * media_type : 001
     * reservation : 0
     * publisher : 娴欐睙浜烘皯鍑虹増绀緗SJ鎶�鏈
     * circulation_type : 999_CN01]]涓枃鍥句功
     * current_location : 905
     * Curlib : XAPL
     */
    @DatabaseField(generatedId = true)
    private long bookid;
    @DatabaseField
    private String item_properties;
    @DatabaseField
    private String permanent_location;
    @DatabaseField
    private String overdue_days;
    @DatabaseField
    private String hold_pickup_date;
    @DatabaseField
    private String item_author;
    @DatabaseField
    private String callno;
    @DatabaseField
    private String patron_identifier;
    @DatabaseField
    private String due_date;
    @DatabaseField
    private String title_identifier;
    @DatabaseField
    private String screen_message;
    @DatabaseField
    private String item_identifier;
    @DatabaseField
    private String institution_id;
    @DatabaseField
    private String Page_num;
    @DatabaseField
    private String media_type;
    @DatabaseField
    private String reservation;
    @DatabaseField
    private String publisher;
    @DatabaseField
    private String circulation_type;
    @DatabaseField
    private String current_location;
    @DatabaseField
    private String Curlib;

    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public static CheckoutListBean objectFromData(String str) {

        return new Gson().fromJson(str, CheckoutListBean.class);
    }

    public String getItem_properties() {
        return item_properties;
    }

    public void setItem_properties(String item_properties) {
        this.item_properties = item_properties;
    }

    public String getPermanent_location() {
        return permanent_location;
    }

    public void setPermanent_location(String permanent_location) {
        this.permanent_location = permanent_location;
    }

    public String getOverdue_days() {
        return overdue_days;
    }

    public void setOverdue_days(String overdue_days) {
        this.overdue_days = overdue_days;
    }

    public String getHold_pickup_date() {
        return hold_pickup_date;
    }

    public void setHold_pickup_date(String hold_pickup_date) {
        this.hold_pickup_date = hold_pickup_date;
    }

    public String getItem_author() {
        return item_author;
    }

    public void setItem_author(String item_author) {
        this.item_author = item_author;
    }

    public String getCallno() {
        return callno;
    }

    public void setCallno(String callno) {
        this.callno = callno;
    }

    public String getPatron_identifier() {
        return patron_identifier;
    }

    public void setPatron_identifier(String patron_identifier) {
        this.patron_identifier = patron_identifier;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getTitle_identifier() {
        return title_identifier;
    }

    public void setTitle_identifier(String title_identifier) {
        this.title_identifier = title_identifier;
    }

    public String getScreen_message() {
        return screen_message;
    }

    public void setScreen_message(String screen_message) {
        this.screen_message = screen_message;
    }

    public String getItem_identifier() {
        return item_identifier;
    }

    public void setItem_identifier(String item_identifier) {
        this.item_identifier = item_identifier;
    }

    public String getInstitution_id() {
        return institution_id;
    }

    public void setInstitution_id(String institution_id) {
        this.institution_id = institution_id;
    }

    public String getPage_num() {
        return Page_num;
    }

    public void setPage_num(String Page_num) {
        this.Page_num = Page_num;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCirculation_type() {
        return circulation_type;
    }

    public void setCirculation_type(String circulation_type) {
        this.circulation_type = circulation_type;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    public String getCurlib() {
        return Curlib;
    }

    public void setCurlib(String Curlib) {
        this.Curlib = Curlib;
    }
}
