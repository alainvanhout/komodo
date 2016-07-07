package komodo.plans;

import komodo.actions.Action;
import komodo.actions.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Plan {

    private Action check = new Action();
    private Action success = new Action();
    private Action failure = new Action();
    private Map<String, String> config = new HashMap<>();

    private double interval = 60;
    private transient LocalDateTime last;
    private Context context;

    public void init(){
        context = new Context();
        config.entrySet().forEach(e -> context.add(e.getKey(), e.getValue()));

        check.context(new Context(context));
        success.context(new Context(context));
        failure.context(new Context(context));
    }

    public Action getCheck() {
        return check;
    }

    public Action getSuccess() {
        return success;
    }

    public Action getFailure() {
        return failure;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }


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
