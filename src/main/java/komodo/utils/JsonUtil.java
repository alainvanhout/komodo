package komodo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonUtil {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static <T> T toObject(String input, Class<T> type) {
        return gson.fromJson(input, type);
    }

    public static <T> T toObject(File inputFile, Class<T> type) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
            String input = IOUtils.toString(inputStream);
            return toObject(input, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static <T> List<T> toList(String s) {
        Type listType = new TypeToken<ArrayList<T>>() {
        }.getType();
        return gson.fromJson(s, listType);
    }
}
