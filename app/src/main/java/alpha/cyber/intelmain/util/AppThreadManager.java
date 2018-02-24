package alpha.cyber.intelmain.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 业务处理管理类，线程池处理所有请求、耗时操作
 * TODO 类描述
 * <p/>
 * 创建时间: 2014年10月22日 上午11:32:26 <br/>
 *
 * @version 
 * @since v0.0.1
 */
public class AppThreadManager {

	private static volatile AppThreadManager instance = null;
	// 线程池
	private ExecutorService pool;

	// private static final int POOL_SIZE = 5;

	public AppThreadManager() {
		pool = new ThreadPoolExecutor(3, 10, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
	}

	public static AppThreadManager getInstance() {
		if (instance == null) {
			synchronized (AppThreadManager.class) {
				if (instance == null) {
					instance = new AppThreadManager();
				}
			}
		}
		return instance;
	}

	public void start(Runnable runnable) {
		if (runnable != null) {
			pool.execute(runnable);
		}
	}

	public Future<?> submitTask(Runnable task) {
		if (task != null) {
			return pool.submit(task);
		}

		return null;
	}

	public void stop() {
		if (instance == null) {
			return;
		} else {
			synchronized (AppThreadManager.class) {
				if (instance.pool != null) {
					instance.pool.shutdown();// 关闭线程池
					instance = null;
				}
			}
		}
	}
	
	/**
	 * 移除当前runnalbe
	 * @param run
	 */
	public void removeRunnable(Runnable run){
		
	}
	
	
}
