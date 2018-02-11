package alpha.cyber.intelmain.bean;

/**
 * Created by wangrui on 2018/2/11.
 */

public class BookInfoBean {
    private String bookname;
    private String bookcode;
    private String endtime;
    private String borrowtime;
    private String latedays;

    public String getLatedays() {
        return latedays;
    }

    public void setLatedays(String latedays) {
        this.latedays = latedays;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookcode() {
        return bookcode;
    }

    public void setBookcode(String bookcode) {
        this.bookcode = bookcode;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getBorrowtime() {
        return borrowtime;
    }

    public void setBorrowtime(String borrowtime) {
        this.borrowtime = borrowtime;
    }
}
