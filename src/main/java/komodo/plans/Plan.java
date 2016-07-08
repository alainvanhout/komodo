package komodo.plans;

import komodo.actions.Action;
import komodo.actions.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plan {

    private String name;

    private List<Action> check = new ArrayList<>();
    private List<Action> success = new ArrayList<>();
    private List<Action> failure = new ArrayList<>();
    private Map<String, String> config = new HashMap<>();

    private double interval = 60;
    private transient LocalDateTime last;
    private Context context;

    public void init() {
        context = new Context();
        config.entrySet().forEach(e -> context.add(e.getKey(), e.getValue()));

        check.forEach(a -> a.context(new Context(context)));
        success.forEach(a -> a.context(new Context(context)));
        failure.forEach(a -> a.context(new Context(context)));
    }

    public List<Action> getCheck() {
        return check;
    }

    public List<Action> getSuccess() {
        return success;
    }

    public List<Action> getFailure() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
