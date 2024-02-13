package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entities.Company;
import project.entities.CompanyData;
import project.repositories.CompanyDataRepository;

import java.sql.Date;

@Service
@Transactional
public class CompanyDataService {
    @Autowired
    CompanyDataRepository repo;
    @Autowired
    CompanyService companyService;


    public CompanyData findByCompanyId(Long id_company) {
        Company company = companyService.get(id_company);
        CompanyData companyData = new CompanyData();
        companyData = repo.findByCompany(company);
        return companyData;
    }

    public CompanyData fillIn(CompanyData companyData, Company company){
        Date date = Date.valueOf(java.time.LocalDate.now());
        companyData.setDate(date);
        companyData.setCompany(company);
        save(companyData);
        return companyData;
    }

    public void save(CompanyData data){
        repo.save(data);
    }

    public CompanyData get(Long id) {
        return repo.findById(id).get();
    }

}
