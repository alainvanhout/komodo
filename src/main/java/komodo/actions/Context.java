package komodo.actions;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private Map<String, String> map = new HashMap<>();
    private Context parent = null;

    public Context() {
    }

    public Context(Context parent) {
        this.parent = parent;
    }

    public boolean contains(String key) {
        return map.containsKey(key) || (parent != null ? parent.contains(key) : false);
    }

    public String get(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        if (parent != null) {
            return parent.get(key);
        }
        return null;
    }

    public Context add(String key, String value) {
        map.put(key, value);
        return this;
    }

    public void setParent(Context parent) {
        this.parent = parent;
    }
}
