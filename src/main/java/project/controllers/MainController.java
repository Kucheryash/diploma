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
import project.repository.CompanyRepository;
import project.repository.UserRepository;
import project.service.UserService;

import java.util.Optional;

@Controller
public class MainController {
//    #2C3341
//    #4F5D75
//    #CBCDCD
//    #D4B5A7
//    #EC6A32
    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/home/{id}")
    public String loginHome(Model model, @PathVariable("id") long id){
        model.addAttribute("user", userService.get(id));
        return "home";
    }

    @GetMapping("/login")
    public String authorization(@ModelAttribute("user") User user){
        return "login";
    }

    @PostMapping("/singin")
    public String login(Model model, @ModelAttribute("user") User user){
        Optional<User> u = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if(u.isEmpty()){
            model.addAttribute("errorMessage", "Данного пользователя не существует! Проверьте корректность введённых данных.");
            return "redirect:/login";
        }
        return "redirect:/home/"+u.get().getId();
    }

    @GetMapping("/reg")
    public String register(@ModelAttribute("user") User user, @ModelAttribute("company") Company company){
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") User user, @ModelAttribute("company") Company company){
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

}
