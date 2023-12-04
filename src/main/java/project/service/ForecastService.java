package project.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.entity.*;
import project.repository.CompetitivenessRepository;
import project.repository.CompetitorsRepository;
import project.repository.ForecastRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@Transactional
public class ForecastService {
    @Autowired
    ForecastRepository repo;
    @Autowired
    CompetitorsRepository repoCompetitors;

    @Autowired
    CompanyDataService companyDataService;

    public void createForecast(List<Double> forecastRevComp, List<Double> forecastRevMarket, List<Double> forecastMarketShare, CompanyData companyData){
        String revCompAsString = forecastRevComp.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String revMarketAsString = forecastRevMarket.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String forecastMarketShareAsString = forecastMarketShare.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        ForecastData forecastData = new ForecastData();
        Date date = Date.valueOf(java.time.LocalDate.now());
        forecastData.setDate(date);
        forecastData.setData(companyData);
        forecastData.setActivity(companyData.getActivity());
        forecastData.setCompRevenue23(revCompAsString);
        forecastData.setMarketRevenue23(revMarketAsString);
        forecastData.setMarketShare23(forecastMarketShareAsString);
        save(forecastData);
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


    public ForecastData findByCompanyData(CompanyData companyData) {
        return repo.findByData(companyData);
    }

    public void save(ForecastData forecastData){
        repo.save(forecastData);
    }

    public ForecastData get(Long id) {
        return repo.findById(id).get();
    }

}
