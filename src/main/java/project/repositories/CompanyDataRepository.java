package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Company;
import project.entities.CompanyData;

import java.util.Optional;

@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyData, Long> {
     Optional<CompanyData> findById(Long id);
     CompanyData findByCompany(Company company);
}
