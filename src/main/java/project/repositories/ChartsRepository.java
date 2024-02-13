package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Charts;
import project.entities.CompanyData;

import java.util.Optional;

@Repository
public interface ChartsRepository extends JpaRepository<Charts, Long> {
     Optional<Charts> findById(Long id);
     Charts findByData(CompanyData companyData);
}
