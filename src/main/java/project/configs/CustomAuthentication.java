package project.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.entities.enums.Role;
import project.services.UserService;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAuthentication extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Set<Role> roles = auth.getAuthorities().stream()
                .map(authority -> Role.valueOf(authority.getAuthority()))
                .collect(Collectors.toSet());

        Long userId = userService.getUserIdByUsername(username);
        if (roles.contains(Role.ROLE_SPEC))
            response.sendRedirect("/specialist/" + userId);
        else {
            response.sendRedirect("/home/" + userId);
        }
    }
}
