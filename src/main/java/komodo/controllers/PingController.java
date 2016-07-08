package komodo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @RequestMapping("ping")
    public String ping(){
        return "ok";
    }

    @RequestMapping("ping2")
    public String ping2(){
        return "ok";
    }
    @RequestMapping("ping3")
    public String ping3(){
        return "ok";
    }
}
