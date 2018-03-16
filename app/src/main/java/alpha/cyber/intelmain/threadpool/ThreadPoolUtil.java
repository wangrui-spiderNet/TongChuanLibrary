package alpha.cyber.intelmain.threadpool;

import android.os.AsyncTask;

/**
 * 
 * @Title: ThreadPoolUtil.java 
 * @Package com.hwp.frameworklibrary.threadpool   
 * @date 2015年8月4日 下午3:54:04 
 * @version V1.0  
 * @author hwp
 * @Description: 线程管理工具
 */
public class ThreadPoolUtil implements IThreadProvider{

	
	private static ThreadPoolUtil mInstance = null;
	
	public static ThreadPoolUtil getInstance(){
		if(mInstance == null){
			mInstance = new ThreadPoolUtil();
		}
		return mInstance;
	}

	@Override
	public void addRequest(IThreadRequest request) {
		request.setRequest();
		Thread thread = request.getThread();
		ThreadPoolManager.getInstance().execute(thread);
	}

	@Override
	public void addRequest(IThreadRequest request, int priority) {
		request.setRequest();
		Thread thread = request.getThread();
		thread.setPriority(priority);
		ThreadPoolManager.getInstance().execute(thread);
	}

	@Override
	public void addRequest(Runnable runnable) {
		ThreadPoolManager.getInstance().execute(runnable);
	}
    
	@Override
	public void addRequest(Runnable runnable, int priority) {
		Thread thread = new Thread(runnable);
		thread.setPriority(priority);
		ThreadPoolManager.getInstance().execute(thread);
	}

	@Override
	public void addRequest(BaseAsyncTask task) {
		if(task == null) return;
		boolean isHasRunnable = false;
		do {
			if(task.getTastRunnable() != null){
				isHasRunnable = true;
			}
		} while (isHasRunnable != true);
		ThreadPoolManager.getInstance().execute(task);
	}

	@Override
	public void addRequest(AsyncTask<Object, Object, Object> task, int priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancle(){
		ThreadPoolManager.getInstance().shutDown();
	}

	@Override
	public void stopThread() {
		ThreadPoolManager.getInstance().shutDownNow();
	}


	@Override
	public void pause() {
		ThreadPoolManager.getInstance().pause();
	}


	@Override
	public void resume() {
		ThreadPoolManager.getInstance().resume();
	}
	
	
}
