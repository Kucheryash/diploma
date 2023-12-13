package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.entity.Competitors;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitorsRepository extends JpaRepository<Competitors, Long> {
     Optional<Competitors> findById(Long id);

     @Query("SELECT c.revenue22, c.employees22 FROM competitors c WHERE c.revenue22 IS NOT NULL AND c.employees22 IS NOT NULL")
     List<Object[]> findAllNotNullFields();

     @Query("SELECT c.revenue22 FROM competitors c WHERE c.revenue22 IS NOT NULL")
     List<Object> findRevenue22Values();

     @Query("SELECT c.growthRev FROM competitors c WHERE c.growthRev IS NOT NULL")
     List<Object> findRevenueGrowthValues();

     @Query("SELECT c.revenue22, c.employees22 FROM competitors c WHERE c.activity LIKE %:activity% AND c.revenue22 IS NOT NULL AND c.employees22 IS NOT NULL")
     List<Object[]> findAllNotNullFieldsByActivity(@Param("activity") String activity);

     @Query("SELECT c.revenue22 FROM competitors c WHERE c.activity LIKE %:activity% AND c.revenue22 IS NOT NULL")
     List<Object[]> findRevenue22ValuesByActivity(@Param("activity") String activity);

     @Query("SELECT c.growthRev FROM competitors c WHERE c.activity LIKE %:activity% AND c.growthRev IS NOT NULL")
     List<Object[]> findRevenueGrowthValuesByActivity(@Param("activity") String activity);
}
