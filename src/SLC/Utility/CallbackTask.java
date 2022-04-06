package SLC.Utility;

import java.util.Optional;

public abstract class CallbackTask {

    public final void runWith(Callback cb) {
        run();
        Optional.ofNullable(cb).ifPresent(Callback::call);
    }

    public abstract void run();
}
