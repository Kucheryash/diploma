package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.entity.*;
import project.service.*;

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

    @PostMapping("/analysis/{idu}/{idc}")
    public String analysis(Model model, @PathVariable("idu") long id_user, @PathVariable("idc") long id_company){
        Company company = companyService.get(id_company);
        User user = userService.get(id_user);
        CompanyData companyData = companyDataService.find(id_company);

        if (companyData==null)
            return "redirect:/go-to-company-data/" + id_user + "/" + id_company;

        SWOT swot = swotService.findByCompany(company);
        if (swot==null)
            return "redirect:/swot/"+id_company;

        Competitiveness competitiveness = competitivenessService.findByCompany(company);
        if (competitiveness==null)
            competitiveness = competitivenessService.makeAnalisys(company);

        List<Double> forecastRevComp = competitivenessService.makeForecastRevCompany(competitiveness);
        List<Double> forecastRevMarket = competitivenessService.makeForecastRevMarket();
        List<Double> forecastMarketShare = competitivenessService.makeForecastMarketShare(competitiveness);

        model.addAttribute("user", user);
        model.addAttribute("swot", swot);
        model.addAttribute("company", company);
        model.addAttribute("companyData", companyData);
        model.addAttribute("competitivenessList", competitiveness);
        model.addAttribute("forecastRevComp", forecastRevComp);
        model.addAttribute("forecastRevMarket", forecastRevMarket);
        model.addAttribute("forecastMarketShare", forecastMarketShare);
        return "competitiveness";
    }
}
