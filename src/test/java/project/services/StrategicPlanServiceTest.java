package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.entities.Company;
import project.entities.CompanyData;
import project.entities.StrategicPlan;
import project.repositories.StrategicPlanRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class StrategicPlanServiceTest {
    @Mock
    private StrategicPlanRepository repo;

    @Mock
    private CompetitorsService competitorsService;

    @InjectMocks
    private StrategicPlanService strategicPlanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testMakeRecommendations() {
        // Arrange
        CompanyData companyData = new CompanyData();
        companyData.setRevenue22(1000);
        companyData.setEmployees22(50);
        companyData.setCompany(new Company());

        when(competitorsService.getRevAndEmpNotNull()).thenReturn(getMockCompetitorsData());
        when(repo.save(any(StrategicPlan.class))).thenReturn(new StrategicPlan());

        // Act
        StrategicPlan result = strategicPlanService.makeRecommendations(companyData);

        // Assert
        // Add assertions based on the expected behavior of makeRecommendations method
        // For example:
        assertEquals("Исследуйте свои текущие ресурсы и процессы, чтобы выявить возможности для оптимизации и повышения эффективности. Рассмотрите автоматизацию рутинных задач, внедрение новых технологий и использование инструментов управления проектами для оптимизации рабочих процессов.\n" +
                "Обратите внимание на создание мотивирующей и поддерживающей рабочей среды, где сотрудники чувствуют себя ценными и могут достичь личного и профессионального роста.\n" +
                "Активно изучайте рынок и конкурентов, чтобы быть в курсе последних тенденций и изменений.", result.getDescription());
        assertEquals("создан", result.getStatus());
    }

    private List<Object[]> getMockCompetitorsData() {
        // Return mock competitors data for testing
        // Adjust the values based on your test scenario
        // Here is an example with two competitors
        List<Object[]> competitorsData = new ArrayList<>();
        Object[] competitorData1 = {2000, 100};
        Object[] competitorData2 = {1500, 80};
        competitorsData.add(competitorData1);
        competitorsData.add(competitorData2);
        return competitorsData;
    }
}