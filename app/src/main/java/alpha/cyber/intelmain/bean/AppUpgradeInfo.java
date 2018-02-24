package alpha.cyber.intelmain.bean;

import com.alibaba.fastjson.JSON;

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
	 * 升级状态： 0 表示已是最新 1 检测到新版本升级 2 检测到强制升级版本
	 */
	private int updateType;

	/**
	 * 升级版本
	 */
	private String version;

	/**
	 * 升级编号
	 */
	private long versionCode;

	/**
	 * 升级介绍
	 */
	private String versionIntro;
	/**
	 * 升级版本下载地址
	 */
	private String downLoadUrl;
	/**
	 * 升级包发布时间
	 */
	private long createDate;
	/**
	 * 升级包大小
	 */
	private String versionSize;

	/**
	 * 升级状态：0 表示已是最新 1 检测到新版本升级 2 检测到强制升级版本
	 */
	public int getUpdateType() {
		return updateType;
	}

	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(long versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionIntro() {
		return versionIntro;
	}

	public void setVersionIntro(String versionIntro) {
		this.versionIntro = versionIntro;
	}

	public String getDownLoadUrl() {
		return downLoadUrl;
	}

	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public String getVersionSize() {
		return versionSize;
	}

	public void setVersionSize(String versionSize) {
		this.versionSize = versionSize;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(this);
	}
}
