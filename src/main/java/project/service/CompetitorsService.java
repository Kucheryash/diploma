package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Competitors;
import project.repository.CompetitorsRepository;

import java.util.List;

@Service
@Transactional
public class CompetitorsService {
    @Autowired
    CompetitorsRepository repo;

    public List<Object[]> getRevAndEmpNotNull(){
        return repo.findAllNotNullFields();
    }

    public Competitors get(Long id) {
        return repo.findById(id).get();
    }
}
