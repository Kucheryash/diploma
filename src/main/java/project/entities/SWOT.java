package project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "swot_analyse")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SWOT {
    @Column(name = "id_swot")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 65535)
    private String strengths;

    @Column(nullable = false, length = 65535)
    private String weaknesses;

    @Column(nullable = false, length = 65535)
    private String opportunities;

    @Column(nullable = false, length = 65535)
    private String threats;

    @Column(nullable = false, name = "creation_date")
    private Date date;

    @Column(nullable = false)
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "fk_swot_company"))
    private Company company;
}
