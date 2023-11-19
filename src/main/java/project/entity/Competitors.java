package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "competitors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Competitors {
    @Column(name = "id_competitor")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private int revenue21;

    @Column
    private int revenue22;

    @Column(name = "rev_growth", columnDefinition = "DECIMAL(38,3)")
    private BigDecimal growthRev;

    @Column(name = "num_of_employees_21")
    private int employees21;

    @Column(name = "num_of_employees_22")
    private int employees22;

    @Column(name = "empl_growth", columnDefinition = "DECIMAL(38,3)")
    private BigDecimal growthEmpl;

    @Column(nullable = false, name = "field_of_activity")
    private String activity;
}
