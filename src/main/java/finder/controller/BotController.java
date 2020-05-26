package finder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BotController {


    @RequestMapping("https://findjobbot.herokuapp.com/")
    public String getMain(){
        return "main";
    }

    @RequestMapping("/")
    public String start(){
        return "start";
    }

}
