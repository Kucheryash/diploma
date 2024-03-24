package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.entities.Company;
import project.entities.CompanyData;
import project.entities.SWOT;
import project.repositories.SWOTRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SWOTServiceTest {
    @Mock
    private SWOTRepository repo;

    @Mock
    private CompetitorsService competitorsService;

    @Mock
    private CompanyDataService companyDataService;

    @InjectMocks
    private SWOTService swotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void SWOTAnalysis() {
        int revenue = 1000;
        int num_of_employees = 50;
        Company company = new Company();
        CompanyData companyData = new CompanyData();
        List<Object[]> competitors = new ArrayList<>();
        Object[] competitor1 = {2000, 100};
        Object[] competitor2 = {1500, 80};
        competitors.add(competitor1);
        competitors.add(competitor2);

        when(companyDataService.findByCompanyId(company.getId())).thenReturn(companyData);
        when(competitorsService.getRevAndEmpNotNullByActivity(companyData.getActivity())).thenReturn(competitors);
        when(repo.save(any(SWOT.class))).thenReturn(new SWOT());

        SWOT result = swotService.SWOTAnalysis(revenue, num_of_employees, company);

        assertEquals("создан", result.getStatus());
    }
}