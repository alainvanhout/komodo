package komodo.runners;

import komodo.domain.Check;
import komodo.domain.UrlCheck;

public interface CheckRunner<T extends Check> {

    void run(T check);

    boolean shouldRun(T check);

    boolean supports(Check check);
}
