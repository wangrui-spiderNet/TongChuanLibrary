package alpha.cyber.intelmain.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @Title: ThreadPoolPause.java 
 * @Package com.hwp.frameworklibrary.threadpool   
 * @date 2015年8月4日 下午10:08:22 
 * @version V1.0  
 * @author hwp
 * @Description: 可暂停执行的线程池
 */
public class ThreadPoolPause extends ThreadPoolExecutor{

	private boolean isPaused;
	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unpaused = pauseLock.newCondition();
	
	
	/**
	 * 
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 */
	public ThreadPoolPause(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	@Override
	protected void beforeExecute(Thread t,Runnable r){
		super.beforeExecute(t, r);
		pauseLock.lock();
		try {
			while (isPaused) {
				unpaused.await();
			}
		} catch (Exception e) {
			t.interrupt();
		} finally{
			pauseLock.unlock();
		}
	}
	/**
	 * 暂定线程池中的执行
	 */
	public void pause(){
		pauseLock.lock();
		try {
			isPaused = true;
		} catch (Exception e) {
		} finally{
			pauseLock.unlock();
		}
	}
	/**
	 * 回复线程池中的操作
	 */
	public void resume(){
		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		} catch (Exception e) {
		} finally{
			pauseLock.unlock();
		}
	}

}
