package alpha.cyber.intelmain.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @Title: ThreadPoolManager.java 
 * @Package com.hwp.frameworklibrary.threadpool   
 * @date 2015年8月4日 下午3:55:01 
 * @version V1.0  
 * @author hwp
 * @Description: 线程管理
 */
public class ThreadPoolManager {
    /** 当前cpu的内核数*/
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	/** 核心线程数*/
	private static int CORE_THREAD_POOL_SIZE = CPU_COUNT + 1;
	/** 线程池最大线程数*/
	private static int MAX_THREAD_POOL_SIZE = CPU_COUNT * 2 + 1;
	/** 额外的线程空状态的生成时间*/
	private static int KEEP_LIVE_THREAD_TIME = 5;
	/** 额外的线程队列，当核心线程被占用，且阻塞队列已满的情况下，额外线程开启*/
	private static BlockingQueue<Runnable> outThreadQueue = new LinkedBlockingDeque<Runnable>(32);
	
	private static ThreadPoolPause threadPool = null;
	private static ThreadPoolManager threadManager = null;
	private List<BaseAsyncTask> listTask = new ArrayList<BaseAsyncTask>();
	private List<Runnable> listRunnable = new ArrayList<Runnable>();
	/**
	 * 线程工厂
	 */
	private static ThreadFactory threadFactory = new ThreadFactory() {
		private final AtomicInteger integer = new AtomicInteger();
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "myThreadPool thread:" + integer.getAndIncrement());
		}
	}; 
	
	public static ThreadPoolManager getInstance(){
		if(threadManager == null){
			threadManager = new ThreadPoolManager();
		}
		return threadManager;
	}
	
	private ThreadPoolManager(){
		getThreadPoolInstance();
	}
	
	public static ThreadPoolPause getThreadPoolInstance(){
		if(threadPool == null){
			threadPool = new ThreadPoolPause(CORE_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE, KEEP_LIVE_THREAD_TIME,
					TimeUnit.SECONDS, outThreadQueue, threadFactory);
			threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		}
		return threadPool;
	}
	/**
	 * 执行线程操作
	 * @param runnable
	 */
	public void execute(Runnable runnable){
		if(threadPool != null){
			listRunnable.add(runnable);
			threadPool.execute(runnable);
		}
	}
	/**
	 * 执行task操作
	 * @param task
	 */
	public void execute(BaseAsyncTask task){
		listTask.add(task);
		execute(task.getTastRunnable());
	}
	
	/**
	 * 立刻关闭线程池操作
	 */
	public void shutDownNow(){
		threadPool.shutdownNow();
		destory();
	}
	/**
	 * 关闭线程池操作
	 */
	public void shutDown(){
		threadPool.shutdown();
		destory();
	}
	/**
	 * 暂定线程操作
	 */
	public void pause(){
		if(listRunnable.size() > 0){
			for(int i=0;i<listRunnable.size();i++){
				Runnable r = listRunnable.get(i);
				synchronized (r) {
					try {
						r.wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		threadPool.pause();
	}
	/**
	 * 回复线程操作
	 */
	public void resume(){
		if(listRunnable.size() > 0){
			for(int i=0;i<listRunnable.size();i++){
				Runnable r = listRunnable.get(i);
				synchronized (this) {
					try {
						r.notify();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		threadPool.resume();
	}
	
	private void destory(){
		if(listTask.size() > 0){
			for(int i=0;i<listTask.size();i++){
				BaseAsyncTask task = listTask.get(i);
				if(task!=null && !task.isCancelled()){
					task.cancel(true);
				}
			}
		}
		threadManager = null;
		threadPool = null;
	}
}
