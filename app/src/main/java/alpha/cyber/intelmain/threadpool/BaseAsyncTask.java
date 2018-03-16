package alpha.cyber.intelmain.threadpool;

import android.os.AsyncTask;

/**
 * 
 * @Title: BaseAsyncTask.java 
 * @Package com.hwp.frameworklibrary.threadpool   
 * @date 2015年8月4日 下午11:22:36 
 * @version V1.0  
 * @author hwp
 * @Description: TODO
 */
public abstract class BaseAsyncTask extends AsyncTask<Object, Object, Object>{

	private Runnable runnable;
	private Object result = null;
	private boolean cancaleTask = false;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Object doInBackground(final Object... params) {
		runnable = new Runnable() {
			@Override
			public void run() {
				result = onDoInBackgroud(params);
			}
		};
		return result;
	}
    /**
     * 
     * @return 当前task后台是否取消
     */
	public boolean getTaskCancle(){
		return isCancelled();
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
	}
	/**
	 * 执行task操作后台任务操作执行 
	 * 
	 * 如果你在该方法中存在Thread的调用 eg Thread.sleep(time);
	 * 则需再该方法前执行如下操作
	 * if(getTaskCancle()){
			return null;
		}
		如果有 while（）执行则执行如下操作while(getTaskCancle())
	 * @param params
	 * @return doSomeing
	 */
	public abstract Object onDoInBackgroud(Object... params);
	/**
	 * 
	 * @return 获取当前线程
	 */
	public Runnable getTastRunnable(){
		return runnable;
	}
}
