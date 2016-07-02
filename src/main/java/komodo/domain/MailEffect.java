package komodo.domain;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalTime;

@Component
public class MailEffect implements Effect {

    private JavaMailSenderImpl sender;

    @PostConstruct
    public void init() {
        sender = new JavaMailSenderImpl();
        sender.setHost("localhost");
        sender.setPort(25);
    }

    @Override
    public void run(Check check) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo("test@localhost");
            helper.setText(check.getStatus() + " - " + LocalTime.now());
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
