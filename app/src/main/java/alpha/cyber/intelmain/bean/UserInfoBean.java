package alpha.cyber.intelmain.bean;

import java.util.List;

/**
 * Created by wangrui on 2018/2/11.
 */

public class UserInfoBean {
    private String name;
    private String cardnum;
    private int maxcount;
    private int borrowcount;
    private String permission;
    private List<String > bookcodes;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

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

    public int getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(int maxcount) {
        this.maxcount = maxcount;
    }

    public int getBorrowcount() {
        return borrowcount;
    }

    public void setBorrowcount(int borrowcount) {
        this.borrowcount = borrowcount;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "name='" + name + '\'' +
                ", cardnum='" + cardnum + '\'' +
                ", maxcount=" + maxcount +
                ", borrowcount=" + borrowcount +
                ", permission='" + permission + '\'' +
                ", bookcodes=" + bookcodes +
                '}';
    }
}
