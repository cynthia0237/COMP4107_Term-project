package SLC.Locker;

import java.util.ArrayList;
import java.util.List;

public class LockerManager {

    List<Locker> lockers = new ArrayList<Locker>();
    int lockerCount = 6;
    LockerSize size;

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
    }

    //Debug use
    public void printLockers() {
        for (Locker locker : lockers) {
            System.out.println(locker.getLockerId());
        }
    }

}
