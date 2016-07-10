package komodo.actions.runners;

import komodo.actions.Action;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SuccessRunner implements ActionRunner {

    @Autowired
    private RunnerService runnerService;

    @Override
    public String getId() {
        return Runners.SUCCESS_RUNNER;
    }

    @Override
    public Boolean run(Action action) {
        action.getSuccess().forEach(runnerService::run);
        // should not trigger success or failure
        return null;
    }
}
