package komodo.services;

import komodo.actions.CombinedAction;
import komodo.plans.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TimerService {

    @Autowired
    private PlanService planService;

    @Autowired
    private RunnerService runnerService;

    @Scheduled(fixedRate = 1000)
    public void timer() {
        for (Plan plan : planService.getPlans()) {
            if (plan.shouldRun()) {
//                runnerService.run(new CombinedAction(plan, planService.getConfigAction()), null);
            }
        }
    }
}
