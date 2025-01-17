package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TokenRepository extends JpaRepository<TokenEntity,Long> {
    @Query(value = """
    SELECT t FROM TokenEntity t INNER JOIN t.user u
    WHERE u.id = :id AND (t.expired = false OR t.revoked = false)
    """)
    List<TokenEntity> findAllValidTokenByUser(Long id);


    TokenEntity findByToken(String token);

}
