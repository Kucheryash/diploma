package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Entity(name = "competitiveness")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Competitiveness {
    @Column(name = "id_comptit")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int revenue;

    @Column(nullable = false, name = "num_of_employees")
    private int employees;

    @Column(nullable = false, name = "field_of_activity")
    private String activity;

    @Column(nullable = false, name = "revenue_growth")
    private double revenueGrowth;

    @Column(nullable = false)
    private double profitability;

    @Column(nullable = false, name = "market_share")
    private double marketShare;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_competitive_company"))
    private Company company;
}
