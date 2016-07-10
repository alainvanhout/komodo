package komodo.actions.runners;

import komodo.actions.Action;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class UrlChecker implements ActionRunner {

    @Override
    public String getId() {
        return Runners.URL_CHECK;
    }

    @Override
    public Boolean run(Action action) {
        String url = action.get("url");

        System.out.println("url:" + url);

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            addAuthentication(connection, action);

            int responseCode = connection.getResponseCode();
//            System.out.println(url + ": " + responseCode + " - " + LocalTime.now());

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
        String username = action.get("username");
        String password = action.get("password");
        if (username != null && password != null) {
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);
        }
    }
}