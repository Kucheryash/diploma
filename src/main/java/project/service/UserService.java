package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.User;
import project.entity.enums.Role;
import project.repository.CompanyRepository;
import project.repository.UserRepository;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository repo;
    @Autowired
    CompanyRepository repoCompany;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setHash(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_BA);
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
}
