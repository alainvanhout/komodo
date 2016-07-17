package komodo.actions.runners;

public interface Runners {
    // check runners
    String AND_CHECK_RUNNER = "and-check-runner";
    // success/failure runners
    String SUCCESS_RUNNER = "success-runner";
    String FAILURE_RUNNER = "failure-runner";
    // real runners
    String PRINT_TO_CONSOLE = "print-to-console";
    String SEND_MAIL = "send-mail";
    String URL_CHECK = "url-check";
    String GIT_PULL_ALL = "git-pull-all";
    String PING = "ping";
}
