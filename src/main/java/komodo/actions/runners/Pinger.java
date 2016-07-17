package komodo.actions.runners;

import komodo.actions.Action;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Pinger implements ActionRunner {

    public static final String HOST = "host";
    public static final String TIMEOUT = "timeout";

    @Value("${komodo.defaults.pinger.host:localhost}")
    private String defaultHost;

    @Value("${komodo.defaults.urlchecker.timemout:1000}")
    private String defaultTimeout;

    private String pingCommand;

    @Override
    public String getId() {
        return Runners.PING;
    }

    @PostConstruct
    private void init() {
        boolean isWindows = System.getProperty("os.name").startsWith("Windows");
        pingCommand = "ping -" + (isWindows ? "n" : "c") + " 1 ";
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> map = new HashMap();
        map.put(HOST, "The host name to be checked");
        map.put(TIMEOUT, "Connection timeout in milliseconds (default: " + defaultTimeout + ")");
        return map;
    }

    @Override
    public Boolean run(Action action) {
        String host = action.get(HOST);
        int timeout = Integer.parseInt(action.get(TIMEOUT, defaultTimeout));

        boolean reachable = ping(host, timeout);
        action.getState().setSuccessful(reachable);

        return reachable;
    }

    private boolean ping(String host, int timeout) {
        try {
            Process myProcess = Runtime.getRuntime().exec(pingCommand + host);
            myProcess.waitFor(timeout, TimeUnit.MILLISECONDS);
            return (myProcess.exitValue() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}