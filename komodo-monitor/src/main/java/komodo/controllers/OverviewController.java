package komodo.controllers;

import komodo.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OverviewController {

    @Autowired
    private PlanService planService;

    @RequestMapping("overview")
    public String overview(Model model){
        model.addAttribute("plans", planService.getPlans());
        return "overview";
    }
}
