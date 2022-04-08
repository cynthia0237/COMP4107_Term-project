package SLC.Locker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LockerManager {

   static LockerManager instance = null;

    public List<Locker> lockers = new ArrayList<>();
    public HashMap<String, Locker> lockerMap = new HashMap<>();

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

    public Locker reserveLocker(LockerSize size) {

        List<Locker> suitableLockers = lockers.stream().filter(locker -> locker.getLockerStatus() == LockerStatus.Available && locker.getLockerSize() == size).collect(Collectors.toList());

        if (suitableLockers.size() > 0) {
            return suitableLockers.get(0);
        } else {
            return null;
        }
    }

    public Locker getLockerById(String id) {
        return lockerMap.getOrDefault(id, null);
    }

    //Debug use
    public void printLockers() {
        for (Locker locker : lockers) {
            System.out.println(locker.getLockerId() + " " + locker.getLockerSize());
        }
    }

}
