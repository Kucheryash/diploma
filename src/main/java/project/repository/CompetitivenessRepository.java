package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entity.Competitiveness;

import java.util.Optional;

@Repository
public interface CompetitivenessRepository extends JpaRepository<Competitiveness, Long> {
     Optional<Competitiveness> findById(Long id);
}
