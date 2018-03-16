package alpha.cyber.intelmain.threadpool;

import android.os.AsyncTask;

/**
 * 
 * @Title: IThreadProvider.java 
 * @Package com.hwp.frameworklibrary.threadpool   
 * @date 2015年8月4日 下午10:32:17 
 * @version V1.0  
 * @author hwp
 * @Description: 线程池请求接口
 */
public interface IThreadProvider {
    /**
     * 添加请求
     * @param request
     */
	void addRequest(IThreadRequest request);
	/**
	 * 添加优先级请求
	 * @param request
	 * @param priority  
	 *  Thread.NORM_PRIORITY = 5
		Thread.MAX_PRIORITY =10
		Thread.MIN_PRIORITY =1
	 */
	void addRequest(IThreadRequest request, int priority);
    /**
     * 添加thread
     */
	void addRequest(Runnable runnable);
	/**
     * 添加thread优先级请求
     * @param priority
     *  Thread.NORM_PRIORITY = 5
		Thread.MAX_PRIORITY =10
		Thread.MIN_PRIORITY =1
     */
	void addRequest(Runnable runnable, int priority);
	/**
	 * 添加asynctask 进线程队列
	 * @param task
	 */
	void addRequest(BaseAsyncTask task);
	/**
	 * 添加asynctask 优先级进线程队列
	 * @param task
	 *  Thread.NORM_PRIORITY = 5
		Thread.MAX_PRIORITY =10
		Thread.MIN_PRIORITY =1
	 */
	void addRequest(AsyncTask<Object, Object, Object> task, int priority);
	/**
	 * 取消请求
	 */
	void cancle();
	/**
	 * 停止请求
	 */
	void stopThread();
	/**
	 * 暂停请求
	 */
	void pause();
	/**
	 * 恢复请求
	 */
	void resume();
}
