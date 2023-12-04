package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "charts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Charts {
    @Column(name = "id_chart")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "rev_chart_path")
    private String revenuePath;

    @Column(nullable = false, name = "market_chart_path")
    private String marketSharePath;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_data_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chart_company_data"))
    private CompanyData data;

}
