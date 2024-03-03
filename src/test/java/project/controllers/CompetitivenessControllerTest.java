package project.controllers;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import project.entities.Company;
import project.entities.User;
import project.services.CompanyService;
import project.services.CompetitivenessService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompetitivenessControllerTest {
    @Mock
    private CompanyService companyService;
    @Mock
    private CompetitivenessService competitivenessService;

    @InjectMocks
    private CompetitivenessController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveReport_shouldSaveReportAndRedirect() throws IOException, InvalidFormatException {
        // Arrange
        long companyId = 1L;
        String directoryPath = "D:\\Учёба\\4 курс\\8 семестр\\Диплом\\Отчёты\\";
        Company company = new Company();
        company.setId(companyId);
        User user = new User(); // Создаем объект User
        user.setId(123L); // Устанавливаем id для User
        company.setUser(user); // Устанавливаем User в Company
        when(companyService.get(companyId)).thenReturn(company);

        // Act
        String redirectUrl = controller.saveReport(companyId, directoryPath);

        // Assert
        assertEquals("redirect:/analysis/" + company.getUser().getId() + "/" + companyId, redirectUrl);
        verify(competitivenessService, times(1)).fillReportTemplate(company, directoryPath);
    }

}