package komodo.actions.loaders.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import komodo.actions.Action;
import komodo.actions.loaders.ActionLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class YamlFileLoader implements ActionLoader {

    private File file;
    private Action action;

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
            action = mapper.readValue(input, Action.class);
            if (StringUtils.isBlank(action.getName())) {
                action.setName(StringUtils.substringBeforeLast(file.getName(), ".yml"));
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
        return file.getName().toLowerCase().endsWith(".yml");
    }
}
