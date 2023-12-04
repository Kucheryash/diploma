package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entity.*;
import project.service.*;

import java.time.LocalTime;
import java.util.ArrayList;
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
    @Autowired
    StrategicPlanService planService;
    @Autowired
    ForecastService forecastService;

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
        CompanyData data = dataService.findByCompanyId(company.getId());
        Competitiveness competitiveness = competitivenessService.findByCompany(company);
        model.addAttribute("user", user);
        model.addAttribute("company", company);
        if (data != null && competitiveness != null) {
            SWOT swot = swotService.findByCompany(company);
            ForecastData forecastData = forecastService.findByCompanyData(companyData);
            List<Double> forecastRevComp = new ArrayList<>();
            List<Double> forecastRevMarket = new ArrayList<>();
            List<Double> forecastMarketShare = new ArrayList<>();
            if (forecastData == null) {
                forecastRevComp = forecastService.makeForecastRevCompany(competitiveness);
                forecastRevMarket = forecastService.makeForecastRevMarket();
                forecastMarketShare = forecastService.makeForecastMarketShare(competitiveness);
                forecastService.createForecast(forecastRevComp, forecastRevMarket, forecastMarketShare, companyData);
            } else{
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
            StrategicPlan plan = planService.findByCompany(company);
            model.addAttribute("analysis", "Погнали");
            model.addAttribute("companyData", data);
            model.addAttribute("swot", swot);
            model.addAttribute("competitivenessList", competitiveness);
            model.addAttribute("forecastRevComp", forecastRevComp);
            model.addAttribute("forecastRevMarket", forecastRevMarket);
            model.addAttribute("forecastMarketShare", forecastMarketShare);
            model.addAttribute("plan", plan);
        }
        return "account";
    }

    @PostMapping("/send-email/{id}")
    public String email(Model model, @PathVariable("id") Long id) {
        User user = userService.get(id);
        Company company = companyService.fingByBA(id);
        CompanyData companyData = dataService.findByCompanyId(company.getId());
        SWOT swot = swotService.SWOTAnalysis(companyData.getRevenue22(), companyData.getEmployees22(), companyData.getCompany());
        Competitiveness competitiveness = competitivenessService.findByCompany(company);

        //ДОБАВИТЬ РЕАЛИЗАЦИЮ!!!!

        model.addAttribute("user", user);
        model.addAttribute("company", company);
        model.addAttribute("companyData", companyData);
        model.addAttribute("swot", swot);
        model.addAttribute("competitivenessList", competitiveness);
        model.addAttribute("successMessage", "Отправлено");
        return "competitiveness";
    }

}
