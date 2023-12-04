package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.User;
import project.repository.CompanyRepository;

@Service
@Transactional
public class CompanyService {
    @Autowired
    CompanyRepository repo;

    @Autowired
    UserService userService;

    public Company fingByBA(Long id_user){
        User user = userService.get(id_user);
        Company company = new Company();
        company = repo.findByUser(user);
        return company;
    }

    public void save(Company company){
        repo.save(company);
    }

    public Company findByName(String name){
        return repo.findByName(name);
    }

    public Company get(Long id) {
        return repo.findById(id).get();
    }

}
