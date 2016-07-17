package komodo.actions.runners;

import komodo.actions.Action;
import komodo.actions.CombinedAction;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AndCheckRunner implements ActionRunner {

    public static final String ASYNC = "async";
    public static final String ASYNC_DEFAULT = "async";

    @Autowired
    private RunnerService runnerService;

    @Value("${komodo.defaults.andcheck.ansyc:true}")
    private String defaultAsync;

    @Override
    public String getId() {
        return Runners.AND_CHECK_RUNNER;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> map = new HashMap();
        map.put(ASYNC, "Whether the checks, successes and failures should be run asynchronously (default:" + ASYNC_DEFAULT + ")");
        ;
        return map;
    }

    @Override
    public Boolean run(Action action) {
        boolean async = Boolean.parseBoolean(action.get("async", defaultAsync));
        List<Action> actions = action.getCheck();

        if (async){
            return runAsync(action);
        } else {
            return getStream(actions, async)
                    .map((a) -> runnerService.run(new CombinedAction(a, action), null))
                    .reduce((a, b) -> a && b)
                    .orElse(false);
        }
    }

    private Boolean runAsync(Action action) {
        List<Action> check = action.getCheck();
        CompletableFuture[] futures = check.stream()
                .map((a) -> CompletableFuture.supplyAsync(() -> runnerService.run(new CombinedAction(a, action), null)))
                .collect(Collectors.toList()).toArray(new CompletableFuture[]{});
        CompletableFuture.allOf(futures);

        return check.stream()
                .map((a) ->  a.getState().getSuccessful())
                .reduce((a, b) -> a && b).orElse(false);
    }

    private Stream<Action> getStream(List<Action> actions, boolean async) {
        if (async) {
            return actions.parallelStream();
        }
        return actions.stream();
    }
}
