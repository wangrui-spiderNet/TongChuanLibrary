package alpha.cyber.intelmain.http;

/**
 * Activity页面回调接口实现，主要返回成功和失败的数据信息
 * <p/>
 * 创建时间: 2014年11月5日 上午11:38:25 <br/>
 * 
 * @author hwp
 * @version @param <T>
 * @since v0.0.1
 */
public abstract class RequestHandler<T> {

	// ////////////////////// 以下方法由具体业务选择实现
	// UI Thread onStart
	public  void onStart() {
	}
	// UI Thread onCancelled
	protected void onCancelled() {
	}
	// UI Thread onLoading
	protected void onLoading(long total, long current, boolean isUploading) {
	}
    // request params
	protected void onRequestParamd(Object obj){};
	
	public abstract void onSuccess(T result);

	public abstract void onFailure(String errorCode, String errorMessage);

}
