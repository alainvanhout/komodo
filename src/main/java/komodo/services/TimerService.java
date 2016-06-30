package komodo.services;

import komodo.domain.Endpoint;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimerService {

    private List<Endpoint> endpoints = new ArrayList<>();
    private JavaMailSenderImpl sender;

    @PostConstruct
    private void init() {
        sender = new JavaMailSenderImpl();
        sender.setHost("localhost");
        sender.setPort(25);

        endpoints.add(new Endpoint("http://localhost:8080/ping"));
        endpoints.add(new Endpoint("http://localhost:8081/ping").interval(10));
        endpoints.add(new Endpoint("http://localhost:8080/ping?fast").interval(5));
    }

    @Scheduled(fixedRate = 10000)
    public void timer() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        for (Endpoint endpoint : endpoints) {
            if (Duration.between(endpoint.getLast(), now).getSeconds() > endpoint.getInterval()) {
                performRequest(endpoint);
                endpoint.last(now);
            }
        }
    }

    private void performRequest(Endpoint endpoint) throws IOException {

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(endpoint.getUrl()).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            System.out.println(endpoint.getUrl() + ": " + responseCode);
        } catch (Exception e) {
            sendMail(endpoint);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void sendMail(Endpoint endpoint) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo("test@localhost");
            helper.setText("Failure at " + endpoint.getUrl());
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
