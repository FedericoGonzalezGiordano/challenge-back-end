package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.user.UserDto;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.UserRepository;
import ar.edu.utn.frc.tup.lc.iv.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    @Test
    public void testAllUsers() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("fede@fede.com");
        Mockito.when(userRepository.findAll()).thenReturn(List.of(userEntity));


        List<UserDto> users = userService.allUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("fede@fede.com", users.get(0).getEmail());
    }
}