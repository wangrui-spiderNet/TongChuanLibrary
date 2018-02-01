package alpha.cyber.intellib.lock;

import java.nio.ByteBuffer;

import alpha.cyber.serial.Command;

public class LockCommand extends Command {
	
	
	private static final int PROTOCAL_TYPE = 1;
	
	private static final long DEFAULT_TIMEOUT = 10000;
	
	
	public final static byte CMD_GETPROID = (byte)0x91;//get protocalId version
	public final static byte CMD_GETBOARDADDRESS = (byte)0x81;
	public final static byte CMD_OPENLOCK = (byte)0x8A;
	public final static byte CMD_GETLOCKSTATE = (byte)0x80;
	
	public final static byte STATUS_ON = 0x11;
	public final static byte STATUS_OFF = 0x00;
	
	
	public LockCommand() {
		setDataLenghtType(DataLengthType.FIXED);
		setProtocolType(PROTOCAL_TYPE);
		setTimeout(DEFAULT_TIMEOUT);
	}

	
	private static LockCommand create5Bits(byte cmd, byte boardAddress, byte byte1, byte byte2) {
		LockCommand command = new LockCommand();
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.put(cmd);
		buffer.put(boardAddress);
		buffer.put(byte1);
		buffer.put(byte2);
		buffer.put(getXor(buffer.array()));
		command.setCmd(buffer.array());
		command.setCmdId(cmd);
		return command;
	}
	
	
	public static LockCommand openGrid(byte lockId,byte boardAddress) {		
		LockCommand cmd = create5Bits(CMD_OPENLOCK, boardAddress, lockId, STATUS_ON);
		return cmd;
	}
	
	public static LockCommand getDoorState(byte lockId,byte boardAddress){
		LockCommand cmd = create5Bits(CMD_GETLOCKSTATE,boardAddress,lockId,(byte)0x33);
		return cmd;
	}
	
	public static byte getXor(byte[] datas){  
		  
	    byte temp=datas[0];  
	          
	    for (int i = 1; i <datas.length; i++) {  
	        temp ^=datas[i];  
	    }  
	  
	    return temp;  
	}  

}
