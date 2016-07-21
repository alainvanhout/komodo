package komodo.plans.loaders.file;

import komodo.actions.Action;
import komodo.plans.Plan;
import komodo.plans.loaders.ActionLoader;
import komodo.utils.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonFileLoader implements ActionLoader {

    private File file;
    private Action action;

    public JsonFileLoader(File file) {
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
            action = JsonUtil.toObject(IOUtils.toString(input), Action.class);
            if (StringUtils.isBlank(action.getName())) {
                action.setName(StringUtils.substringBeforeLast(file.getName(), ".json"));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not access file: " + file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Override
    public void reload() {
        action = null;
        load();
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(action);
    }

    public static boolean canLoad(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }
}
