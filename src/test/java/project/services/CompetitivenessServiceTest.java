package project.services;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import project.entities.*;
import project.repositories.CompetitivenessRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CompetitivenessServiceTest {

    @Mock
    private CompetitivenessRepository repo;

    @Mock
    private CompanyDataService companyDataService;

    @Mock
    private CompetitorsService competitorsService;

    @InjectMocks
    private CompetitivenessService competitivenessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testMakeAnalysis() {
        // Arrange
        long companyId = 1;
        double expectedRevenueGrowth = 10.0;
        double expectedProfitability = 15.0;
        double expectedMarketShare = 28.6;

        Company company = new Company();
        company.setId(companyId);

        CompanyData companyData = new CompanyData();
        companyData.setRevenue22(1000);
        companyData.setRevenue21(900);
        companyData.setProfit22(150);
        companyData.setEmployees22(50);
        companyData.setActivity("Technology");

        when(companyDataService.findByCompanyId(companyId)).thenReturn(companyData);
        when(competitorsService.getRevenue22ValuesByActivity("Technology")).thenReturn(getMockMarketRevenues());
        when(repo.save(any(Competitiveness.class))).thenReturn(new Competitiveness());

        // Act
        Competitiveness result = competitivenessService.makeAnalisys(company);

        // Assert
        assertEquals(expectedRevenueGrowth, result.getRevenueGrowth());
        assertEquals(expectedProfitability, result.getProfitability());
        assertEquals(expectedMarketShare, result.getMarketShare());
        // Add more assertions based on the expected behavior of makeAnalisys method
    }

    private List<Object[]> getMockMarketRevenues() {
        // Return mock market revenues for testing
        // Adjust the values based on your test scenario
        // Here is an example with two market revenues
        List<Object[]> marketRevenues = new ArrayList<>();
        Object[] marketRevenue1 = {2000};
        Object[] marketRevenue2 = {1500};
        marketRevenues.add(marketRevenue1);
        marketRevenues.add(marketRevenue2);
        return marketRevenues;
    }

}