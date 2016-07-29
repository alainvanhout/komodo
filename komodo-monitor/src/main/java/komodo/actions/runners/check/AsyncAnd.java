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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class AsyncAnd implements ActionRunner {

    @Autowired
    private RunnerService runnerService;

    @Override
    public String getId() {
        return Runners.ASYNC_AND;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> map = new HashMap();
        return map;
    }

    @Override
    public Boolean run(Action action) {
        List<Action> check = action.getCheck();
        CompletableFuture[] futures = check.stream()
                .map((a) -> CompletableFuture.supplyAsync(() -> runnerService.run(new CombinedAction(a, action), null)))
                .collect(Collectors.toList()).toArray(new CompletableFuture[]{});
        CompletableFuture.allOf(futures);

        Boolean result = check.stream()
                .map((a) -> a.getState().getSuccessful())
                .reduce(true, (a, b) -> a && b);

        action.getState().setSuccessful(result);
        return result;
    }
}
