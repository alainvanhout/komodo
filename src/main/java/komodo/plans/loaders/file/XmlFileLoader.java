package komodo.plans.loaders.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.NamedMapConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import komodo.actions.Action;
import komodo.actions.State;
import komodo.plans.Plan;
import komodo.plans.loaders.PlanLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class XmlFileLoader implements PlanLoader {

    private File file;
    private Plan plan;

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
            xstream.alias("plan", Plan.class);
            xstream.alias("config", java.util.LinkedHashMap.class);

            NamedMapConverter namedMapConverter = new NamedMapConverter(xstream.getMapper(), "item", "key", String.class, "value", String.class, true, true, xstream.getConverterLookup());
            xstream.registerConverter(namedMapConverter);

            plan =  (Plan)xstream.fromXML(input);;
            if (StringUtils.isBlank(plan.getName())) {
                plan.setName(StringUtils.substringBeforeLast(file.getName(), ".xml"));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not access file: " + file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Override
    public void reload() {
        plan = null;
        load();
    }

    @Override
    public List<Plan> getPlans() {
        return Arrays.asList(plan);
    }

    public static boolean canLoad(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }
}
