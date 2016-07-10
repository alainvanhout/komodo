package komodo.services;

import komodo.actions.Context;
import komodo.plans.Plan;
import komodo.plans.loaders.FolderPlanLoader;
import komodo.plans.loaders.PlanLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private List<PlanLoader> planLoaders = new ArrayList<>();
    private List<Plan> plans;

    @PostConstruct
    public void load() {
        //planLoaders.add(new GitRepositoryPlanLoader("file:///C:/Projects/testrepo"));
        planLoaders.add(new FolderPlanLoader(new File("plans/")));

        planLoaders.forEach(PlanLoader::load);
        plans = planLoaders.stream()
                .flatMap(p -> p.getPlans().stream())
                .collect(Collectors.toList());

        Context context = new Context();
        plans.forEach(p -> p.init(context));
    }

    public void reload() {
        planLoaders.forEach(PlanLoader::reload);
        plans = planLoaders.stream()
                .flatMap(p -> p.getPlans().stream())
                .collect(Collectors.toList());

        Context context = new Context();
        plans.forEach(p -> p.init(context));
    }

    public List<Plan> getPlans() {
        return plans;
    }
}
