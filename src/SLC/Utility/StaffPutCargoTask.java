package SLC.Utility;

import SLC.Locker.LockerManager;
import SLC.Locker.LockerStatus;

public class StaffPutCargoTask extends CallbackTask {

    @Override
    public void run() {
        int current = LockerManager.getInstance().getCurrentHandlingLocker();
        LockerManager.getInstance().lockers.get(current).setLockerStatus(LockerStatus.Lock);
        //Debug use
        LockerManager.getInstance().printLockers();
    }
}
