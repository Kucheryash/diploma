package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.entities.User;
import project.services.CompetitorsService;
import project.services.UserService;

@Controller
public class CompetitorsController {
    @Autowired
    CompetitorsService competitorsService;
    @Autowired
    UserService userService;

    @GetMapping("/market")
    public String swot(){
        return "market";
    }

    @GetMapping("/market/{id}")
    public String swot(Model model, @PathVariable("id") long id_user){
        User user = userService.get(id_user);
        model.addAttribute("user", user);
        return "market";
    }

}
