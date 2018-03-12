package alpha.cyber.intelmain.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wangrui on 2018/2/11.
 */
@DatabaseTable(tableName = "book_table")
public class BookInfoBean {

    @DatabaseField (generatedId = true)
    private long bookid;
    @DatabaseField
    private String bookname;
    @DatabaseField
    private String bookcode;
    @DatabaseField
    private String endtime;
    @DatabaseField
    private String borrowtime;
    @DatabaseField
    private String latedays;

    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

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

    @Override
    public String toString() {
        return "BookInfoBean{" +
                "bookname='" + bookname + '\'' +
                ", bookcode='" + bookcode + '\'' +
                ", endtime='" + endtime + '\'' +
                ", borrowtime='" + borrowtime + '\'' +
                ", latedays='" + latedays + '\'' +
                '}';
    }
}
