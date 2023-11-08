package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.KPIAnalyse;
import project.repository.CompanyRepository;
import project.repository.KPIAnalyseRepository;

@Service
@Transactional
public class KPIAnalyseService {
    @Autowired
    KPIAnalyseRepository repo;

    public void save(KPIAnalyse kpiAnalyse){
        repo.save(kpiAnalyse);
    }

    public KPIAnalyse get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
