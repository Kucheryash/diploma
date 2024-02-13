package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.entities.Company;
import project.entities.SWOT;
import project.entities.User;
import project.services.CompanyDataService;
import project.services.CompanyService;
import project.services.SWOTService;

@Controller
public class SWOTController {
    @Autowired
    SWOTService swotService;
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyDataService companyDataService;

    @GetMapping("/swot/{id}")
    public String swot(Model model, @PathVariable("id") long id_company){
        Company company = companyService.get(id_company);
        User user = company.getUser();
        SWOT swot = swotService.findByCompany(company);
        if (swot==null)
            return "redirect:/analysis/"+user.getId()+"/"+id_company;
        model.addAttribute("user", user);
        model.addAttribute("company", company);
        model.addAttribute("swot", swot);
        return "swot";
    }
}
