package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.CompanyData;
import project.entity.Competitiveness;
import project.entity.Competitors;
import project.repository.CompetitivenessRepository;
import project.repository.CompetitorsRepository;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class CompetitivenessService {
    @Autowired
    CompetitivenessRepository repo;
    @Autowired
    CompetitorsRepository repoCompetitors;

    @Autowired
    CompanyDataService companyDataService;

    public Competitiveness makeAnalisys(Company company){
        List<Object> marketRevenues = repoCompetitors.findRevenue22Values();
        CompanyData companyData = companyDataService.find(company.getId());

        Competitiveness competitiveness = new Competitiveness();
        competitiveness.setRevenue(companyData.getRevenue22());
        competitiveness.setEmployees(companyData.getEmployees22());
        competitiveness.setActivity(companyData.getActivity());
        competitiveness.setCompany(company);

        double revenueGrowth = ((double) (companyData.getRevenue22()-companyData.getRevenue21()) /companyData.getRevenue21())*100;
        double profitability = ((double) companyData.getProfit22()/companyData.getRevenue22())*100;

        double sumMarkerRevenue = 0;
        for (Object revenue : marketRevenues) {
            if (revenue instanceof Number) {
                sumMarkerRevenue += ((Number) revenue).doubleValue();
            }
        }
        double marketShare = (companyData.getRevenue22()/sumMarkerRevenue)*100;

        Date date = Date.valueOf(java.time.LocalDate.now());
        competitiveness.setDate(date);
        competitiveness.setRevenueGrowth(Math.round(revenueGrowth * 10) / 10.0);
        competitiveness.setProfitability(Math.round(profitability * 10) / 10.0);
        competitiveness.setMarketShare(Math.round(marketShare * 10) / 10.0);

        save(competitiveness);

        return competitiveness;
    }

    public Competitiveness findByCompany(Company company) {
        return repo.findByCompany(company);
    }

    public void save(Competitiveness competitiveness){
        repo.save(competitiveness);
    }

    public Competitiveness get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
