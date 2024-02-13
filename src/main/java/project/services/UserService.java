package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entities.Company;
import project.entities.User;
import project.repositories.CompanyRepository;
import project.repositories.UserRepository;

import java.sql.Date;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository repo;
    @Autowired
    CompanyRepository repoCompany;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    public String newUser(User user, Company company) {
        String email = user.getEmail();
        String password = user.getPassword();

        String company_name = company.getName();

        if (repoCompany.findByName(company_name) != null)
            return "existCompany";
        if (repo.findByEmail(email) != null)
            return "existUser";

        Date date = Date.valueOf(java.time.LocalDate.now());
        user.setDate(date);
        user.setNohash(password);
        user.setPassword(passwordEncoder.encode(password));
        repo.save(user);

        company.setUser(find(user));
        repoCompany.save(company);

        return "success";
    }

    public User get(Long id) {
        return repo.findById(id).get();
    }

    public User find(User user) {
        return repo.findByEmail(user.getEmail());
    }

    public Long getUserIdByUsername(String email) {
        User user = repo.findByEmail(email);
        return user.getId();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Value("${spring.mail.username}")
    private String username;
    public void sendEmail(String comp_name) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String emailTo = "julaymyak12@gmail.com";
        String subject = "Редактировать анализ компании '"+comp_name+"'.";
        String message = "С нетерпением жду взгялда специалиста!";

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
