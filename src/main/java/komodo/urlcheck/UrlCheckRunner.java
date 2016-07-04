package komodo.urlcheck;

import komodo.domain.Check;
import komodo.domain.Effect;
import komodo.runners.CheckRunner;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

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
            connection = (HttpURLConnection) new URL(check.getUrl()).openConnection();
            connection.setRequestMethod("GET");

            if (check.getUsername() != null && check.getPassword() != null) {
                String userCredentials = check.getUsername() + ":" + check.getPassword();
                String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
                connection.setRequestProperty("Authorization", basicAuth);
            }

            int responseCode = connection.getResponseCode();
            check.last(LocalDateTime.now());

            check.success(true);

            System.out.println(check.getUrl() + ": " + responseCode + " - " + LocalTime.now());
        } catch (Exception e) {
            check.success(false);
            Effect failureEffect = check.getFailureEffect();
            if (failureEffect != null){
                failureEffect.run(check);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
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
