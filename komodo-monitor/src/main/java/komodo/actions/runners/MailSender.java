package komodo.actions.runners;

import komodo.actions.Action;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailSender implements ActionRunner {

    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String TO = "to";
    public static final String SUBJECT = "subject";
    public static final String TEXT = "text";

    @Value("${komodo.defaults.mailsender.host:localhost}")
    private String defaultHost;

    @Value("${komodo.defaults.mailsender.port:#{null}}")
    private String defaultPort;

    @Value("${komodo.defaults.mailsender.to:#{null}}")
    private String defaultTo;

    @Value("${komodo.defaults.mailsender.subject:#{null}}")
    private String defaultSubject;

    @Value("${komodo.defaults.mailsender.text:#{null}}")
    private String defaultText;

    @Override
    public String getId() {
        return Runners.SEND_MAIL;
    }

    @Override
    public Map<String, String> getParameters(){
        Map<String, String> map = new HashMap();
        map.put(HOST, "The mail server host (default:" + defaultHost + ")");
        map.put(PORT, "The mail server port (default:" + defaultPort + ")");
        map.put(TO, "A comma-separated list of email addresses (default:" + defaultTo + ")");;
        map.put(SUBJECT, "The e-mail subject (default:" + defaultSubject + ")");
        map.put(TEXT, "The e-mail body text (default:" + defaultText + ")");
        return map;
    }

    @Override
    public Boolean run(Action action) {
        try {
            String to = action.get(TO, defaultTo);
            String subject = action.get(SUBJECT, defaultSubject);
            String text = action.get(TEXT, defaultText);

            JavaMailSenderImpl sender = createSender(action);
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            sender.send(message);

            action.getState().setSuccessful(true);
            return true;
        } catch (Exception e) {
            action.getState().setSuccessful(false);
            return false;
        }
    }

    private JavaMailSenderImpl createSender(Action action) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(action.get(HOST, defaultHost));
        sender.setPort(Integer.parseInt(action.get(PORT)));
        return sender;
    }
}
