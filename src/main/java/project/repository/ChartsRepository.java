package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entity.Charts;
import project.entity.Company;
import project.entity.CompanyData;

import java.util.Optional;

@Repository
public interface ChartsRepository extends JpaRepository<Charts, Long> {
     Optional<Charts> findById(Long id);
     Charts findByData(CompanyData companyData);
}
