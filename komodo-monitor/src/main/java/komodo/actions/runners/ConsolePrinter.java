package komodo.actions.runners;

import komodo.actions.Action;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConsolePrinter implements ActionRunner {

    public static final String MESSAGE = "message";

    @Override
    public String getId() {
        return Runners.PRINT_TO_CONSOLE;
    }

    @Override
    public Map<String, String> getParameters(){
        Map<String, String> map = new HashMap();
        map.put(MESSAGE, "The message to be printed to console");
        return map;
    }

    @Override
    public Boolean run(Action action) {
        System.out.println(LocalTime.now() + " " + action.get(MESSAGE));
        // inherently always succeeds
        return true;
    }
}
