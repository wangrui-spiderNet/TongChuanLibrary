package alpha.cyber.intelmain.threadpool;
/**
 * 
 * @Title: IThreadRequest.java 
 * @Package com.hwp.frameworklibrary.threadpool   
 * @date 2015年8月4日 下午10:34:03 
 * @version V1.0  
 * @author hwp
 * @Description: threadrequest
 */
public interface IThreadRequest {

	void setRequest();
	
	IThreadRequest getRequest();
	
	Thread getThread();
}
