package komodo.actions;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private Map<String, String> map = new HashMap<>();

    public Context() {
    }
    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public String get(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    public Context add(String key, String value) {
        map.put(key, value);
        return this;
    }
}
