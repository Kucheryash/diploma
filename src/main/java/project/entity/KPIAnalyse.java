package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "kpi_analyse")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KPIAnalyse {
    @Column(name = "id_kpi")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "rev_per_employee")
    private int revenue;

    @Column(nullable = false, name = "overall_profit")
    private int profit;

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_kpi_company"))
    private Company company;
}
