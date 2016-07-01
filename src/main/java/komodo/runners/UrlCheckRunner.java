package komodo.runners;

import komodo.domain.Check;
import komodo.domain.UrlCheck;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class UrlCheckRunner implements CheckRunner<UrlCheck> {

    private JavaMailSenderImpl sender;

    @PostConstruct
    public void init() {
        sender = new JavaMailSenderImpl();
        sender.setHost("localhost");
        sender.setPort(25);
    }

    @Override
    public void run(UrlCheck check) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(check.getUrl()).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            check.last(LocalDateTime.now());

            System.out.println(check.getUrl() + ": " + responseCode + " - " + LocalTime.now());
        } catch (Exception e) {
            sendMail(check);
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

    private void sendMail(UrlCheck check) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo("test@localhost");
            helper.setText("Failure at " + check.getUrl() + " - " + LocalTime.now());
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
