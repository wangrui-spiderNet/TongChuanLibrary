package alpha.cyber.intelmain.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 软件升级检测实体 TODO 类描述
 * <p/>
 * 创建时间: 2014年10月24日 上午10:16:22 <br/>
 * 
 * @author hwp
 * @version
 * @since v0.0.1
 */
public class AppUpgradeInfo implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * new_version_code : 1
	 * old_version_code : 1
	 * downurl : /123
	 * new_version_name : 1.0
	 * is_update : 0
	 * old_version_name : null
	 */

	private int new_version_code;
	private int old_version_code;
	private String downurl;
	private String new_version_name;
	private int is_update;
	private String old_version_name;

	public static AppUpgradeInfo objectFromData(String str) {

		return new Gson().fromJson(str, AppUpgradeInfo.class);
	}

	public int getNew_version_code() {
		return new_version_code;
	}

	public void setNew_version_code(int new_version_code) {
		this.new_version_code = new_version_code;
	}

	public int getOld_version_code() {
		return old_version_code;
	}

	public void setOld_version_code(int old_version_code) {
		this.old_version_code = old_version_code;
	}

	public String getDownurl() {
		return downurl;
	}

	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}

	public String getNew_version_name() {
		return new_version_name;
	}

	public void setNew_version_name(String new_version_name) {
		this.new_version_name = new_version_name;
	}

	public int getIs_update() {
		return is_update;
	}

	public void setIs_update(int is_update) {
		this.is_update = is_update;
	}

	public String getOld_version_name() {
		return old_version_name;
	}

	public void setOld_version_name(String old_version_name) {
		this.old_version_name = old_version_name;
	}
}
