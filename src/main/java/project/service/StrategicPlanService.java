package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.StrategicPlan;
import project.repository.StrategicPlanRepository;

@Service
@Transactional
public class StrategicPlanService {
    @Autowired
    StrategicPlanRepository repo;

    public void save(StrategicPlan plan){
        repo.save(plan);
    }

    public StrategicPlan get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
