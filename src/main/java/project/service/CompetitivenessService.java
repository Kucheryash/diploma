package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Competitiveness;
import project.repository.CompetitivenessRepository;

@Service
@Transactional
public class CompetitivenessService {
    @Autowired
    CompetitivenessRepository repo;

    public void save(Competitiveness competitiveness){
        repo.save(competitiveness);
    }

    public Competitiveness get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
