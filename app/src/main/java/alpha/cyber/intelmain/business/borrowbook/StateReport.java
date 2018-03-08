package alpha.cyber.intelmain.business.borrowbook;

import java.io.Serializable;

/**
 * Created by wangrui on 2018/2/22.
 */
public class StateReport implements Serializable {

    private String lockId;
    private String lockState;

    public StateReport() {
        super();
    }

    public StateReport(String lockId, String lockState) {
        this.setLockId(lockId);
        this.setLockState(lockState);
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }


    @Override
    public String toString() {
        return "StateReport{" +
                "lockId='" + lockId + '\'' +
                ", lockState='" + lockState + '\'' +
                '}';
    }
}
