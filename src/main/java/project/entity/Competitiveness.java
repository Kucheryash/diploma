package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false, name = "income_growth")
    private int income;

    @Column(nullable = false, name = "market_share")
    private int share;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_competitive_company"))
    private Company company;
}
