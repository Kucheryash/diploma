package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.CompanyData;
import project.entities.ForecastData;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<ForecastData, Long> {
     Optional<ForecastData> findById(Long id);
     List<ForecastData> findAllByData(CompanyData companyData);

     ForecastData findByData(CompanyData companyData);

}
