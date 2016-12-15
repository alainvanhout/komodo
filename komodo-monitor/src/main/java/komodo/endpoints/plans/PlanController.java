
package komodo.endpoints.plans;

import komodo.actions.Action;
import komodo.actions.State;
import komodo.plans.Plan;
import komodo.services.PlanService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class PlanController {

    @Autowired
    private PlanService planService;

    @RequestMapping(value = "plans", produces = "application/json")
    public Collection<PlanResponse> findAll() {
        return planService.getPlans().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PlanResponse toResponse(Plan plan) {
        Action action = plan.getAction();
        State state = action.getState();
        PlanResponse response = new PlanResponse(
                action.getName(),
                action.isActive(),
                state.getSuccessful(),
                plan.getInterval(),
                state.getLast() != null ? state.getLast().format(DateTimeFormatter.ISO_DATE_TIME) : null
        );
        response.getConfig().putAll(action.getConfig());
        return response;
    }

    @RequestMapping(value = "plans/{name}", produces = "application/json")
    public PlanResponse find(@PathVariable("name") String name) {
        return planService.getPlans().stream()
                .filter(plan -> StringUtils.equals(plan.getAction().getName(), name))
                .map(this::toResponse)
                .findFirst().orElse(null);
    }
}
