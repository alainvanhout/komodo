package komodo.actions.runners.check;

import komodo.actions.Action;
import komodo.actions.CombinedAction;
import komodo.actions.runners.ActionRunner;
import komodo.actions.runners.Runners;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LazyAnd implements ActionRunner {

    @Autowired
    private RunnerService runnerService;


    @Override
    public String getId() {
        return Runners.LAZY_AND;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> map = new HashMap();
        return map;
    }

    @Override
    public Boolean run(Action action) {
        List<Action> actions = action.getCheck();
        for (Action a : actions) {
            if (runnerService.run(new CombinedAction(a, action), null) == false) {
                action.getState().setSuccessful(false);
                return false;
            }
        }

        action.getState().setSuccessful(true);
        return true;
    }
}
