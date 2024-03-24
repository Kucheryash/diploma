package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.entities.CompanyData;
import project.entities.ForecastData;
import project.repositories.ForecastRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ForecastServiceTest {
    @Mock
    private ForecastRepository repo;

    @InjectMocks
    private ForecastService forecastService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateForecast() {
        List<Double> forecastRevComp = new ArrayList<>();
        forecastRevComp.add(1000.0);
        forecastRevComp.add(2000.0);

        List<Double> forecastRevMarket = new ArrayList<>();
        forecastRevMarket.add(5000.0);
        forecastRevMarket.add(6000.0);

        List<Double> forecastMarketShare = new ArrayList<>();
        forecastMarketShare.add(10.0);
        forecastMarketShare.add(12.0);

        CompanyData companyData = new CompanyData();
        companyData.setActivity("IT-услуги");

        forecastService.createForecast(forecastRevComp, forecastRevMarket, forecastMarketShare, companyData);

        verify(repo, times(1)).save(any(ForecastData.class));
    }
}