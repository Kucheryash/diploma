package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.entities.Company;
import project.entities.Competitiveness;
import project.entities.SWOT;
import project.entities.StrategicPlan;
import project.services.*;

import java.sql.Date;

@Controller
public class SpecialistController {
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
    @Autowired
    ForecastService forecastService;

    @GetMapping("/edit-comp-spec/{idu}/{idc}")
    public String editCompSpec(@PathVariable("idu") long id_user, @PathVariable("idc") long id_company, Model model){
        Company company = companyService.get(id_company);

        Competitiveness competitiveness = competitivenessService.findByCompany(company);
        SWOT swot = swotService.findByCompany(company);
        StrategicPlan plan = planService.findByCompany(company);

        model.addAttribute("user", userService.get(id_user));
        model.addAttribute("company", company);
        model.addAttribute("competitivenessList", competitiveness);
        model.addAttribute("swot", swot);
        model.addAttribute("plan", plan);
        return "edit-comp-spec";
    }

    @PostMapping("/update-comp-spec/{idu}/{idc}")
    public String updateCompSpec(@PathVariable("idu") long id_user, @PathVariable("idc") long id_company, @ModelAttribute("swot") SWOT newSwot, @ModelAttribute("plan") StrategicPlan newPlan){
        Date date = Date.valueOf(java.time.LocalDate.now());
        Company company = companyService.get(id_company);

        SWOT swot = swotService.findByCompany(company);
        swot.setStrengths(newSwot.getStrengths());
        swot.setWeaknesses(newSwot.getWeaknesses());
        swot.setOpportunities(newSwot.getOpportunities());
        swot.setThreats(newSwot.getThreats());
        swot.setStatus("изменено");
        swot.setDate(date);
        swotService.save(swot);

        StrategicPlan plan = planService.findByCompany(company);
        plan.setDescription(newPlan.getDescription());
        plan.setStatus("изменено");
        plan.setDate(date);
        planService.save(plan);
        return "redirect:/specialist/"+id_user;
    }

}
