package komodo.actions.runners;

import komodo.actions.Action;
import komodo.actions.CombinedAction;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class AndCheckRunner implements ActionRunner {

    @Autowired
    private RunnerService runnerService;

    @Override
    public String getId() {
        return Runners.AND_CHECK_RUNNER;
    }


    @Override
    public Map<String, String> getParameters() {
        return Collections.emptyMap();
    }

    @Override
    public Boolean run(Action action) {
        boolean async = Boolean.parseBoolean(action.get("async", "false"));
        return getStream(action.getCheck(), async)
                .map((a) -> runnerService.run(new CombinedAction(a, action), null))
                .reduce((a, b) -> a && b)
                .orElse(false);
    }

    private Stream<Action> getStream(List<Action> actions, boolean async) {
        if (async) {
            return actions.parallelStream();
        }
        return actions.stream();
    }
}
