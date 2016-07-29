package komodo.actions.runners;

import komodo.actions.Action;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class Time implements ActionRunner {

    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public String getId() {
        return Runners.TIME;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> map = new HashMap();
        map.put(BEFORE, "Time should be before (ignored when value is not set)");
        map.put(AFTER, "Time should be after (ignored when value is not set)");
        return map;
    }

    @Override
    public Boolean run(Action action) {
        String before = action.get(BEFORE);
        String after = action.get(AFTER);

        action.getState().setSuccessful(false);

        try {
            if (StringUtils.isNotBlank(before)) {
                LocalTime time = LocalTime.parse(before, FORMATTER);
                if (!LocalTime.now().isBefore(time)) {
                    return false;
                }
            }

            if (StringUtils.isNotBlank(after)) {
                LocalTime time = LocalTime.parse(after, FORMATTER);
                if (!LocalTime.now().isAfter(time)) {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        action.getState().setSuccessful(true);
        return true;
    }

}