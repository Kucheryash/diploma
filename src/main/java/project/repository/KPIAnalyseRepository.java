package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entity.KPIAnalyse;

import java.util.Optional;

@Repository
public interface KPIAnalyseRepository extends JpaRepository<KPIAnalyse, Long> {
     Optional<KPIAnalyse> findById(Long id);
}
