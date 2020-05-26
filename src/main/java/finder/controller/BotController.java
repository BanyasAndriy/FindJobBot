package finder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


//this controller was created to  H10 error
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
