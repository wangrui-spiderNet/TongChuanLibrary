package alpha.cyber.intelmain.http;


import alpha.cyber.intelmain.BuildConfig;

/**
 * Created by huxin on 16/6/12.
 */
public class BaseURL {


    // TODO:服务器错误信息定义
    /**
     * 服务器网络连接(超时)错误
     */
    public static final String APP_EXCEPTION_HTTP_TIMEOUT = "0";
    /**
     * 服务器连接正常200
     */
    public static final String APP_EXCEPTION_HTTP_200 = "200";
    /**
     * 服务器连接错误404（找不到页面错误）
     */
    public static final String APP_EXCEPTION_HTTP_404 = "404";
    /**
     * 服务器连接错误500（内部服务错误）
     */
    public static final String APP_EXCEPTION_HTTP_500 = "500";

    /**
     * 服务器数据解析错误、以及任何未定义的错误
     */
    public static final String APP_EXCEPTION_HTTP_OTHER = "-1";

    /**
     * -1 系统繁忙，请稍后再试
     * 0 请求成功
     * 40001 请求的参数缺少或者错误
     * 40002 设备不可用
     * 40003 未找到该设备关联的机构
     */
    public static final String APP_EXCEPTION_PARAMS_ERROR = "40001";
    public static final String APP_EXCEPTION_DEVICE_ERROR = "40002";
    public static final String APP_EXCEPTION_NO_DEVICE_ERROR = "40003";
    public static final String APP_EXCEPTION_LIB_ERROR = "50001";

    /**
     * 业务成功代码：0000000<br>
     * <br>
     * <a
     * href="http://wiki.thinkjoy.local/pages/viewpage.action?pageId=25264313"
     * >异常代码参考Wiki资料</a><br>
     */
    public static final String APP_BUSINESS_SUCCESS = "0";

    /**
     * 获取当前环境－服务器域名、地址
     */
    public static String getBaseURL() {

        return BuildConfig.API_HOST;
    }

}
