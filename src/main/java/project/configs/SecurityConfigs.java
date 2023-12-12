package project.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigs{
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomAuthentication authenticationSuccessHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/static/**","/static/styles/**", "/static/images/**", "/reg", "/registration/**",
                                "/home", "/login/**", "/analysis/**", "/market", "/market/**", "/prices", "/prices/**",
                                "/swot/**").permitAll()
                        .requestMatchers("/home/**", "/go-to-company-data/**", "/new-company-data/**", "/save-report/**",
                                "/account/**", "/send-email/**").hasAnyRole("BA")
                        .requestMatchers("/specialist/**", "/edit-comp-spec/**", "/update-comp-spec/**").hasAnyRole("SPEC")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .failureUrl("/login_error")
                        .successHandler(authenticationSuccessHandler)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                        .logoutSuccessUrl("/"));
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("SELECT email, password, 1 as enabled FROM users WHERE email = ?")
                .authoritiesByUsernameQuery("SELECT users.email, roles.role FROM roles JOIN users ON users.id_user = roles.id_user WHERE users.email=?");
    }
}
