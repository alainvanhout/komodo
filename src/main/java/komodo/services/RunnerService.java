package komodo.services;

import komodo.actions.Action;
import komodo.actions.runners.ActionRunner;
import komodo.actions.runners.Runners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class RunnerService {

    @Autowired
    private Collection<ActionRunner> actionRunners;

    private Map<String, ActionRunner> runners = new HashMap<>();

    @PostConstruct
    public void load() {
        for (ActionRunner actionRunner : actionRunners) {
            runners.put(actionRunner.getId(), actionRunner);
        }
    }

    public Boolean run(Action action) {
        boolean success = run(action, action.getRunner());
        action.getState().setSuccessful(success);

        return success;
    }

    public Boolean run(Action action, String runner) {
        if (!runners.containsKey(action.getRunner())) {
            throw new RuntimeException("Runner not found: " + action.getRunner());
        }

        action.getState().setLast(LocalDateTime.now());
        Boolean success = runners.get(runner).run(action);

        // handle success or failure
        // but ignore when null
        if (success != null) {
            if (success == true) {
                run(action, Runners.SUCCESS_RUNNER);
            } else if (success == false) {
                run(action, Runners.FAILURE_RUNNER);
            }
        }

        return success;
    }
}
