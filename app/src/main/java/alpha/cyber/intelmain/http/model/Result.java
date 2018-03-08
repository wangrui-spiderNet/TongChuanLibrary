package alpha.cyber.intelmain.http.model;


import java.io.Serializable;

import alpha.cyber.intelmain.http.BaseURL;


/**
 * Created by huxin on 16/6/13.
 */
public class Result<T> implements Serializable {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 服务器当前时间
     */
    private long servertime;
    /**
     * 响应结果code
     */
    private String errcode;
    /**
     * 提示语
     */
    private String errmsg;

    public long getServertime() {
        return servertime;
    }

    public void setServertime(long servertime) {
        this.servertime = servertime;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public boolean isSuccess() {
        if (BaseURL.APP_BUSINESS_SUCCESS.equalsIgnoreCase(this.errcode)) {
            return true;
        } else {
//            if(BaseURL.APP)
            return false;
        }
    }
}
