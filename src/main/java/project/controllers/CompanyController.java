package project.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entity.Company;
import project.entity.CompanyData;
import project.entity.SWOT;
import project.entity.User;
import project.service.CompanyDataService;
import project.service.CompanyService;
import project.service.SWOTService;
import project.service.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
public class CompanyController {
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyDataService companyDataService;
    @Autowired
    UserService userService;
    @Autowired
    SWOTService swotService;

    @GetMapping("/go-to-company-data/{idu}/{idc}")
    public String goToAdd(Model model, @PathVariable("idu") long id_user, @PathVariable("idc") long id_company, @ModelAttribute("companyData") CompanyData companyData){
        Company company = companyService.get(id_company);
        User user = userService.get(id_user);

        model.addAttribute("user", user);
        model.addAttribute("company", company);

        List<String> activities = Arrays.asList(
                "Группа компаний",
                "Дистрибуция АО",
                "Дистрибуция ПО",
                "Дистрибуция АО и ПО",
                "ИТ-услуги",
                "Производство АО",
                "Производство АО и ПО",
                "Разработка АО",
                "Разработка ПО",
                "Разработка АО и ПО"
        );
        model.addAttribute("activities", activities);

        return "add-company-data";
    }

    @PostMapping("/new-company-data/{idu}/{idc}")
    public String addCompanyData(Model model, @PathVariable("idu") long id_user, @PathVariable("idc") long id_company, @ModelAttribute("companyData") CompanyData companyData, @RequestParam("activity") List<String> activities){
        if (activities == null || activities.isEmpty()) {
            model.addAttribute("error", "Выберите хотя бы одну сферу деятельности.");
            return "add-company-data";
        }
        Company company = companyService.get(id_company);
        User user = userService.get(id_user);

        String activityString = String.join(", ", activities);
//        if (activities.contains("Производство АО") && activities.contains("Дистрибуция АО"))
//            activityString = activityString.replaceAll("Производство АО, Дистрибуция АО", "Производство и дистрибуция АО");
//        if (activities.contains("Производство АО и ПО") && activities.contains("Дистрибуция АО и ПО"))
//            activityString = activityString.replaceAll("Производство АО и ПО, Дистрибуция АО и ПО", "Производство и дистрибуция АО и ПО");
//        if (activities.contains("Разработка АО") && activities.contains("Дистрибуция АО"))
//            activityString = activityString.replaceAll("Разработка АО, Дистрибуция АО", "Разработка и дистрибуция АО");
//        if (activities.contains("Разработка ПО") && activities.contains("Дистрибуция ПО"))
//            activityString = activityString.replaceAll("Разработка ПО, Дистрибуция ПО", "Разработка и дистрибуция ПО");
        companyData.setActivity(activityString);

        companyData = companyDataService.fillIn(companyData, company);

        model.addAttribute("user", user);
        model.addAttribute("company", company);
        return "add-success";
    }
}
