package komodo.plans.loaders;

import komodo.plans.Plan;
import komodo.plans.loaders.file.JsonFileLoader;
import komodo.plans.loaders.file.XmlFileLoader;
import komodo.plans.loaders.file.YamlFileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FolderPlanLoader implements PlanLoader {

    private File folder;
    private List<PlanLoader> loaders = new ArrayList<>();

    public FolderPlanLoader(File folder) {
        this.folder = folder;
    }

    @Override
    public void load() {
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder does not exist: " + folder.getAbsolutePath());
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("File is not a folder: " + folder.getAbsolutePath());
        }
        try {
            Files.walk(Paths.get(folder.getAbsolutePath())).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    File file = filePath.toFile();
                    if (JsonFileLoader.canLoad(file)) {
                        JsonFileLoader loader = new JsonFileLoader(file);
                        loader.reload();
                        loaders.add(loader);
                    }
                    if (YamlFileLoader.canLoad(file)) {
                        YamlFileLoader loader = new YamlFileLoader(file);
                        loader.reload();
                        loaders.add(loader);
                    }
                    if (XmlFileLoader.canLoad(file)) {
                        XmlFileLoader loader = new XmlFileLoader(file);
                        loader.reload();
                        loaders.add(loader);
                    }
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Encountered error while walking folder: " + folder.getAbsolutePath());
        }
    }

    @Override
    public void reload() {
        loaders = new ArrayList<>();
        load();
    }

    @Override
    public List<Plan> getPlans() {
        return loaders.stream()
                .flatMap(l -> l.getPlans().stream())
                .collect(Collectors.toList());
    }
}
