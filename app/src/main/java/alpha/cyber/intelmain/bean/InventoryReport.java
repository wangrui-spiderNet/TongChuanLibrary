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
    private long findCnt = 0;

    public InventoryReport() {
        super();
    }

    public InventoryReport(String uid, String tayType, long cnt) {
        super();
        this.setUidStr(uid);
        this.setTagTypeStr(tayType);
        this.setFindCnt(cnt);
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

    public long getFindCnt() {
        return findCnt;
    }

    public void setFindCnt(long findCnt) {
        this.findCnt = findCnt;
    }

    @Override
    public String toString() {
        return "InventoryReport{" +
                "uidStr='" + uidStr + '\'' +
                ", TagTypeStr='" + TagTypeStr + '\'' +
                ", findCnt=" + findCnt +
                '}';
    }
}
