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

public class CombinedAction extends Action {

    private Action runnerAction;
    private Action configAction;

    public CombinedAction(Action runnerAction, Action configAction) {
        this.runnerAction = runnerAction;
        this.configAction = configAction;
    }

    @Override
    public String getRunner() {
        return runnerAction.getRunner();
    }

    @Override
    public void init(Action parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(String key) {
        return get(key, null);
    }

    @Override
    public String get(String key, String defaultValue) {
        String value = runnerAction.get(key);
        if (value == null){
            value = configAction.get(key, defaultValue);
        }
        return value;
    }

    @Override
    public String getName() {
        return configAction.getName() + ":" + runnerAction.getName();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context getContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Action context(Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getConfig() {
        return configAction.getConfig();
    }

    @Override
    public void setConfig(Map<String, String> config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Action> getCheck() {
        return runnerAction.getCheck();
    }

    @Override
    public List<Action> getSuccess() {
        return runnerAction.getSuccess();
    }

    @Override
    public List<Action> getFailure() {
        return runnerAction.getFailure();
    }

    @Override
    public State getState() {
        return configAction.getState();
    }

    @Override
    public Action parent(Action parent) {
        throw new UnsupportedOperationException();
    }
}
