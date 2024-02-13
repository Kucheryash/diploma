package project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "strategic_plan")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StrategicPlan {
    @Column(name = "id_plan")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 65535)
    private String description;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @Column(nullable = false)
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_plan_company"))
    private Company company;
}
