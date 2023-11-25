package project.service;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.User;
//import project.entity.enums.Role;
import project.repository.CompanyRepository;
import project.repository.UserRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Date;
import java.util.*;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository repo;
    @Autowired
    CompanyRepository repoCompany;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

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
//        user.setHash(passwordEncoder.encode(password));
//        user.setRole(Role.ROLE_BA);
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

    public List<User> listAll() {
        return (List<User>) repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void sendEmail(String phone) {
        // Настройки для подключения к серверу Gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Учетные данные отправителя
        final String senderEmail = "julaymyak@gmail.com";
        final String senderPassword = "j08260716";

        // Создание сессии
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        String specialistEmail = "julaymyak12@gmail.com";
        String messageTopic = "Помощь с конкурентоспособностью компании.";
        String messageContent = "Прошу помочь, вот номер: +" + phone;
        try {
            // Создание объекта MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(specialistEmail));
            message.setSubject(messageTopic);
            message.setText(messageContent);

            // Отправка сообщения
            Transport.send(message);

            System.out.println("Письмо успешно отправлено!");
        } catch (MessagingException e) {
            System.out.println("Ошибка при отправке письма: " + e.getMessage());
        }
    }
}
