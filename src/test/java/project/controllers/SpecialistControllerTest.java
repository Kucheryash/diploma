package project.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.entities.Company;
import project.entities.SWOT;
import project.entities.StrategicPlan;
import project.services.CompanyService;
import project.services.SWOTService;
import project.services.StrategicPlanService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SpecialistControllerTest {
    @Mock
    private CompanyService companyService;
    @Mock
    private SWOTService swotService;
    @Mock
    private StrategicPlanService planService;

    @InjectMocks
    private SpecialistController specialistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateCompSpecTest() {
        long userId = 1L;
        long companyId = 2L;
        SWOT newSwot = new SWOT();
        StrategicPlan newPlan = new StrategicPlan();
        Company company = new Company();

        when(companyService.get(companyId)).thenReturn(company);
        when(swotService.findByCompany(company)).thenReturn(new SWOT());
        when(planService.findByCompany(company)).thenReturn(new StrategicPlan());

        String result = specialistController.updateCompSpec(userId, companyId, newSwot, newPlan);

        verify(companyService, times(1)).get(companyId);
        verify(swotService, times(1)).findByCompany(company);
        verify(planService, times(1)).findByCompany(company);
        verify(swotService, times(1)).save(any(SWOT.class));
        verify(planService, times(1)).save(any(StrategicPlan.class));

        assertEquals("redirect:/specialist/" + userId, result);
    }
}