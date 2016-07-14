package komodo.actions.runners;

import komodo.actions.Action;
import komodo.actions.CombinedAction;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AndCheckRunner implements ActionRunner {

    @Autowired
    private RunnerService runnerService;

    @Override
    public String getId() {
        return  Runners.AND_CHECK_RUNNER;
    }


    @Override
    public Map<String, String> getParameters(){
        return Collections.emptyMap();
    }

    @Override
    public Boolean run(Action action) {
        List<Action> actions = action.getCheck();
        return actions.stream()
                .map((a) -> runnerService.run(new CombinedAction(a, action), null))
                .reduce((a, b) -> a && b)
                .orElse(false);
    }
}
