package komodo.actions.runners;

import komodo.actions.Action;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class AndCheckRunner implements ActionRunner {

    @Autowired
    private RunnerService runnerService;

    @Override
    public String getId() {
        return  Runners.AND_CHECK_RUNNER;
    }

    @Override
    public Boolean run(Action action) {
        List<Action> actions = action.getCheck();
        return actions.stream()
                .map(runnerService::run)
                .reduce((a, b) -> a && b)
                .orElse(false);
    }
}
