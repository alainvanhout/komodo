package komodo.services;

import komodo.actions.Action;
import komodo.plans.Plan;
import komodo.plans.loaders.FolderPlanLoader;
import komodo.plans.loaders.PlanLoader;
import komodo.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Value("${komodo.init.plans:plans/}")
    private String planPath;

    @Value("${komodo.init.configaction:config.json}")
    private String configActionPath;

    private List<PlanLoader> loaders = new ArrayList<>();
    private Action configAction = new Action();

    @PostConstruct
    public void load() {
        // set to "config" because it should error when the config action is asked to actually run
        configAction.setRunner("config");
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
        reloadConfig();
        loaders.forEach(PlanLoader::reload);
        getPlans().forEach(p -> p.init(null));
    }

    private void reloadConfig() {
        File configActionFile = new File(configActionPath);
        if (configActionFile.exists()) {
            HashMap<String, String> map = JsonUtil.toObject(configActionFile, HashMap.class);
            configAction.setConfig(map);
        }
    }

    public List<Plan> getPlans() {
        return loaders.stream()
                .flatMap(p -> p.getPlans().stream())
                .collect(Collectors.toList());
    }

    public List<PlanLoader> getLoaders() {
        return loaders;
    }

    public Action getConfigAction() {
        return configAction;
    }
}
