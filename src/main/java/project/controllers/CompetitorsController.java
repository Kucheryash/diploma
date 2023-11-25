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
public class CompetitorsController {
    @Autowired
    CompetitorsService competitorsService;
    @Autowired
    UserService userService;

    @GetMapping("/market")
    public String swot(Model model){
//        List<Market> competitors = competitorsService.getListFromExcel();
//        model.addAttribute("competitors", competitors);
        return "market";
    }

    @GetMapping("/market/{id}")
    public String swot(Model model, @PathVariable("id") long id_user){
//        List<Market> competitors = competitorsService.getListFromExcel();
        User user = userService.get(id_user);
        model.addAttribute("user", user);
//        model.addAttribute("competitors", competitors);
        return "market";
    }

}
