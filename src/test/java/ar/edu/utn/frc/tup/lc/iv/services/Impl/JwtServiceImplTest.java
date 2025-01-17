package ar.edu.utn.frc.tup.lc.iv.services.Impl;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceImplTest {

    @SpyBean
    private JwtService jwtService;

    private UserEntity userEntity;
    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long EXPIRATION = 86400000;
    private final long REFRESH_EXPIRATION = 604800000;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("fede@fede.com");
        userEntity.setName("Fede");

        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", REFRESH_EXPIRATION);
    }

    @Test
    void generateToken() {
        String token = jwtService.generateToken(userEntity);

        assertNotNull(token);
        assertTrue(token.length() > 0);

        String username = jwtService.extractUserName(token);
        assertEquals(userEntity.getEmail(), username);
    }

    @Test
    void generateRefreshToken() {
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);

        String username = jwtService.extractUserName(refreshToken);
        assertEquals(userEntity.getEmail(), username);
    }

    @Test
    void extractUserName() {
        String token = jwtService.generateToken(userEntity);
        String username = jwtService.extractUserName(token);

        assertNotNull(username);
        assertEquals(userEntity.getEmail(), username);
    }

    @Test
    void isTokenValid() {
        String token = jwtService.generateToken(userEntity);

        boolean isValid = jwtService.isTokenValid(token, userEntity);
        assertTrue(isValid);

        UserEntity otherUser = new UserEntity();
        otherUser.setEmail("other@test.com");

        boolean isNotValid = jwtService.isTokenValid(token, otherUser);
        assertFalse(isNotValid);
    }

    @Test
    void isTokenExpired() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);
        String token = jwtService.generateToken(userEntity);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted while waiting for token expiration");
        }

        boolean isValid = jwtService.isTokenValid(token, userEntity);
        assertFalse(isValid);

        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
    }



    @Test
    void buildToken() {
        String token = ReflectionTestUtils.invokeMethod(jwtService, "buildToken", userEntity, EXPIRATION);

        assertNotNull(token);
        assertTrue(!token.isEmpty());

        String username = jwtService.extractUserName(token);
        assertNotNull(username);
        assertEquals(userEntity.getEmail(), username);

        boolean isValid = jwtService.isTokenValid(token, userEntity);
        assertTrue(isValid);

        Date expiration = ReflectionTestUtils.invokeMethod(jwtService, "extractExpiration", token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }
}