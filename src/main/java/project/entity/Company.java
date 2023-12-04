package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "companies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company {
    @Column(name = "id_company")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ba_id", nullable = false, foreignKey = @ForeignKey(name = "fk_company_ba"))
    private User user;

    @OneToMany(mappedBy= "company", cascade = CascadeType.ALL)
    private List<CompanyData> data;

    @OneToMany(mappedBy= "company", cascade = CascadeType.ALL)
    private List<SWOT> swots;

    @OneToMany(mappedBy= "company", cascade = CascadeType.ALL)
    private List<Competitiveness> competitiveness;

}
