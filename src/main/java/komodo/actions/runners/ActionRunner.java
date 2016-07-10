package komodo.actions.runners;

import komodo.actions.Action;
import komodo.services.RunnerService;

public interface ActionRunner {

    String getId();

    Boolean run(Action action);
}
