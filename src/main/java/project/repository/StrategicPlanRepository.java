package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entity.Company;
import project.entity.StrategicPlan;

import java.util.Optional;

@Repository
public interface StrategicPlanRepository extends JpaRepository<StrategicPlan, Long> {
     Optional<StrategicPlan> findById(Long id);
     StrategicPlan findByCompany(Company company);
}
