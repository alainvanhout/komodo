package komodo.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import komodo.urlcheck.UrlCheck;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailEffect implements Effect {

    private Gson gson;
    private JavaMailSenderImpl sender;
    private Map<String, String> params;

    @PostConstruct
    public void init() throws FileNotFoundException {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        params = toMap(new File("mailParams.json"));

        sender = new JavaMailSenderImpl();
        sender.setHost(params.get("host"));
        sender.setPort(Integer.parseInt(params.get("port")));
    }

    @Override
    public void run(Check check) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(params.get("to"));
            helper.setSubject(params.get("subject"));
            helper.setText(check.getStatus() + " - " + LocalTime.now());
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Map toMap(File file) throws FileNotFoundException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file.getAbsolutePath()));
            Map<String, String> params = gson.fromJson(reader, HashMap.class);
            return params;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

}
