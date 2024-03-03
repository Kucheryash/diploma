package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.entities.Company;
import project.entities.CompanyData;
import project.repositories.CompanyDataRepository;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
class CompanyDataServiceTest {

    @InjectMocks
    private CompanyDataService companyDataService;

    @Mock
    private CompanyDataRepository companyDataRepository;

    @Mock
    private CompanyService companyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void fillIn() {
        // Создание заглушки для companyData
        CompanyData companyData = new CompanyData();
        Company company = new Company();
        Date date = Date.valueOf(java.time.LocalDate.now());

        // Установка поведения заглушки
        when(companyDataRepository.save(companyData)).thenReturn(companyData);
        when(companyService.get(company.getId())).thenReturn(company);

        // Вызов метода, который должен быть протестирован
        CompanyData result = companyDataService.fillIn(companyData, company);

        // Проверка, что метод save был вызван с ожидаемым аргументом
        verify(companyDataRepository).save(companyData);

        // Проверка, что данные были заполнены правильно
        assertEquals(date, result.getDate());
        assertEquals(company, result.getCompany());
        assertEquals(companyData, result);
    }
}