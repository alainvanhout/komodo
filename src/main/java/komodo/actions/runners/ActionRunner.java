package komodo.actions.runners;

import komodo.actions.Action;

import java.util.Map;

public interface ActionRunner {

    String getId();

    Map<String, String> getParameters();

    Boolean run(Action action);
}
