package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

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
    private int revenue22;

    @Column(nullable = false)
    private int revenue21;

    @Column(nullable = false)
    private int profit22;

    @Column(nullable = false, name = "num_of_employees22")
    private int employees22;

    @Column(nullable = false, name = "num_of_employees21")
    private int employees21;

    @Column(nullable = false, name = "field_of_activity")
    private String activity;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_data_company"))
    private Company company;

    @OneToMany(mappedBy= "data", cascade = CascadeType.ALL)
    private List<Charts> charts;

    @OneToMany(mappedBy= "data", cascade = CascadeType.ALL)
    private List<ForecastData> forecast;

}
