package komodo.services;

import komodo.actions.Action;
import komodo.actions.runners.ActionRunner;
import komodo.plans.Plan;
import komodo.plans.loaders.FolderPlanLoader;
import komodo.plans.loaders.PlanLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimerService {

    @Autowired
    private Collection<ActionRunner> actionRunners;

    private Map<String, ActionRunner> runners = new HashMap<>();
    private List<PlanLoader> planLoaders = new ArrayList<>();
    private List<Plan> plans;

    @PostConstruct
    public void load() {
        for (ActionRunner actionRunner : actionRunners) {
            runners.put(actionRunner.getId(), actionRunner);
        }

        //planLoaders.add(new GitRepositoryPlanLoader("file:///C:/Projects/testrepo"));
        planLoaders.add(new FolderPlanLoader(new File("plans/")));

        planLoaders.forEach(PlanLoader::load);
        plans = planLoaders.stream()
                .flatMap(p -> p.getPlans().stream())
                .collect(Collectors.toList());
        plans.forEach(Plan::init);}

    public void reload() {
        planLoaders.forEach(PlanLoader::reload);
        plans = planLoaders.stream()
                .flatMap(p -> p.getPlans().stream())
                .collect(Collectors.toList());
        plans.forEach(Plan::init);
    }

    @Scheduled(fixedRate = 1000)
    public void timer() throws IOException {
        for (Plan plan : plans) {
            if (plan.shouldRun()) {
                andRunner(plan);
            }
        }
    }

    private Boolean andRunner(List<Action> actions) {
        // todo : replace this AndResultEvaluator to a provided <? implements ResultEvaluator> with this one as default
        return actions.stream().map(this::andRunner).reduce((a, b) -> a && b).orElse(null);
    }

    private Boolean andRunner(Action action) {
        action.getState().setLast(LocalDateTime.now());

        boolean success;
        if (runners.containsKey(action.getRunner())) {
            // use specified runner
            success = runners.get(action.getRunner()).run(action);
        } else {
            // use AND-runner
            success = andRunner(action.getCheck());
            action.getState().setSuccessful(success);
        }

        if (success == true) {
            andRunner(action.getSuccess());
        } else if (success == false) {
            andRunner(action.getFailure());
        }
        return success;
    }

    public List<Plan> getPlans() {
        return plans;
    }
}
