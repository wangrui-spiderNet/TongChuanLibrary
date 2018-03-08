package alpha.cyber.intelmain.bean;

import com.google.gson.Gson;
import java.util.List;

/**
 * Created by wangrui on 2018/3/8.
 */

public class UserBorrowInfo {

    /**
     * global_lib_rest : 0000
     * print_line :
     * fee : 0.0
     * patron_identifier : 6101008880085324
     * lib_hold : 0001
     * hold_items : ["00834503"]
     * personal_name : 闊╂棴杈墊CF0.0
     * loanedvalue : 62.9
     * screen_message :
     * depositrate : 5.0
     * hold_items_limit : 0004
     * checkoutList : [{"item_properties":"62.9/62.9","permanent_location":"905","overdue_days":0,"hold_pickup_date":"2018-03-07","item_author":"(缇�)甯冭幈鎭┞烽樋鐟�(W. Brian Arthur)钁梶AK978-7-213-05998-8","callno":"N0/3","patron_identifier":"6101008880085324","due_date":"2018-04-06","title_identifier":"鎶�鏈殑鏈川锛氭妧鏈槸浠�涔堬紝瀹冩槸濡備綍杩涘寲鐨勶細what it is and how it evolves","screen_message":"","item_identifier":"00834503","institution_id":"XAPL","Page_num":"17,259椤祙KP","media_type":"001","reservation":"0","publisher":"娴欐睙浜烘皯鍑虹増绀緗SJ鎶�鏈","circulation_type":"999_CN01]]涓枃鍥句功","current_location":"905","Curlib":"XAPL"}]
     * prepay : 0.0
     * fee_amount : 100.0
     * global_lib_hold : 0000
     * enddate : 2043-12-24
     * lib_rest : 0004
     * readertype : 002
     */

    private String global_lib_rest;
    private String print_line;
    private double fee;
    private String patron_identifier;
    private String lib_hold;
    private String personal_name;
    private String loanedvalue;
    private String screen_message;
    private String depositrate;
    private String hold_items_limit;
    private String prepay;
    private String fee_amount;
    private String global_lib_hold;
    private String enddate;
    private String lib_rest;
    private String readertype;
    private List<String> hold_items;
    private List<CheckoutListBean> checkoutList;
    private List<String> warning;

    public static UserBorrowInfo objectFromData(String str) {

        return new Gson().fromJson(str, UserBorrowInfo.class);
    }

    public String getGlobal_lib_rest() {
        return global_lib_rest;
    }

    public void setGlobal_lib_rest(String global_lib_rest) {
        this.global_lib_rest = global_lib_rest;
    }

    public String getPrint_line() {
        return print_line;
    }

    public void setPrint_line(String print_line) {
        this.print_line = print_line;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getPatron_identifier() {
        return patron_identifier;
    }

    public void setPatron_identifier(String patron_identifier) {
        this.patron_identifier = patron_identifier;
    }

    public String getLib_hold() {
        return lib_hold;
    }

    public void setLib_hold(String lib_hold) {
        this.lib_hold = lib_hold;
    }

    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }

    public String getLoanedvalue() {
        return loanedvalue;
    }

    public void setLoanedvalue(String loanedvalue) {
        this.loanedvalue = loanedvalue;
    }

    public String getScreen_message() {
        return screen_message;
    }

    public void setScreen_message(String screen_message) {
        this.screen_message = screen_message;
    }

    public String getDepositrate() {
        return depositrate;
    }

    public void setDepositrate(String depositrate) {
        this.depositrate = depositrate;
    }

    public String getHold_items_limit() {
        return hold_items_limit;
    }

    public void setHold_items_limit(String hold_items_limit) {
        this.hold_items_limit = hold_items_limit;
    }

    public String getPrepay() {
        return prepay;
    }

    public void setPrepay(String prepay) {
        this.prepay = prepay;
    }

    public String getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(String fee_amount) {
        this.fee_amount = fee_amount;
    }

    public String getGlobal_lib_hold() {
        return global_lib_hold;
    }

    public void setGlobal_lib_hold(String global_lib_hold) {
        this.global_lib_hold = global_lib_hold;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getLib_rest() {
        return lib_rest;
    }

    public void setLib_rest(String lib_rest) {
        this.lib_rest = lib_rest;
    }

    public String getReadertype() {
        return readertype;
    }

    public void setReadertype(String readertype) {
        this.readertype = readertype;
    }

    public List<String> getHold_items() {
        return hold_items;
    }

    public void setHold_items(List<String> hold_items) {
        this.hold_items = hold_items;
    }

    public List<CheckoutListBean> getCheckoutList() {
        return checkoutList;
    }

    public void setCheckoutList(List<CheckoutListBean> checkoutList) {
        this.checkoutList = checkoutList;
    }

    public List<String> getWarning() {
        return warning;
    }

    public void setWarning(List<String> warning) {
        this.warning = warning;
    }
}
