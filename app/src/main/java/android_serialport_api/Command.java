package android_serialport_api;

import java.util.Arrays;


public class Command {
	
	public enum DataLengthType {
		FIXED,
		UNFIXED,
	}
	
	private int protocolType;

	private int cmdType;
	
	private int cmdId;
	
	private byte[] cmd;
	
	private long timeout;
	
	private int userId;
	
	private DataLengthType dataLenghtType = DataLengthType.FIXED;
	

	public DataLengthType getDataLenghtType() {
		return dataLenghtType;
	}

	public void setDataLenghtType(DataLengthType dataLenghtType) {
		this.dataLenghtType = dataLenghtType;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}


	public int getCmdType() {
		return cmdType;
	}

	public void setCmdType(int cmdType) {
		this.cmdType = cmdType;
	}

	public int getCmdId() {
		return cmdId;
	}

	public void setCmdId(int cmdId) {
		this.cmdId = cmdId;
	}

	public byte[] getCmd() {
		return cmd;
	}

	public void setCmd(byte[] cmd) {
		this.cmd = cmd;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Command [protocolType=" + protocolType + ", cmdType=" + cmdType + ", cmdId=" + cmdId + ", cmd="
				+ Arrays.toString(cmd) + ", timeout=" + timeout + ", userId=" + userId + ", dataLenghtType="
				+ dataLenghtType + "]";
	}

	
}
