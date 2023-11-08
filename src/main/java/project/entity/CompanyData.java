package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "comp_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyData {
    @Column(name = "id_data")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int revenue;

    @Column(nullable = false, name = "num_of_employees")
    private int employees;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_data_company"))
    private Company company;

}
