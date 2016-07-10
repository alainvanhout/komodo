package komodo.actions.runners;

import komodo.actions.Action;
import komodo.actions.Context;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalTime;

@Component
public class MailSender implements ActionRunner {

    @Override
    public String getId() {
        return Runners.SEND_MAIL;
    }

    @Override
    public Boolean run(Action action) {
        try {
            String host = action.get("host");
            String to = action.get("to");
            String subject = action.get("subject");
            String text = action.get("text");

            JavaMailSenderImpl sender = createSender(action, host);
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            sender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private JavaMailSenderImpl createSender(Action action, String host) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(Integer.parseInt(action.get("port")));
        return sender;
    }
}
