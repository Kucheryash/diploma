package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import project.entity.*;
import project.service.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyDataService dataService;
    @Autowired
    UserService userService;
    @Autowired
    SWOTService swotService;
    @Autowired
    CompetitivenessService competitivenessService;

    @GetMapping("/account/{id}")
    public String account(Model model, @PathVariable("id") long id_user, @ModelAttribute("companyData") CompanyData companyData){
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        String greeting;
        if (hour >= 6 && hour < 11)
            greeting = "Доброе утро";
        else if (hour >= 11 && hour < 17)
            greeting = "Добрый день";
        else if (hour >= 17 && hour < 22)
            greeting = "Добрый вечер";
        else
            greeting = "Доброй ночи";
        model.addAttribute("greeting", greeting);

        User user = userService.get(id_user);
        Company company = companyService.fingByBA(id_user);
        CompanyData data = dataService.find(company.getId());
        model.addAttribute("user", user);
        model.addAttribute("company", company);
        if (data != null) {
            model.addAttribute("companyData", data);
//            SWOT swot = swotService.findByCompany(company);
            SWOT swot = swotService.SWOTAnalysis(companyData.getRevenue22(), companyData.getEmployees22(), companyData.getCompany());
            model.addAttribute("swot", swot);
            Competitiveness competitiveness = competitivenessService.findByCompany(company);
            model.addAttribute("competitiveness", competitiveness);
        }
        return "account";
    }

}
