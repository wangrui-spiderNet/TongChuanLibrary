package alpha.cyber.intelmain.util;

import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.threadpool.ThreadPoolUtil;

/**
 * 日志工具类
 * <p/>
 * 创建时间: 2014-8-19 上午11:58:53 <br/>
 * 
* @author hwp
 * @version
 * @since v0.0.1
 */
public class LogSaveUtils {
	
	private static final boolean DEBUG = MyApplication.isDebug;
	private static final String log_path = "LogFile/";

    private static StringBuffer sb;
	static{
		sb = MyApplication.getInstance().getStringBuffer();
	}

	public static void d(String tag, String message) {
		if (DEBUG){
			Log.d(tag, message);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}

	public static void d(String tag, String message, Throwable tr) {
		if (DEBUG){
			Log.d(tag, message, tr);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}

	public static void i(String tag, String message) {
		if (DEBUG) {
			Log.i(tag, message);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}

	public static void i(String tag, String message, Throwable tr) {
		if (DEBUG) {
			Log.d(tag, message, tr);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}
	public static void w(String tag, String message) {
		if (DEBUG) {
			Log.w(tag, message);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}
	
	public static void w(String tag, String message, Throwable tr) {
		if (DEBUG) {
			Log.w(tag, message, tr);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}

	public static void e(String tag, String message) {
		if (DEBUG) {
			Log.e(tag, message);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}

	public static void e(String tag, String message, Throwable tr) {
		if (DEBUG) {
			Log.d(tag, message, tr);
			if(tag!=null && tag.equals(Constant.TAG)){
				writeLogFile(message);
			}
		}
	}

	/**
	 * http log method
	 */
	public static void http(String className, String message) {
		if (DEBUG) {
			Log.d("httpMessage", className + " : " + message);
		}
	}

	public static String makeLogTag(Class<?> cls) {
		return "Androidpn_" + cls.getSimpleName();
	}
	
	private static void writeLogFile(String logTag){

		String path = FileUtils.getAppRootDir();
	    write(path, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss   ").format(System.currentTimeMillis())+logTag+"\n");
	}
	
	private static void write(final String path,String log){
		try {
			sb.append(log+"\n");
			
			if(sb!=null && sb.toString().length()/1024 >  10 ){
				ThreadPoolUtil.getInstance().addRequest(new Runnable() {
					
					@Override
					public void run() {
						String name = path+log_path+"lib_log_"+new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(System.currentTimeMillis())+".txt";
						FileUtils.createDirAndFile(name);
						FileUtils.writeFileFromString(name, sb.toString());
						sb.setLength(0);
					}
				});
				removeFile(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static synchronized void removeFile(final String path){
		try {
			ThreadPoolUtil.getInstance().addRequest(new Runnable() {
				
				@Override
				public void run() {
					File logFile = new File(path+log_path);
					if(logFile != null && logFile.listFiles()!=null && logFile.listFiles().length > 200){
						File[] files = logFile.listFiles();
						for(int i=0;i<files.length-150;i++){
							FileUtils.deleteFile(files[i]);
						}
						files = null;
					}
					logFile = null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static boolean deleteLogFiles(){
		return FileUtils.deleteDir(new File(FileUtils.getAppRootDir()+log_path));
	}

}