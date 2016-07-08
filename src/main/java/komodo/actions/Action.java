package komodo.actions;

import org.apache.commons.lang.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {
    private String runner;
    private Map<String, String> config;

    // in seconds
    private Boolean success;

    private Context context;

    public String getRunner() {
        return runner;
    }

    public Action runnerId(String actionId) {
        this.runner = actionId;
        return this;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public String get(String key){
        if (config != null && config.containsKey(key)){
            return interpret(config.get(key));
        }
        return interpret(context.get(key));
    }

    public Boolean getSuccess() {
        return success;
    }

    public Action success(Boolean success) {
        this.success = success;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public Action context(Context context) {
        this.context = context;
        return this;
    }

    private String interpret(String input){
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
}
