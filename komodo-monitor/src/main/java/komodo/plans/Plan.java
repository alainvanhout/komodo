package komodo.plans;

import komodo.actions.Action;

import java.time.Duration;
import java.time.LocalDateTime;

public class Plan {

    private double interval;
    private boolean active;
    private Action action;

    public Plan(Action action) {
        this.action = action;
    }

    public Plan init(){
        interval = Double.parseDouble(action.get("interval", "60"));
        active = action.isActive();
        return this;
    }

    public boolean shouldRun() {
        if (!this.isActive()){
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last = action.getState().getLast();
        return last == null || Duration.between(last, now).toMillis() >= interval * 1000 - 100;
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
