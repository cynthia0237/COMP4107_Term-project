package Locker;

public class Locker {
    private String id;
    private LockerSize size;
    private LockerStatus status;

    public Locker(LockerSize size)
    {
        //id =
        this.size = size;
        status = LockerStatus.Available;
    }
}
