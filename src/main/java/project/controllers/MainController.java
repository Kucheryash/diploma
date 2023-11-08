package project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(){
        return "home";
    }

//    #2C3341
//    #4F5D75
//    #CBCDCD
//    #D4B5A7
//    #EC6A32


}
