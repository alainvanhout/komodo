package komodo.plans.loaders;

import komodo.plans.Plan;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.FetchResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class GitRepositoryPlanLoader implements PlanLoader {

    private final String remotePath;
    private Git git = null;
    private FolderPlanLoader folderPlanLoader = null;

    public GitRepositoryPlanLoader(String remotePath) {
        this.remotePath = remotePath;
    }

    @Override
    public void load() {
        try {
            File localFolder = Files.createTempDirectory(new File("").toPath(), "").toFile();
            String localPath = localFolder.getAbsolutePath();
            Git.cloneRepository()
                    .setURI(remotePath)
                    .setDirectory(new File(localPath))
                    .call();

            FileRepository localRepo = new FileRepository(localPath + "/.git");
            git = new Git(localRepo);

            git.pull().call();

            folderPlanLoader = new FolderPlanLoader(localFolder);
            folderPlanLoader.reload();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RefNotFoundException e) {
            throw new RuntimeException(e);
        } catch (WrongRepositoryStateException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        } catch (CanceledException e) {
            throw new RuntimeException(e);
        } catch (InvalidRemoteException e) {
            throw new RuntimeException(e);
        } catch (TransportException e) {
            throw new RuntimeException(e);
        } catch (DetachedHeadException e) {
            throw new RuntimeException(e);
        } catch (NoHeadException e) {
            throw new RuntimeException(e);
        } catch (RefNotAdvertisedException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reload() {
        try {
            git.pull().call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        folderPlanLoader.reload();
    }

    @Override
    public List<Plan> getPlans() {
        return folderPlanLoader.getPlans();
    }
}
