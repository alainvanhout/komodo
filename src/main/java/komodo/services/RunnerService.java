package komodo.services;

import komodo.actions.Action;
import komodo.actions.CombinedAction;
import komodo.actions.runners.ActionRunner;
import komodo.actions.runners.Runners;
import komodo.plans.loaders.FolderPlanLoader;
import komodo.plans.loaders.PlanLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class RunnerService {

    @Autowired
    private Collection<ActionRunner> actionRunners;

    @Value("${komodo.init.namedactions:named-actions/}")
    private String namedActionsPath;

    private Map<String, ActionRunner> runners = new HashMap<>();
    private Map<String, Action> namedActions = new HashMap<>();

    @PostConstruct
    public void load() {
        for (ActionRunner actionRunner : actionRunners) {
            runners.put(actionRunner.getId(), actionRunner);
        }

        File folder = new File(namedActionsPath);
        if (folder.exists()){
            PlanLoader namedActionsLoader = new FolderPlanLoader(folder);
            namedActionsLoader.load();
            for (Action action : namedActionsLoader.getPlans()) {
                action.init(null);
                namedActions.put(action.getName(), action);
            }
        }
    }

    public Boolean run(Action action, Action caller) {
        boolean success = run(action, caller, action.getRunner());
        action.getState().setSuccessful(success);
        return success;
    }

    public Boolean run(Action action, Action caller, String runner) {

        Action runnerAction = action;

        if (caller != null){
            runnerAction = new CombinedAction(action, caller);
        }

        if (namedActions.containsKey(runnerAction.getRunner())) {
            Action namedAction = namedActions.get(runnerAction.getRunner());
            return run(new CombinedAction(namedAction, runnerAction), null);
        }

        if (!runners.containsKey(runnerAction.getRunner())) {
            throw new RuntimeException("Runner not found: " + runnerAction.getRunner());
        }

        runnerAction.getState().setLast(LocalDateTime.now());
        Boolean success = runners.get(runner).run(runnerAction);

        // handle success or failure
        // but ignore when null
        if (success != null) {
            if (success == true) {
                run(action, null, Runners.SUCCESS_RUNNER);
            } else if (success == false) {
                run(action, null, Runners.FAILURE_RUNNER);
            }
        }

        return success;
    }

    public Map<String, Action> getNamedActions() {
        return namedActions;
    }

    public Map<String, ActionRunner> getRunners() {
        return runners;
    }
}
