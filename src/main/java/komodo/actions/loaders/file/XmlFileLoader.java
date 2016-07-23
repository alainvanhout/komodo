package komodo.actions.loaders.file;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.NamedMapConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import komodo.actions.Action;
import komodo.actions.loaders.ActionLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class XmlFileLoader implements ActionLoader {

    private File file;
    private Action action;

    public XmlFileLoader(File file) {
        this.file = file;
    }

    @Override
    public void load() {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }
        FileInputStream input = null;


        try {
            input = new FileInputStream(file);

            XStream xstream = new XStream(new PureJavaReflectionProvider());
            xstream.ignoreUnknownElements();
            xstream.alias("action", Action.class);
            xstream.alias("config", java.util.LinkedHashMap.class);

            NamedMapConverter namedMapConverter = new NamedMapConverter(xstream.getMapper(), "item", "key", String.class, "value", String.class, true, true, xstream.getConverterLookup());
            xstream.registerConverter(namedMapConverter);

            action = (Action) xstream.fromXML(input);
            ;
            if (StringUtils.isBlank(action.getName())) {
                action.setName(StringUtils.substringBeforeLast(file.getName(), ".xml"));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not access file: " + file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Override
    public void reload() {
        action = null;
        load();
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(action);
    }

    public static boolean canLoad(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }
}
