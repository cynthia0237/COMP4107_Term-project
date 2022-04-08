package SLC.Locker;

public class Locker {
    private String id;
    private LockerSize size;
    private LockerStatus status;
    private boolean lock;

    public Locker(String id, LockerSize size)
    {
        this.id = id;
        this.size = size;
        status = LockerStatus.Available;
        lock = true;
    }

    public String getLockerId() {
        return id;
    }

    public LockerSize getLockerSize() {
        return size;
    }

    public void setLockerStatus(LockerStatus status) {
        this.status = status;
    }

    public LockerStatus getLockerStatus() {
        return status;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

}
