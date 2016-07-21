package komodo.plans.loaders;

import komodo.actions.Action;

import java.util.List;

public interface ActionLoader {

    void load();

    void reload();

    List<Action> getActions();
}
