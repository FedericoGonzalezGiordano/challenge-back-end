package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.enums.TokenType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllValidTokenByUser() {
        UserEntity user = new UserEntity();
        user.setName("fede");
        entityManager.persist(user);
        entityManager.flush();

        TokenEntity validToken = new TokenEntity();
        validToken.setUser(user);
        validToken.setExpired(false);
        validToken.setRevoked(false);
        entityManager.persist(validToken);
        entityManager.flush();

        TokenEntity expiredTokenButNotRevoked = new TokenEntity();
        expiredTokenButNotRevoked.setUser(user);
        expiredTokenButNotRevoked.setExpired(true);
        expiredTokenButNotRevoked.setRevoked(false);
        entityManager.persist(expiredTokenButNotRevoked);
        entityManager.flush();

        TokenEntity revokedTokenButNotExpired = new TokenEntity();
        revokedTokenButNotExpired.setUser(user);
        revokedTokenButNotExpired.setExpired(false);
        revokedTokenButNotExpired.setRevoked(true);
        entityManager.persist(revokedTokenButNotExpired);
        entityManager.flush();

        TokenEntity expired = new TokenEntity();
        expired.setUser(user);
        expired.setExpired(true);
        expired.setRevoked(true);
        entityManager.persist(expired);
        entityManager.flush();

        List<TokenEntity> validTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        assertNotNull(validTokens);
        assertEquals(3, validTokens.size());
        assertTrue(validTokens.contains(validToken));
        assertFalse(validTokens.contains(expired));
    }


    @Test
    void findByToken() {
        UserEntity user = new UserEntity();
        user.setName("fede");
        entityManager.persist(user);
        entityManager.flush();

        TokenEntity validToken = new TokenEntity();

        validToken.setUser(user);
        validToken.setToken("asdasdasdasdasdasdadsadadsadsadsadsadasdadsad21/121asdasd");
        validToken.setExpired(false);
        validToken.setType(TokenType.BEARER);
        validToken.setRevoked(false);
        entityManager.persist(validToken);
        entityManager.flush();
        TokenEntity tokenEntityToSearch=this.tokenRepository.findByToken("asdasdasdasdasdasdadsadadsadsadsadsadasdadsad21/121asdasd");

        assertNotNull(tokenEntityToSearch);
        assertEquals(tokenEntityToSearch.getToken(), "asdasdasdasdasdasdadsadadsadsadsadsadasdadsad21/121asdasd");
        assertEquals(tokenEntityToSearch.getType(),TokenType.BEARER);
        assertFalse(tokenEntityToSearch.expired);

    }
}