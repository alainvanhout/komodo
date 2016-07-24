package komodo.controllers;

import komodo.services.PlanService;
import komodo.services.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ReloadController {

    @Autowired
    private TimerService timerService;

    @Autowired
    private PlanService planService;

    @RequestMapping("reload")
    public String reload(){
        planService.reload();
        timerService.timer();
        return "redirect:/overview";
    }
}
