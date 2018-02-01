package alpha.cyber.intellib.lock;


public interface LockCallback {
	
	public void onGetProtocalVerison(int version);
	public void onGetLockState(int id, byte state);
	public void onGetAllLockState(byte[] state);
	
}
