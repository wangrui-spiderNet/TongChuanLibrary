package alpha.cyber.intelmain.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class RoleSerializableUtil {
	/**
	 * hexiaodong
	 * 将当前角色信息写到本地文件
	 * @param context
	 * @param loginBean
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
//	public static void serializePerson(Context context , LoginBean loginBean) throws FileNotFoundException, IOException {
//		// ObjectOutputStream  对象输出流，将Person对象存储到E盘的Person.txt文件中，完成对Person对象的序列化操作
//		File dir = new File(context.getFilesDir().getAbsolutePath(), context.getPackageName());
//		if (!dir.exists()) {
//			dir.mkdirs();
//		}
//		File file = new File(dir, "user.txt");
//		ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));
//		oo.writeObject(loginBean);
//		oo.close();
//	}
//
//	/**
//	 * 从本地文件读取当前角色信息
//	 * @param context
//	 * @return
//	 * @throws Exception
//	 * @throws IOException
//	 */
//	public static LoginBean deserializePerson(Context context) throws Exception, IOException {
//		File dir = new File(context.getFilesDir().getAbsolutePath(), context.getPackageName());
//		if(dir!=null&&!dir.exists()){
//			return null;
//		}else{
//			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(dir, "user.txt")));
//			LoginBean person = (LoginBean) ois.readObject();
//			return person;
//		}
//	}
//
//	public static SchoolInfoBean getCurrentRole(Context context) throws Exception {
//		LoginBean loginBean = deserializePerson(context);
//		if(loginBean != null && loginBean.getItems().size() >= 1){
//			for (SchoolInfoBean schoolInfoBean : loginBean.getItems()) {
//				if (schoolInfoBean.getIsDefault() == 1) {
//					return schoolInfoBean;
//				}
//			}
//		}
//		return null;
//	}
}
