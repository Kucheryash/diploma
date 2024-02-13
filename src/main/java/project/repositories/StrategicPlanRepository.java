package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Company;
import project.entities.StrategicPlan;

import java.util.Optional;

@Repository
public interface StrategicPlanRepository extends JpaRepository<StrategicPlan, Long> {
     Optional<StrategicPlan> findById(Long id);
     StrategicPlan findByCompany(Company company);
}
