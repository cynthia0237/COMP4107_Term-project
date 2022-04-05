package SLC.Locker;

public class Locker {
    private String id;
    private LockerSize size;
    private LockerStatus status;

    public Locker(String id, LockerSize size)
    {
        this.id = id;
        this.size = size;
        status = LockerStatus.Available;
    }

    public String getLockerId() {
        return id;
    }

    public void setLockerStatus(LockerStatus status) {
        this.status = status;
    }

    public LockerStatus getLockerStatus() {
        return status;
    }
}
