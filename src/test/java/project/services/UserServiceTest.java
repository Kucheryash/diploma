package project.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    @Test
    public void sendEmail_ShouldSendEmailWithCorrectParameters() {
        // Arrange
        String compName = "TestCompany";
        String emailTo = "julaymyak12@gmail.com";
        String expectedSubject = "Редактировать анализ компании '" + compName + "'.";
        String expectedMessage = "С нетерпением жду взгялда специалиста!";

        // Act
        userService.sendEmail(compName);

        // Assert
        verify(mailSender, times(1)).send(mailMessageCaptor.capture());
        SimpleMailMessage capturedMailMessage = mailMessageCaptor.getValue();

        assertEquals(null, capturedMailMessage.getFrom());
        assertEquals(emailTo, capturedMailMessage.getTo()[0]);
        assertEquals(expectedSubject, capturedMailMessage.getSubject());
        assertEquals(expectedMessage, capturedMailMessage.getText());
    }
}