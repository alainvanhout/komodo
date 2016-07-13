package komodo.services;

import komodo.actions.Action;
import komodo.actions.Context;
import komodo.plans.Plan;
import komodo.plans.loaders.FolderPlanLoader;
import komodo.plans.loaders.PlanLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Value("${komodo.init.plans:plans/}")
    private String planPath;

    private List<PlanLoader> loaders = new ArrayList<>();
    private Action configAction = new Action();

    @PostConstruct
    public void load() {
        setupDefaultLoaders();
        reload();
    }

    private void setupDefaultLoaders() {
//        loaders.add(new GitRepositoryPlanLoader("file:///C:/Projects/testrepo"));
        File folder = new File(planPath);
        if (folder.exists()) {
            loaders.add(new FolderPlanLoader(folder));
        }
    }

    public void reload() {
        loaders.forEach(PlanLoader::reload);
        getPlans().forEach(p -> p.init(configAction));
    }

    public List<Plan> getPlans() {
        return loaders.stream()
                .flatMap(p -> p.getPlans().stream())
                .collect(Collectors.toList());
    }

    public List<PlanLoader> getLoaders() {
        return loaders;
    }
}
