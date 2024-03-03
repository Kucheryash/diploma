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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForecastServiceTest {
    @Mock
    private ForecastRepository repo;

    @Mock
    private CompanyDataService companyDataService;

    @Mock
    private CompetitorsService competitorsService;

    @InjectMocks
    private ForecastService forecastService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateForecast() {
        // Arrange
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

        // Act
        forecastService.createForecast(forecastRevComp, forecastRevMarket, forecastMarketShare, companyData);

        // Assert
        // Add assertions based on the expected behavior of createForecast method
        // For example:
        verify(repo, times(1)).save(any(ForecastData.class));
        // Additional assertions based on the expected behavior of the method
    }
}