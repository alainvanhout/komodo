package komodo.services;

import komodo.actions.Action;
import komodo.actions.runners.ActionRunner;
import komodo.plans.Plan;
import komodo.plans.loaders.FilePlanLoader;
import komodo.plans.loaders.FolderPlanLoader;
import komodo.plans.loaders.PlanLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimerService {

    @Autowired
    private Collection<ActionRunner> actionRunners;

    private Map<String, ActionRunner> runners = new HashMap<>();
    private List<Plan> plans;

    @PostConstruct
    public void reload() {

        for (ActionRunner actionRunner : actionRunners) {
            runners.put(actionRunner.getId(), actionRunner);
        }

        PlanLoader planLoader = new FolderPlanLoader(new File("plans/"));
        planLoader.reload();

        plans = planLoader.getPlans();
        for (Plan plan : plans) {
            plan.init();
        }
    }

    private Boolean run(Action action) {
        if (action == null || action.getRunner() == null) {
            return null;
        }
        if (!runners.containsKey(action.getRunner())) {
            System.out.println("Action not found: " + action.getRunner());
            return null;
        }
        return runners.get(action.getRunner()).run(action);
    }

    @Scheduled(fixedRate = 1000)
    public void timer() throws IOException {

        for (Plan plan : plans) {
            if (plan.shouldRun()) {
                plan.last(LocalDateTime.now());
                boolean success = run(plan.getCheck());
                if (success == true) {
                    run(plan.getSuccess());
                } else if (success == false) {
                    run(plan.getFailure());
                }
            }
        }
    }
}
