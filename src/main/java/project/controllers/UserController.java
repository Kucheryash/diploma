package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.entity.User;
import project.service.CompetitorsService;
import project.service.UserService;

@Controller
public class UserController {
    @Autowired
    CompetitorsService competitorsService;
    @Autowired
    UserService userService;

    @GetMapping("/account/{id}")
    public String swot(Model model, @PathVariable("id") long id_user){
        User user = userService.get(id_user);
        model.addAttribute("user", user);
        return "account";
    }

}
