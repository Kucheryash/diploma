package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.CompanyData;
import project.repository.CompanyDataRepository;

@Service
@Transactional
public class CompanyDataService {
    @Autowired
    CompanyDataRepository repo;

    public void save(CompanyData data){
        repo.save(data);
    }

    public CompanyData get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
