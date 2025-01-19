package ar.edu.utn.frc.tup.lc.iv.repositories;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity>findByEmail(String email);
}
