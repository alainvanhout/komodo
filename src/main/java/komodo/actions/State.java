package komodo.actions;

import komodo.plans.Plan;

import java.time.LocalDateTime;

public class State {

    private transient LocalDateTime last;
    private boolean successful;

    public boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }


    public LocalDateTime getLast() {
        return last;
    }

    public void setLast(LocalDateTime last) {
        this.last = last;
    }

}
