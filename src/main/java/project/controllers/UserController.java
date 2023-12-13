package project.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entity.*;
import project.entity.enums.Role;
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
    public String account(Model model, @PathVariable("id") long id_user, @ModelAttribute("companyData") CompanyData companyData, HttpSession session){
        User user = userService.get(id_user);
        if (user.getRole().contains(Role.ROLE_SPEC))
            return "redirect:/specialist/"+user.getId();

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

        Company company = companyService.fingByBA(id_user);
        CompanyData data = dataService.findByCompanyId(company.getId());
        Competitiveness competitiveness = competitivenessService.findByCompany(company);

        if (data != null && competitiveness != null) {
            SWOT swot = swotService.findByCompany(company);
            ForecastData forecastData = forecastService.findByCompanyData(companyData);
            List<Double> forecastRevComp = new ArrayList<>();
            List<Double> forecastRevMarket = new ArrayList<>();
            List<Double> forecastMarketShare = new ArrayList<>();
            if (forecastData == null) {
                forecastRevComp = forecastService.makeForecastRevCompany(competitiveness);
                forecastRevMarket = forecastService.makeForecastRevMarket(companyData);
                forecastMarketShare = forecastService.makeForecastMarketShare(competitiveness, companyData);
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

            String status = swot.getStatus();
            if (status.equals("заявка"))
                model.addAttribute("status", "<Заявка специалисту отправлена>");
            else if (status.equals("изменено"))
                model.addAttribute("status", "<Данные обновлены>");
            else
                model.addAttribute("status", "");

            String successMessage = (String) session.getAttribute("successMessage");
            session.removeAttribute("successMessage");

            model.addAttribute("user", user);
            model.addAttribute("company", company);
            model.addAttribute("analysis", "Погнали");
            model.addAttribute("companyData", data);
            model.addAttribute("swot", swot);
            model.addAttribute("competitivenessList", competitiveness);
            model.addAttribute("forecastRevComp", forecastRevComp);
            model.addAttribute("forecastRevMarket", forecastRevMarket);
            model.addAttribute("forecastMarketShare", forecastMarketShare);
            model.addAttribute("plan", plan);
            model.addAttribute("successMessage", successMessage);
        }
        return "account";
    }

    @GetMapping("/specialist/{id}")
    public String accSpecialist(Model model, @PathVariable("id") long id_user){
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
        List<SWOT> swotList = swotService.findAllByStatus("заявка");

        List<Company> companyList = new ArrayList<>();
        for (SWOT swot : swotList) {
            companyList.add(swot.getCompany());
        }

        model.addAttribute("companyList", companyList);
        model.addAttribute("user", userService.get(id_user));
        return "acc-specialist";
    }

    @PostMapping("/send-email/{id}")
    public String email(HttpSession session, @PathVariable("id") Long id) {
        Company company = companyService.fingByBA(id);

        SWOT swot = swotService.findByCompany(company);
        swot.setStatus("заявка");
        swotService.save(swot);
        StrategicPlan plan = planService.findByCompany(company);
        plan.setStatus("заявка");
        planService.save(plan);

        userService.sendEmail(company.getName());

        session.setAttribute("successMessage", "Отправлено");
        return "redirect:/analysis/"+id+"/"+company.getId();
    }

    @PostMapping("/account/send-email/{id}")
    public String emailFromAcc(HttpSession session, @PathVariable("id") Long id) {
        Company company = companyService.fingByBA(id);

        SWOT swot = swotService.findByCompany(company);
        swot.setStatus("заявка");
        swotService.save(swot);
        StrategicPlan plan = planService.findByCompany(company);
        plan.setStatus("заявка");
        planService.save(plan);

        userService.sendEmail(company.getName());

        session.setAttribute("successMessage", "Отправлено");
        return "redirect:/account/"+id;
    }
}
