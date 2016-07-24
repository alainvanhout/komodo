package komodo.actions;

import komodo.actions.runners.Runners;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {
    private String name;
    private boolean active = true;
    protected String runner = Runners.AND_CHECK_RUNNER;
    private List<Action> check = new ArrayList<>();

    private List<Action> success = new ArrayList<>();
    private List<Action> failure = new ArrayList<>();
    private Map<String, String> config = new HashMap<>();

    private transient Action parent;
    private transient Context context = new Context();
    private transient State state = new State();

    public String getRunner() {
        return runner;
    }

    public Action() {
    }

    public void init(Action parent) {
        this.parent = parent;
        check.forEach(a -> a.init(this));
        success.forEach(a -> a.init(this));
        failure.forEach(a -> a.init(this));
    }

    public String get(String key) {
        return get(key, null, this);
    }

    public String get(String key, Action root) {
        return get(key, null, root);
    }


    public String get(String key, String defaultValue) {
        return get(key, defaultValue, this);
    }


    public String get(String key, String defaultValue, Action root) {
        String value = getValue(key, defaultValue, root);
        return interpret(value, root);
    }

    private String getValue(String key, String defaultValue, Action root) {
        String value = null;
        // first check config
        if (config.containsKey(key)) {
            value = config.get(key);
        }
        // then ask parent
        if (value == null && parent != null) {
            value = parent.get(key, defaultValue, root);
        }
        // fallback on default
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    protected String interpret(String input, Action root) {
        Pattern regex = Pattern.compile("(\\$\\{[^\\{]*\\})");
        Matcher regexMatcher = regex.matcher(StringUtils.defaultIfEmpty(input, ""));
        String output = input;
        while (regexMatcher.find()) {
            MatchResult matchResult = regexMatcher.toMatchResult();
            String group = matchResult.group();
            String inner = StringUtils.substring(group, 2, group.length() - 1);
            String innerValue = root.get(inner);
            output = output.replace(group, innerValue != null ? innerValue : "<not found>");
        }
        return output;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context getContext() {
        return context;
    }

    public Action context(Context context) {
        this.context = context;
        return this;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public Map<String, String> getFullConfig() {
        Map<String, String> map = new HashMap<>();
        map.putAll(getConfig());
        if (parent != null) {
            map.putAll(parent.getFullConfig());
        }
        return map;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
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

    public State getState() {
        return state;
    }

    public Action parent(Action parent) {
        this.parent = parent;
        return this;
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
