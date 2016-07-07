package komodo.actions.runners;

import komodo.actions.Action;

public interface ActionRunner {

    String getId();

    boolean run(Action action);
}
