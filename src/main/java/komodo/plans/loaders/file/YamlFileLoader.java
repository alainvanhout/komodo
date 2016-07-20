package komodo.plans.loaders.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import komodo.plans.Plan;
import komodo.plans.loaders.PlanLoader;
import komodo.utils.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class YamlFileLoader implements PlanLoader {

    private File file;
    private Plan plan;

    public YamlFileLoader(File file) {
        this.file = file;
    }

    @Override
    public void load() {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }
        FileInputStream input = null;


        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            input = new FileInputStream(file);
            plan = mapper.readValue(input, Plan.class);
            if (StringUtils.isBlank(plan.getName())) {
                plan.setName(StringUtils.substringBeforeLast(file.getName(), ".yml"));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not access file: " + file.getAbsolutePath(), e);
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

    public static boolean canLoad(File file) {
        return file.getName().toLowerCase().endsWith(".yml");
    }
}