package komodo.plans.loaders;

import komodo.plans.Plan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FolderPlanLoader implements PlanLoader {

    private List<FilePlanLoader> loaders = new ArrayList<>();
    private File folder;

    public FolderPlanLoader(File folder) {
        this.folder = folder;
    }

    @Override
    public void reload() {
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder does not exist: " + folder.getAbsolutePath());
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("File is not a folder: " + folder.getAbsolutePath());
        }
        try {
            Files.walk(Paths.get(folder.getAbsolutePath())).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    FilePlanLoader loader = new FilePlanLoader(filePath.toFile());
                    loader.reload();
                    loaders.add(loader);
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Encountered error while walking folder: " + folder.getAbsolutePath());
        }
    }

    @Override
    public List<Plan> getPlans() {
        return loaders.stream()
                .flatMap(l -> l.getPlans().stream())
                .collect(Collectors.toList());
    }
}
