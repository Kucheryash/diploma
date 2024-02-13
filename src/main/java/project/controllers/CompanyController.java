package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entities.*;
import project.services.*;

import java.sql.Date;
import java.util.ArrayList;
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
    @Autowired
    ForecastService forecastService;

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

    @GetMapping("/forecast/{id}")
    public String forecast(Model model, @PathVariable("id") long id_company){
        Company company = companyService.get(id_company);
        User user = company.getUser();

        CompanyData companyData = companyDataService.findByCompanyId(id_company);
        if (companyData == null)
            return "redirect:/analysis/"+user.getId()+"/"+id_company;

        Competitiveness competitiveness = competitivenessService.findByCompany(company);
        ForecastData forecastData = forecastService.findByCompanyData(companyData);
        List<Double> forecastRevComp = new ArrayList<>();
        List<Double> forecastRevMarket = new ArrayList<>();
        List<Double> forecastMarketShare = new ArrayList<>();
        if (forecastData == null) {
            forecastRevComp = forecastService.makeForecastRevCompany(competitiveness);
            forecastRevMarket = forecastService.makeForecastRevMarket(companyData);
            forecastMarketShare = forecastService.makeForecastMarketShare(competitiveness, companyData);
            forecastService.createForecast(forecastRevComp, forecastRevMarket, forecastMarketShare, companyData);
        } else {
            String[] compRevArr = forecastData.getCompRevenue23().split(",");
            for (String s : compRevArr) {
                forecastRevComp.add(Double.parseDouble(s));
            }

            String[] marketRevArr = forecastData.getMarketRevenue23().split(",");
            for (String s : marketRevArr) {
                forecastRevMarket.add(Double.parseDouble(s));
            }

            String[] marketShareArr = forecastData.getMarketShare23().split(",");
            for (String s : marketShareArr) {
                forecastMarketShare.add(Double.parseDouble(s));
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("company", company);
        model.addAttribute("competitivenessList", competitiveness);
        model.addAttribute("forecastRevComp", forecastRevComp);
        model.addAttribute("forecastRevMarket", forecastRevMarket);
        model.addAttribute("forecastMarketShare", forecastMarketShare);
        return "forecast";
    }
}
