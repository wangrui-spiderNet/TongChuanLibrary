package alpha.cyber.intelmain.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by wangrui on 2018/2/22.
 */
@DatabaseTable(tableName = "inventory_table")
public class InventoryReport {

    @DatabaseField(generatedId = true)
    private long uid;
    @DatabaseField
    private String uidStr;
    @DatabaseField
    private String TagTypeStr;
    @DatabaseField
    private int boxid;
    @DatabaseField
    private String bookcode;

    public InventoryReport() {
        super();
    }

    public InventoryReport(String uid, String tayType, int boxid) {
        super();
        this.setUidStr(uid);
        this.setTagTypeStr(tayType);
        this.setBoxid(boxid);
    }

    public String getBookcode() {
        return bookcode;
    }

    public void setBookcode(String bookcode) {
        this.bookcode = bookcode;
    }

    public int getBoxid() {
        return boxid;
    }

    public void setBoxid(int boxid) {
        this.boxid = boxid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUidStr() {
        return uidStr;
    }

    public void setUidStr(String uidStr) {
        this.uidStr = uidStr;
    }

    public String getTagTypeStr() {
        return TagTypeStr;
    }

    public void setTagTypeStr(String tagTypeStr) {
        TagTypeStr = tagTypeStr;
    }

    @Override
    public String toString() {
        return "InventoryReport{" +
                "uid=" + uid +
                ", uidStr='" + uidStr + '\'' +
                ", TagTypeStr='" + TagTypeStr + '\'' +
                ", boxid=" + boxid +
                '}';
    }
}
