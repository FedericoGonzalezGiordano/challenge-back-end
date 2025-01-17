package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    void findByEmail() {
        UserEntity user=new UserEntity();
        user.setEmail("federico.gonzalez.it@gmail.com");
        entityManager.persist(user);
        entityManager.flush();
        Optional<UserEntity> userFindByEmail=userRepository.findByEmail(user.getEmail());
        assertNotNull(userFindByEmail);
        assertEquals("federico.gonzalez.it@gmail.com",userFindByEmail.get().getEmail());

    }
}