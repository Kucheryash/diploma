package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entity.CompanyData;

import java.util.Optional;

@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyData, Long> {
     Optional<CompanyData> findById(Long id);
}
