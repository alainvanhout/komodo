package komodo.services;

import komodo.actions.Action;
import komodo.plans.Plan;
import komodo.actions.loaders.FolderActionLoader;
import komodo.actions.loaders.ActionLoader;
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

    private List<ActionLoader> loaders = new ArrayList<>();
    private Action configAction = new Action();
    private List<Plan> plans;

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
            loaders.add(new FolderActionLoader(folder));
        }
    }

    public void reload() {
        reloadConfig();
        loaders.forEach(ActionLoader::reload);
        plans = loadPlans();
        plans.forEach(p -> p.getAction().init(null));
    }

    private void reloadConfig() {
        File configActionFile = new File(configActionPath);
        if (configActionFile.exists()) {
            HashMap<String, String> map = JsonUtil.toObject(configActionFile, HashMap.class);
            configAction.setConfig(map);
        }
    }

    private List<Plan> loadPlans() {
        return loaders.stream()
                .flatMap(action -> action.getActions().stream())
                .map(action -> new Plan(action).init())
                .collect(Collectors.toList());
    }

    public List<ActionLoader> getLoaders() {
        return loaders;
    }

    public Action getConfigAction() {
        return configAction;
    }

    public List<Plan> getPlans() {
        return plans;
    }
}
