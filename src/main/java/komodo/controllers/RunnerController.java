
package komodo.controllers;

import komodo.actions.runners.ActionRunner;
import komodo.services.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RunnerController {

    @Autowired
    private RunnerService runnerService;

    @RequestMapping(value = "runners", produces = "application/json")
    public Map<String, ActionRunner> namedActions() {
        return runnerService.getRunners();
    }
}
