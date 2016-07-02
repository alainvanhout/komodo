package komodo.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import komodo.domain.Check;
import komodo.domain.MailEffect;
import komodo.domain.UrlCheck;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UrlCheckLoader implements CheckLoader {

    private Gson gson;

    @Autowired
    private MailEffect mailEffect;

    @PostConstruct
    private void init() {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    @Override
    public List<Check> load() {
        List<Check> checks = new ArrayList<>();

        try {
            Files.walk(Paths.get("checks/")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        checks.add(toCheck(filePath.toFile()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return checks;
    }

    private Check toCheck(File file) throws FileNotFoundException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file.getAbsolutePath()));
            UrlCheck urlCheck = gson.fromJson(reader, UrlCheck.class);
            urlCheck.last(LocalDateTime.now());
            urlCheck.failureEffect(mailEffect);
            return urlCheck;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
}
