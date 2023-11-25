package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.CompanyData;
import project.entity.Competitiveness;
import project.repository.CompetitivenessRepository;
import project.repository.CompetitorsRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

        double revenueGrowth = ((double) (companyData.getRevenue22()-companyData.getRevenue21()) /companyData.getRevenue22())*100;
        double profitability = ((double) companyData.getProfit22()/companyData.getRevenue22())*100;

        double sumMarkerRevenue = summaryMarketRev();
        double marketShare = (companyData.getRevenue22()/sumMarkerRevenue)*100;

        Date date = Date.valueOf(java.time.LocalDate.now());
        competitiveness.setDate(date);
        competitiveness.setRevenueGrowth(Math.round(revenueGrowth * 10) / 10.0);
        competitiveness.setProfitability(Math.round(profitability * 10) / 10.0);
        competitiveness.setMarketShare(Math.round(marketShare * 10) / 10.0);

        save(competitiveness);

        return competitiveness;
    }

    public List<Double> makeForecastRevCompany(Competitiveness competitiveness) {
        return monthRevenue(competitiveness.getRevenue(), competitiveness.getRevenueGrowth());
    }

    public List<Double> makeForecastMarketShare(Competitiveness competitiveness) {
        List<Double> monthRev = monthRevenue(competitiveness.getRevenue(), competitiveness.getRevenueGrowth());
        double sumMarketRevenue = summaryMarketRev()/12;

        List<Double> marketShareList = new ArrayList<>();
        for (Double value : monthRev) {
            double marketShare = value / sumMarketRevenue;
            double percents = marketShare*100;
            marketShareList.add(Math.round(percents*100.0)/100.0);
        }
        return marketShareList;
    }

    public List<Double> makeForecastRevMarket(){
        List<Object> marketRevenues = repoCompetitors.findRevenue22Values();
        double sumMarketRevenue = summaryMarketRev();
        double revenueMarket = sumMarketRevenue/marketRevenues.size();

        List<Object> marketGrowths = repoCompetitors.findRevenueGrowthValues();
        double sumMarketGrowthRev = 0;
        for (Object growth : marketGrowths) {
            if (growth instanceof Number) {
                sumMarketGrowthRev += ((Number) growth).doubleValue();
            }
        }
        double revenueGrowthMarket = sumMarketGrowthRev/marketGrowths.size();

        return monthRevenue(revenueMarket, revenueGrowthMarket);
    }

    public double summaryMarketRev(){
        List<Object> marketRevenues = repoCompetitors.findRevenue22Values();
        double sumMarketRevenue = 0;
        for (Object revenue : marketRevenues) {
            if (revenue instanceof Number) {
                sumMarketRevenue += ((Number) revenue).doubleValue();
            }
        }
        return sumMarketRevenue;
    }

    public static List<Double> monthRevenue(double revenue22, double growthRev) {
        List<Double> revenuePerMonth = new ArrayList<>();

        double revenue23Growth = (growthRev+0.53*growthRev)/ 100.0;
        double monthlyGrowth = revenue23Growth / 12.0;

        Random random = new Random();
        for (int m = 1; m <= 12; m++) {
            double monthlyRevenue = (revenue22 + revenue22*revenue23Growth)/12;
            if (m == 1 || m == 12 || m == 7 || m == 8) {
                monthlyRevenue = monthlyRevenue + monthlyRevenue*monthlyGrowth * 0.8;
            } else {
                monthlyRevenue = monthlyRevenue + monthlyRevenue*monthlyGrowth;
            }
            monthlyRevenue = monthlyRevenue * (1 + random.nextDouble() * 0.1);
            revenuePerMonth.add((double) Math.round(monthlyRevenue));
        }

        return revenuePerMonth;
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


}
