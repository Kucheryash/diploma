package project.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import project.entities.User;
import project.services.CompetitorsService;
import project.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompetitorsControllerTest {
    @Mock
    private CompetitorsService competitorsService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private CompetitorsController competitorsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void market2_ShouldReturnMarketViewAndAddUserToModel() {
        // Arrange
        long userId = 1L;
        User user = new User();
        when(userService.get(userId)).thenReturn(user);

        // Act
        String viewName = competitorsController.market2(model, userId);

        // Assert
        assertEquals("market", viewName);
        verify(model, times(1)).addAttribute("user", user);
    }
}