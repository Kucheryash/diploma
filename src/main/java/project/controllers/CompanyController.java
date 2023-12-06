package project.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entity.*;
import project.service.*;

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
    @Autowired
    CompetitivenessService competitivenessService;
    @Autowired
    StrategicPlanService planService;

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
        companyData.setActivity(activityString);

        companyData = companyDataService.fillIn(companyData, company);

        model.addAttribute("user", user);
        model.addAttribute("company", company);
        return "add-success";
    }

    @GetMapping("/edit-comp-spec/{id}")
    public String editCompSpec(@PathVariable("id") long id, Model model){
        Company company = companyService.get(id);

        Competitiveness competitiveness = competitivenessService.findByCompany(company);
        SWOT swot = swotService.findByCompany(company);
        StrategicPlan plan = planService.findByCompany(company);

        model.addAttribute("company", company);
        model.addAttribute("competitivenessList", competitiveness);
        model.addAttribute("swot", swot);
        model.addAttribute("plan", plan);
        return "edit-comp-spec";
    }

    @PostMapping("/update-comp-spec/{id}")
    public String updateCompSpec(@PathVariable("id") long id, @ModelAttribute("swot") SWOT swot, @ModelAttribute("plan") StrategicPlan plan){
        swot.setStrengths(swot.getStrengths());
        swot.setWeaknesses(swot.getWeaknesses());
        swot.setOpportunities(swot.getOpportunities());
        swot.setThreats(swot.getThreats());
        swot.setStatus("изменено");
        swotService.save(swot);

        plan.setDescription(plan.getDescription());
        plan.setStatus("изменено");
        planService.save(plan);
        return "redirect:/specialist";
    }
}
