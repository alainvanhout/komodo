package komodo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonUtil {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static <T> T toObject(String s, Class<T> type){
        return gson.fromJson(s, type);
    }

    public static <T> List<T> toList(String s){
        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        return gson.fromJson(s, listType);
    }
}
