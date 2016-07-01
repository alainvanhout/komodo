package komodo.controllers;

import komodo.services.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReloadController {

    @Autowired
    private TimerService timerService;

    @RequestMapping("reload")
    public void reload(){
        timerService.reload();
    }
}
