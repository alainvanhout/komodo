package komodo.runners;

import komodo.domain.Check;

public interface CheckRunner<T extends Check> {

    void run(T check);

    boolean shouldRun(T check);

    boolean supports(Check check);
}
