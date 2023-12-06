package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entity.Company;
import project.entity.SWOT;

import java.util.List;
import java.util.Optional;

@Repository
public interface SWOTRepository extends JpaRepository<SWOT, Long> {
     Optional<SWOT> findById(Long id);
     SWOT findByCompany(Company company);

     List<SWOT> findAllByStatus(String status);
}
