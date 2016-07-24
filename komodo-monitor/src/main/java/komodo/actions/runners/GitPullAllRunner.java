package komodo.actions.runners;

import komodo.actions.Action;
import komodo.actions.loaders.GitRepositoryActionLoader;
import komodo.actions.loaders.ActionLoader;
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
            List<ActionLoader> loaders = planService.getLoaders();
            for (ActionLoader loader : loaders) {
                if (loader instanceof GitRepositoryActionLoader) {
                    loader.reload();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
