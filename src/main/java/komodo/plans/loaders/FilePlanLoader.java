package komodo.plans.loaders;

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

    private File file;
    private Plan plan;

    public FilePlanLoader(File file) {
        this.file = file;
    }

    @Override
    public void load() {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            plan = JsonUtil.toObject(IOUtils.toString(input), Plan.class);
            if (StringUtils.isBlank(plan.getName())) {
                plan.setName(StringUtils.substringBeforeLast(file.getName(), ".json"));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not access file: " + file.getAbsolutePath());
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Override
    public void reload() {
        plan = null;
        load();
    }

    @Override
    public List<Plan> getPlans() {
        return Arrays.asList(plan);
    }
}
