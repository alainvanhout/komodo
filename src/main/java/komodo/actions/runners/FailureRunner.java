package komodo.actions.runners;

import komodo.actions.Action;
import komodo.actions.CombinedAction;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class FailureRunner implements ActionRunner {

    @Autowired
    private RunnerService runnerService;
    @Override
    public String getId() {
        return Runners.FAILURE_RUNNER;
    }

    @Override
    public Map<String, String> getParameters(){
        return Collections.emptyMap();
    }

    @Override
    public Boolean run(Action action) {
        List<Action> failure = action.getFailure();
        failure.forEach((a) -> runnerService.run(new CombinedAction(a, action), null));
        // should not trigger success or failure
        return null;
    }
}
