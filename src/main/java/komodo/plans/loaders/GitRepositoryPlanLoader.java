package komodo.plans.loaders;

import komodo.plans.Plan;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GitRepositoryPlanLoader implements PlanLoader {

    private FolderPlanLoader folderPlanLoader;

    public GitRepositoryPlanLoader() {
    }

    @Override
    public void reload() {
        try {
            File localFolder = Files.createTempDirectory(new File("").toPath(), "").toFile();
            String localPath = localFolder.getAbsolutePath();
            String remotePath = "file:///C:/Projects/testrepo";
            Git.cloneRepository()
                    .setURI(remotePath)
                    .setDirectory(new File(localPath))
                    .call();
            FileRepository localRepo = new FileRepository(localPath + "/.git");
            Git git = new Git(localRepo);

            git.pull().call();

            folderPlanLoader = new FolderPlanLoader(localFolder);
            folderPlanLoader.reload();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RefNotFoundException e) {
            e.printStackTrace();
        } catch (WrongRepositoryStateException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (CanceledException e) {
            e.printStackTrace();
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
        } catch (TransportException e) {
            e.printStackTrace();
        } catch (DetachedHeadException e) {
            e.printStackTrace();
        } catch (NoHeadException e) {
            e.printStackTrace();
        } catch (RefNotAdvertisedException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Plan> getPlans() {
        return folderPlanLoader.getPlans();
    }
}
