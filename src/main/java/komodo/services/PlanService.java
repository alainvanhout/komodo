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

    private List<PlanLoader> loaders = new ArrayList<>();

    @PostConstruct
    public void load() {
        setupDefaultLoaders();

        Context context = new Context();

        loaders.forEach(PlanLoader::load);
        loaders.stream()
                .flatMap(p -> p.getPlans().stream())
                .peek(p -> p.init(context))
                .collect(Collectors.toList());
    }

    private void setupDefaultLoaders() {
//        loaders.add(new GitRepositoryPlanLoader("file:///C:/Projects/testrepo"));
        File folder = new File("plans/");
        if (folder.exists()) {
            loaders.add(new FolderPlanLoader(folder));
        }
    }

    public void reload() {
        loaders.forEach(PlanLoader::reload);
        Context context = new Context();
        getPlans().forEach(p -> p.init(context));
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
