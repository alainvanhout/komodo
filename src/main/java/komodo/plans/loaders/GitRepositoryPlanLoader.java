package komodo.plans.loaders;

import komodo.plans.Plan;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class GitRepositoryPlanLoader implements PlanLoader {

    private final String remotePath;
    private Git git = null;
    private FolderPlanLoader folderPlanLoader = null;
    private FileRepository localRepo;

    public GitRepositoryPlanLoader(String remotePath) {
        this.remotePath = remotePath;
    }

    @Override
    public void load() {
        try {
            File localFolder = Files.createTempDirectory(new File("").toPath(), "").toFile();
            String localPath = localFolder.getAbsolutePath();
            Git git = Git.cloneRepository()
                    .setURI(remotePath)
                    .setDirectory(new File(localPath))
                    .call();


            localRepo = new FileRepository(localPath + "/.git");
            this.git = new Git(localRepo);

            this.git.pull().call();
            this.git.close();

            git.reset();
            git.close();

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
            git = new Git(localRepo);
            PullResult pull = git.pull().call();
            if (pull.getMergeResult().getMergeStatus().equals(MergeResult.MergeStatus.FAST_FORWARD)) {
                folderPlanLoader.reload();
            }
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Plan> getPlans() {
        return folderPlanLoader.getPlans();
    }
}
