package komodo.actions.runners;

import komodo.actions.Action;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ConsolePrinter implements ActionRunner {

    @Override
    public String getId() {
        return "print-to-console";
    }

    @Override
    public boolean run(Action action) {
        System.out.println(LocalTime.now() + " " + action.get("message"));
        return true;
    }
}
