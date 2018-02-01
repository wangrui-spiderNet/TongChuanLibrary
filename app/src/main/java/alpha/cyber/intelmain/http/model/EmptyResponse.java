package alpha.cyber.intelmain.http.model;

import com.google.gson.Gson;

/**
 * TODO
 * <p>
 * Create time: 2017/3/9 16:25
 *
 * @author liuyun.
 */
public class EmptyResponse {

    /**
     * msg : 请求成功
     * rtnCode : 0000000
     * ts : 1497523303197
     */

    private String msg;
    private String rtnCode;
    private long ts;

    public static EmptyResponse objectFromData(String str) {

        return new Gson().fromJson(str, EmptyResponse.class);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
