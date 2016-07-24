package komodo.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The first first (runner) action provides the runner id and config, while the second (config) action only provides
 * config information (which the runner also provides, with the runner action taking precedence)
 */
public class CombinedAction extends Action {

    private Action runnerAction;
    private Action configAction;

    public CombinedAction(Action runnerAction, Action configAction) {
        // set to ensure that it errors when this action is actually asked to be run
        super.setRunner("combined-action");
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
    public String get(String key, String defaultValue, Action root) {
        String value = runnerAction.get(key, root);
        if (value == null) {
            value = configAction.get(key, defaultValue, root);
        }
        return interpret(value, root);
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
        Map<String, String> map = new HashMap<>();
        map.putAll(configAction.getFullConfig());
        map.putAll(runnerAction.getFullConfig());
        return map;
    }

    @Override
    public void setConfig(Map<String, String> config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRunner(String runner) {
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
        return runnerAction.getState();
    }

    @Override
    public Action parent(Action parent) {
        throw new UnsupportedOperationException();
    }
}
