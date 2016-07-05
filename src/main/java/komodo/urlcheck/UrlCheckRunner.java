package komodo.urlcheck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import komodo.domain.Check;
import komodo.domain.Effect;
import komodo.runners.CheckRunner;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class UrlCheckRunner implements CheckRunner<UrlCheck> {

    @Override
    public void run(UrlCheck check) {
        HttpURLConnection connection = null;
        try {
            check.last(LocalDateTime.now());

            connection = (HttpURLConnection) new URL(check.getUrl()).openConnection();
            connection.setRequestMethod("GET");

            addAuthentication(check, connection);

            int responseCode = connection.getResponseCode();
            System.out.println(check.getUrl() + ": " + responseCode + " - " + LocalTime.now());

            check.success(responseCode >= 200 && responseCode < 300);
            if (!check.isSuccess()){
                fail(check);
            }
        } catch (Exception e) {
            fail(check);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void addAuthentication(UrlCheck check, HttpURLConnection connection) {
        if (check.getUsername() != null && check.getPassword() != null) {
            String userCredentials = check.getUsername() + ":" + check.getPassword();
            String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);
        }
    }

    private void fail(UrlCheck check) {
        check.success(false);
        Effect failureEffect = check.getFailureEffect();
        if (failureEffect != null){
            failureEffect.run(check);
        }
    }

    @Override
    public boolean shouldRun(UrlCheck check) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(check.getLast(), now).toMillis() >= check.getInterval() * 1000 - 100;
    }

    @Override
    public boolean supports(Check check) {
        return check instanceof UrlCheck;
    }


}
