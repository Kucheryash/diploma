package project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import project.entity.enums.Role;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User{
    @Column(name = "id_user")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String hash;

    @Column(name = "created_date", nullable = false)
    private Date date;

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Role role;

}
