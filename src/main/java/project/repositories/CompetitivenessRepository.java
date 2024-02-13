package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Company;
import project.entities.Competitiveness;

import java.util.Optional;

@Repository
public interface CompetitivenessRepository extends JpaRepository<Competitiveness, Long> {
     Optional<Competitiveness> findById(Long id);
     Competitiveness findByCompany(Company company);
}
