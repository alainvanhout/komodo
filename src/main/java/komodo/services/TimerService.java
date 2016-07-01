package komodo.services;

import komodo.domain.Check;
import komodo.domain.UrlCheck;
import komodo.loaders.CheckLoader;
import komodo.runners.CheckRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public class TimerService {

    private List<Check> checks;
    private Map<CheckRunner, List<Check>> map;

    @Autowired
    private Collection<CheckRunner> checkRunners;

    @Autowired
    private Collection<CheckLoader> checkLoaders;

    @PostConstruct
    public void reload() {
        checks = new ArrayList<>();
        map = new HashMap<>();

        for (CheckLoader checkLoader : checkLoaders) {
            checks.addAll(checkLoader.load());
        }
        checkRunners.forEach(r -> map.put(r, new ArrayList<>()));

        for (Check check : this.checks) {
            for (CheckRunner checkRunner : checkRunners) {
                List<Check> runnerChecks = map.get(checkRunner);
                if (checkRunner.supports(check)) {
                    runnerChecks.add(check);
                    break;
                }
            }
        }
    }

    @Scheduled(fixedRate = 100)
    public void timer() throws IOException {
        map.entrySet().parallelStream()
                .forEach(entry -> entry.getValue().stream()
                        .filter(entry.getKey()::shouldRun)
                        .forEach(entry.getKey()::run));
    }
}
