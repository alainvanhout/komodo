package komodo.plans.loaders;

import komodo.actions.Action;
import komodo.plans.Plan;
import komodo.utils.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public interface PlanLoader {

    void reload();

    List<Plan> getPlans();
}
