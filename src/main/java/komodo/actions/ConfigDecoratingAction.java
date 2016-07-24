package komodo.actions;

import java.util.List;
import java.util.Map;

public class ConfigDecoratingAction extends Action{

    private final Action action;
    private final Action decorator;

    public ConfigDecoratingAction(Action action, Action decorator) {
        this.action = action;
        this.decorator = decorator;
    }

    @Override
    public String getRunner() {
        return action.getRunner();
    }

    @Override
    public void init(Action parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(String key, String defaultValue, Action root) {
        // the primary action takes precedence over the decorator
        String value = action.get(key, root);
        if (value == null) {
            value = decorator.get(key, defaultValue, root);
        }
        return interpret(value, root);
    }

    @Override
    protected String interpret(String input, Action root) {
        return action.interpret(input, root);
    }

    @Override
    public String getName() {
        return action.getName();
    }

    @Override
    public void setName(String name) {
        action.setName(name);
    }

    @Override
    public Context getContext() {
        return action.getContext();
    }

    @Override
    public Action context(Context context) {
        return action.context(context);
    }

    @Override
    public Map<String, String> getConfig() {
        return action.getConfig();
    }

    @Override
    public Map<String, String> getFullConfig() {
        return action.getFullConfig();
    }

    @Override
    public void setConfig(Map<String, String> config) {
        action.setConfig(config);
    }

    @Override
    public List<Action> getCheck() {
        return action.getCheck();
    }

    @Override
    public List<Action> getSuccess() {
        return action.getSuccess();
    }

    @Override
    public List<Action> getFailure() {
        return action.getFailure();
    }

    @Override
    public State getState() {
        return action.getState();
    }

    @Override
    public Action parent(Action parent) {
        return action.parent(parent);
    }

    @Override
    public void setRunner(String runner) {
        action.setRunner(runner);
    }

    @Override
    public Boolean isActive() {
        return action.isActive();
    }

    @Override
    public void setActive(boolean active) {
        action.setActive(active);
    }
}
