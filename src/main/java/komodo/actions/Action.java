package komodo.actions;

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
    private String runner = "and-check-runner";

    private List<Action> check = new ArrayList<>();
    private List<Action> success = new ArrayList<>();
    private List<Action> failure = new ArrayList<>();

    private Map<String, String> config = new HashMap<>();
    private Context context;

    private State state = new State();

    public String getRunner() {
        return runner;
    }

    public void init(Context context) {
        this.context = new Context(context);
        config.entrySet().forEach(e -> context.add(e.getKey(), e.getValue()));

        check.forEach(a -> a.init(this.context));
        success.forEach(a -> a.init(this.context));
        failure.forEach(a -> a.init(this.context));
    }

    public Action runnerId(String actionId) {
        this.runner = actionId;
        return this;
    }

    public String get(String key) {
        if (config != null && config.containsKey(key)) {
            return interpret(config.get(key));
        }
        return interpret(context.get(key));
    }

    private String interpret(String input) {
        Pattern regex = Pattern.compile("(\\$\\{[^\\{]*\\})");
        Matcher regexMatcher = regex.matcher(StringUtils.defaultIfEmpty(input, ""));
        String output = input;
        while (regexMatcher.find()) {
            MatchResult matchResult = regexMatcher.toMatchResult();
            String group = matchResult.group();
            String inner = StringUtils.substring(group, 2, group.length() - 1);
            String innerValue = get(inner);
            output = output.replace(group, innerValue != null ? innerValue : "null");
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
}
