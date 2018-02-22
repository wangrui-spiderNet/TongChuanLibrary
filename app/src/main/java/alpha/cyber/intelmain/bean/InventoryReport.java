package alpha.cyber.intelmain.bean;

/**
 * Created by wangrui on 2018/2/22.
 */

public class InventoryReport {

    private String uidStr;
    private String TagTypeStr;
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
