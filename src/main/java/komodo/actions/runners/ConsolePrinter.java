package komodo.actions.runners;

import komodo.actions.Action;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ConsolePrinter implements ActionRunner {

    @Override
    public String getId() {
        return Runners.PRINT_TO_CONSOLE;
    }

    @Override
    public Boolean run(Action action) {
        System.out.println(LocalTime.now() + " " + action.get("message"));
        // inherently always succeeds
        return true;
    }
}
