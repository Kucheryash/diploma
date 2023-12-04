package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Entity(name = "forecast_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForecastData {
    @Column(name = "id_forecast")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String compRevenue23;

    @Column(nullable = false)
    private String marketRevenue23;

    @Column(nullable = false)
    private String marketShare23;

    @Column(nullable = false, name = "field_of_activity")
    private String activity;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_data_id", nullable = false, foreignKey = @ForeignKey(name = "fk_forecast_company_data"))
    private CompanyData data;

}
