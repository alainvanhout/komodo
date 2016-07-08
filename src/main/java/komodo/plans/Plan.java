package komodo.plans;

import komodo.actions.Action;
import komodo.actions.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plan extends Action {

    private double interval = 60;
    private transient LocalDateTime last;

    public LocalDateTime getLast() {
        return last;
    }

    public Plan last(LocalDateTime last) {
        this.last = last;
        return this;
    }

    public boolean shouldRun() {
        LocalDateTime now = LocalDateTime.now();
        return last == null || Duration.between(last, now).toMillis() >= interval * 1000 - 100;
    }
}
