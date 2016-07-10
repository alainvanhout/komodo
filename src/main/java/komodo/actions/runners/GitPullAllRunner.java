package komodo.actions.runners;

import komodo.actions.Action;
import komodo.plans.loaders.GitRepositoryPlanLoader;
import komodo.plans.loaders.PlanLoader;
import komodo.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class GitPullAllRunner implements ActionRunner {

    @Autowired
    private PlanService planService;

    @Override
    public String getId() {
        return Runners.GIT_PULL_ALL;
    }

    @Override
    public Map<String, String> getParameters() {
        return Collections.emptyMap();
    }

    @Override
    public Boolean run(Action action) {
        try {
            List<PlanLoader> loaders = planService.getLoaders();
            for (PlanLoader loader : loaders) {
                if (loader instanceof GitRepositoryPlanLoader) {
                    loader.reload();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
