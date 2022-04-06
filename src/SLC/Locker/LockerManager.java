package SLC.Locker;

import java.util.ArrayList;
import java.util.List;

public class LockerManager {

    static LockerManager instance = null;

    public List<Locker> lockers = new ArrayList<>();
    int lockerCount = 6;
    LockerSize size;
    int currentHandlingLocker;

    public static LockerManager getInstance(){
        if(instance == null){
            synchronized(LockerManager.class){
                if(instance == null){
                    instance = new LockerManager();
                }
            }
        }
        return instance;
    }

    public LockerManager() {
        initLockers();
    }

    void initLockers() {
        for (int i = 0; i < lockerCount; i++) {
            if (i < 2) {
                size = LockerSize.S;
            }
            if (i >= 2 && i < 4) {
                size = LockerSize.M;
            }
            if (i >= 4 && i < 6) {
                size = LockerSize.L;
            }

            Locker locker = new Locker(String.valueOf(i), size);
            lockers.add(locker);
        }

        System.out.println("init locker done");
    }

    public void setCurrentHandlingLocker(int current) {
        currentHandlingLocker = current;
    }

    public int getCurrentHandlingLocker() {
        return currentHandlingLocker;
    }

    //Debug use
    public void printLockers() {
        for (Locker locker : lockers) {
            System.out.println(locker.getLockerId() + " " + locker.getLockerStatus());
        }
    }

}
