
package komodo.controllers;

import komodo.actions.runners.ActionRunner;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class RunnerController {

    @Autowired
    private RunnerService runnerService;

    @RequestMapping(value = "runners", produces = "application/json")
    public Collection<ActionRunner> runners() {
        return runnerService.getRunners().values().stream()
                .sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .collect(Collectors.toList());
    }
}
