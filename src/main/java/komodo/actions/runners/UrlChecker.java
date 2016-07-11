package komodo.actions.runners;

import komodo.actions.Action;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class UrlChecker implements ActionRunner {

    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String METHOD = "method";
    public static final String METHOD_DEFAULT = "GET";
    public static final String TIMEOUT = "timeout";
    public static final String TIMEOUT_DEFAULT = "1000";

    @Override
    public String getId() {
        return Runners.URL_CHECK;
    }

    @Override
    public Map<String, String> getParameters(){
        Map<String, String> map = new HashMap();
        map.put(URL, "The url to be checked");
        map.put(METHOD, "HTTP method/verb (default: " + METHOD_DEFAULT + ")");
        map.put(TIMEOUT, "Connection timeout(default: " + TIMEOUT_DEFAULT + ")");
        map.put(USERNAME, "Basic authentication username (optional)");
        map.put(PASSWORD, "Basic authentication password (optional)");
        return map;
    }

    @Override
    public Boolean run(Action action) {
        String url = action.get(URL);
        String method = action.get(METHOD, METHOD_DEFAULT);
        int timeout = Integer.parseInt(action.get(TIMEOUT, TIMEOUT_DEFAULT));

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeout);

            addAuthentication(connection, action);
            int responseCode = connection.getResponseCode();
            action.getState().setSuccessful(responseCode >= 200 && responseCode < 300);
        } catch (Exception e) {
            action.getState().setSuccessful(false);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return action.getState().getSuccessful();
    }

    private void addAuthentication(HttpURLConnection connection, Action action) {
        String username = action.get(USERNAME);
        String password = action.get(PASSWORD);
        if (username != null && password != null) {
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);
        }
    }
}