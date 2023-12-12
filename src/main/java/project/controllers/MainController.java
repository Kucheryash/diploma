package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.entity.Company;
import project.entity.User;
import project.entity.enums.Role;
import project.service.CompanyService;
import project.service.UserService;

import java.util.Collections;

@Controller
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/home/{id}")
    public String loginHome(Model model, @PathVariable("id") long id){
        model.addAttribute("user", userService.get(id));
        model.addAttribute("company", companyService.fingByBA(id));
        return "home";
    }

    @GetMapping("/login")
    public String authorization(@ModelAttribute("user") User user){
        return "login";
    }

    @GetMapping("/reg")
    public String register(@ModelAttribute("user") User user, @ModelAttribute("company") Company company){
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") User user, @ModelAttribute("company") Company company){
        user.setRole(Collections.singleton(Role.ROLE_BA));

        String message = userService.newUser(user, company);
        if (message.equals("success")) {
            User newUser = userService.find(user);
            model.addAttribute("user", newUser);
            return "redirect:/home/"+newUser.getId();
        }else if(message.equals("existUser")){
            model.addAttribute("errorEmail", "Пользователь с email '"+user.getEmail()+"' уже существует.");
            return "registration";
        } else {
            model.addAttribute("errorCompany", "Компания с названием '"+company.getName()+"' уже существует.");
            return "registration";
        }
    }

    @GetMapping("/prices")
    public String prices(@ModelAttribute("user") User user){
        return "prices";
    }

    @GetMapping("/prices/{id}")
    public String pricesForUser(Model model, @PathVariable("id") long id){
        model.addAttribute("user", userService.get(id));
        return "prices";
    }

}
