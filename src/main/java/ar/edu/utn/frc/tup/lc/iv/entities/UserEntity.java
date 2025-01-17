package ar.edu.utn.frc.tup.lc.iv.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private String name;


    @Column
    private String password;


    @Column(unique = true)
    private String email;


    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<TokenEntity> tokens;

}
