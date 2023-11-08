package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.repository.CompanyRepository;

@Service
@Transactional
public class CompanyService {
    @Autowired
    CompanyRepository repo;

    public void save(Company company){
        repo.save(company);
    }

    public Company get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
