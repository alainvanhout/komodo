package komodo.plans;

import komodo.actions.Action;

import java.time.Duration;
import java.time.LocalDateTime;

public class Plan extends Action {

    private double interval = 60;

    public boolean shouldRun() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last = getState().getLast();
        return last == null || Duration.between(last, now).toMillis() >= interval * 1000 - 100;
    }
}
