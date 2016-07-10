package komodo.actions.runners;

import komodo.actions.Action;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailSender implements ActionRunner {

    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String TO = "to";
    public static final String SUBJECT = "subject";
    public static final String TEXT = "text";

    @Override
    public String getId() {
        return Runners.SEND_MAIL;
    }

    @Override
    public Map<String, String> getParameters(){
        Map<String, String> map = new HashMap();
        map.put(HOST, "The mail server host");
        map.put(PORT, "The mail server port");
        map.put(TO, "A comma-separated list of email addresses");
        map.put(SUBJECT, "The e-mail subject");
        map.put(TEXT, "The e-mail body text");
        return map;
    }

    @Override
    public Boolean run(Action action) {
        try {
            String to = action.get(TO);
            String subject = action.get(SUBJECT);
            String text = action.get(TEXT);

            JavaMailSenderImpl sender = createSender(action);
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

    private JavaMailSenderImpl createSender(Action action) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(action.get(HOST));
        sender.setPort(Integer.parseInt(action.get(PORT)));
        return sender;
    }
}
