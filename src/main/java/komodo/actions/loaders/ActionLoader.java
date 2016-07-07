package komodo.actions.loaders;

import komodo.actions.Action;

import java.util.List;

public interface ActionLoader {

    void reload();

    List<Action> getActions();
}
