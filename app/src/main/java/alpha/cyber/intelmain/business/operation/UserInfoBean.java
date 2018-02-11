package alpha.cyber.intelmain.business.operation;

import java.util.List;

/**
 * Created by wangrui on 2018/2/11.
 */

public class UserInfoBean {
    private String name;
    private String cardnum;
    private String maxcount;
    private String borrowcount;
    private List<String > bookcodes;

    public List<String> getBookcodes() {
        return bookcodes;
    }

    public void setBookcodes(List<String> bookcodes) {
        this.bookcodes = bookcodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(String maxcount) {
        this.maxcount = maxcount;
    }

    public String getBorrowcount() {
        return borrowcount;
    }

    public void setBorrowcount(String borrowcount) {
        this.borrowcount = borrowcount;
    }
}
