package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
    private int revenue;

    @Column(nullable = false, name = "num_of_employees")
    private int employees;

}
