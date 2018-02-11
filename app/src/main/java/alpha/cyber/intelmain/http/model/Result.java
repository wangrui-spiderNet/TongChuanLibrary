package alpha.cyber.intelmain.http.model;


import java.io.Serializable;


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

    //    /**
//     * 服务器当前时间
//     */
//    private long ts;
//    /**
//     * 响应结果code
//     */
//    private String rtnCode;
//    /**
//     * 提示语
//     */
//    private String msg;
//
//    /**
//     * 业务相关数据
//     */
//    private T bizData;
//
//    /**
//     * rtnCode.
//     *
//     * @return the rtnCode
//     * @since v0.0.1
//     */
//    public String getRtnCode() {
//        return rtnCode;
//    }
//
//    /**
//     * rtnCode.
//     *
//     * @param rtnCode the rtnCode to set
//     * @since v0.0.1
//     */
//    public void setRtnCode(String rtnCode) {
//        this.rtnCode = rtnCode;
//    }
//
//    /**
//     * msg.
//     *
//     * @return the msg
//     * @since v0.0.1
//     */
//    public String getMsg() {
//        return msg;
//    }
//
//    /**
//     * msg.
//     *
//     * @param msg the msg to set
//     * @since v0.0.1
//     */
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    /**
//     * bizData.
//     *
//     * @return the bizData
//     * @since v0.0.1
//     */
//    public T getBizData() {
//        return bizData;
//    }
//
//    /**
//     * bizData.
//     *
//     * @param bizData the bizData to set
//     * @since v0.0.1
//     */
//    public void setBizData(T bizData) {
//        this.bizData = bizData;
//    }
//
//
//    /**
//     * 服务器当前时间
//     *
//     * @return the systenTime
//     * @since v0.0.1
//     */
//    public long getTs() {
//        return ts;
//    }
//
//    /**
//     * 服务器当前时间
//     * <p>
//     * the systenTime to set
//     *
//     * @since v0.0.1
//     */
//    public void setTs(long ts) {
//        this.ts = ts;
//    }
//
//    public boolean isSuccess() {
//        if(BaseURL.APP_BUSINESS_SUCCESS.equalsIgnoreCase(this.rtnCode)){
//            return true;
//        }else {
////            if(BaseURL.APP)
//            return false;
//        }
//    }
}
