package alpha.cyber.intellib.lock;

public interface LockFunction {
	
	public void open();
	
	public void close();
	
	public int getBoardAddress();

	public int getDoorState(byte LockID, byte boardAddress);
	
	public int getAllDoorState(byte boardAddress);
	
	public int getProtocalID(int boardAddr);

	public void openGrid(byte doorID, byte boardAddress,boolean isCallBack);

}
