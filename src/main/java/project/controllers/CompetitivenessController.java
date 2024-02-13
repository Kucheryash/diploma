package project.controllers;

import jakarta.servlet.http.HttpSession;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entities.*;
import project.services.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CompetitivenessController {
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
    ChartService chartService;
    @Autowired
    ForecastService forecastService;

    @RequestMapping(value = "/analysis/{idu}/{idc}", method = {RequestMethod.GET, RequestMethod.POST})
    public String analysis(Model model, @PathVariable("idu") long id_user, @PathVariable("idc") long id_company/*, @ModelAttribute("successMessage") String successMessage*/, HttpSession session) {
        Company company = companyService.get(id_company);
        User user = userService.get(id_user);
        CompanyData companyData = companyDataService.findByCompanyId(id_company);

        if (companyData == null)
            return "redirect:/go-to-company-data/" + id_user + "/" + id_company;

        SWOT swot = swotService.findByCompany(company);
        if (swot == null) {
            swot = swotService.SWOTAnalysis(companyData.getRevenue22(), companyData.getEmployees22(), companyData.getCompany());
            swotService.save(swot);
        }

        Competitiveness competitiveness = competitivenessService.findByCompany(company);
        if (competitiveness == null)
            competitiveness = competitivenessService.makeAnalisys(company);

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
            for (String data : compRevArr) {
                forecastRevComp.add(Double.parseDouble(data));
            }

            String[] marketRevArr = forecastData.getMarketRevenue23().split(",");
            for (String data : marketRevArr) {
                forecastRevMarket.add(Double.parseDouble(data));
            }

            String[] marketShareArr = forecastData.getMarketShare23().split(",");
            for (String data : marketShareArr) {
                forecastMarketShare.add(Double.parseDouble(data));
            }
        }

        Charts charts = chartService.findByCompanyData(companyData);
        if (charts == null)
            chartService.createCharts(forecastRevComp, forecastRevMarket, forecastMarketShare, companyData);

        StrategicPlan plan = planService.findByCompany(company);
        if (plan == null) {
            plan = planService.makeRecommendations(companyData);
            planService.save(plan);
        }

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
        model.addAttribute("swot", swot);
        model.addAttribute("company", company);
        model.addAttribute("companyData", companyData);
        model.addAttribute("competitivenessList", competitiveness);
        model.addAttribute("forecastRevComp", forecastRevComp);
        model.addAttribute("forecastRevMarket", forecastRevMarket);
        model.addAttribute("forecastMarketShare", forecastMarketShare);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("plan", plan);
        return "competitiveness";
    }

    @PostMapping("/save-report/{id}")
    public String saveReport(@PathVariable("id") long id_company, @RequestParam("directoryPath") String directoryPath) throws IOException, InvalidFormatException {
        Company company = companyService.get(id_company);

        competitivenessService.fillReportTemplate(company, directoryPath);

        return "redirect:/analysis/" + company.getUser().getId() + "/" + id_company;
    }
}
