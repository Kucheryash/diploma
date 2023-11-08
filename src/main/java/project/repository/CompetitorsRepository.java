package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entity.Competitors;

import java.util.Optional;

@Repository
public interface CompetitorsRepository extends JpaRepository<Competitors, Long> {
     Optional<Competitors> findById(Long id);
}
