package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.SWOT;
import project.repository.SWOTRepository;

@Service
@Transactional
public class SWOTService {
    @Autowired
    SWOTRepository repo;

    public void save(SWOT swot){
        repo.save(swot);
    }

    public SWOT get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
