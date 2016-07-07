package komodo.actions.loaders;

import komodo.actions.Action;
import komodo.utils.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileActionLoader implements ActionLoader {

    private Action action;
    private File file;

    public FileActionLoader(File file) {
        this.file = file;
    }

    @Override
    public void reload() {
        try {
            action = JsonUtil.toObject(IOUtils.toString(new FileInputStream(file)), Action.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(action);
    }
}
