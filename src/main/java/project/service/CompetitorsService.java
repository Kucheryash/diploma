package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Competitors;
import project.repository.CompetitorsRepository;

@Service
@Transactional
public class CompetitorsService {
    @Autowired
    CompetitorsRepository repo;

    public void save(Competitors competitor){
        repo.save(competitor);
    }

    public Competitors get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
