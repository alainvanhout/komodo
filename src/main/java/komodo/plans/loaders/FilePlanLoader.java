package komodo.plans.loaders;

import komodo.actions.Action;
import komodo.actions.loaders.ActionLoader;
import komodo.plans.Plan;
import komodo.utils.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FilePlanLoader implements PlanLoader {

    private Plan plan;
    private File file;

    public FilePlanLoader(File file) {
        this.file = file;
    }

    @Override
    public void reload() {
        if (!file.exists()){
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }
        try {
            plan = JsonUtil.toObject(IOUtils.toString(new FileInputStream(file)), Plan.class);
            if (StringUtils.isBlank(plan.getName())){
                plan.setName(file.getName());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not access file: " + file.getAbsolutePath());
        }
    }

    @Override
    public List<Plan> getPlans() {
        return Arrays.asList(plan);
    }
}