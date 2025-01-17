package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.LoginUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.RegisterUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.TokenResponseDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantFlagDto;
import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.TokenRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.UserRepository;
import ar.edu.utn.frc.tup.lc.iv.services.AuthService;
import ar.edu.utn.frc.tup.lc.iv.services.JwtService;
import ar.edu.utn.frc.tup.lc.iv.services.TokenService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceImplTest {
    @MockBean
    @Qualifier("modelMapper")
    private ModelMapper modelMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private TokenRepository tokenRepository;
    @SpyBean
    private AuthService authService;


    @Test
    void register() {
        RegisterUserDto argument = new RegisterUserDto();
        argument.setName("fede");
        argument.setPassword("123asd123a");
        argument.setEmail("fede@gmail.com");

        UserEntity expectedUserEntity = new UserEntity();
        expectedUserEntity.setEmail(argument.getEmail());

        when(modelMapper.map(argument, UserEntity.class)).thenReturn(expectedUserEntity);

        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(argument.getPassword())).thenReturn(encodedPassword);

        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserEntity);

        String jwtToken = "jwtToken123";
        String refreshToken = "refreshToken123";
        when(jwtService.generateToken(expectedUserEntity)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(expectedUserEntity)).thenReturn(refreshToken);

        TokenResponseDto response = authService.register(argument);

        assertNotNull(response);
        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        verify(passwordEncoder).encode(argument.getPassword());
    }

    @Test
    void login() {
        LoginUserDto argument = new LoginUserDto();
        argument.setPassword("fede@123.vk");
        argument.setEmail("fede@gmail.com");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(argument.getEmail(), argument.getPassword());
        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(mockAuthentication);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(argument.getEmail());
        when(userRepository.findByEmail("fede@gmail.com")).thenReturn(Optional.of(userEntity));

        String token = "asdadasdasdasdasdasdasdadsadasda1233";
        when(jwtService.generateToken(userEntity)).thenReturn(token);

        String refreshToken = "asdadasdasdasdasdasdasdadsadasda1234";
        when(jwtService.generateRefreshToken(userEntity)).thenReturn(refreshToken);

        doNothing().when(tokenService).saveUserToken(userEntity, token);

        TokenResponseDto response = authService.login(argument);

        verify(authenticationManager).authenticate(authenticationToken);
        verify(userRepository).findByEmail("fede@gmail.com");
        verify(jwtService).generateToken(userEntity);
        verify(jwtService).generateRefreshToken(userEntity);
        verify(tokenService).saveUserToken(userEntity, token);
        assertNotNull(response);
        assertEquals(token, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void refreshToken() {
        String validRefreshToken = "valid-refresh-token";
        String authHeader = "Bearer " + validRefreshToken;

        String userEmail = "fede@gmail.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userEmail);

        when(jwtService.extractUserName(validRefreshToken)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(jwtService.isTokenValid(validRefreshToken, userEntity)).thenReturn(true);

        String newAccessToken = "new-access-token";
        when(jwtService.generateToken(userEntity)).thenReturn(newAccessToken);

        TokenResponseDto response = authService.refreshToken(authHeader);

        assertNotNull(response);
        assertEquals(newAccessToken, response.getAccessToken());
        assertEquals(validRefreshToken, response.getRefreshToken());

        verify(jwtService).extractUserName(validRefreshToken);
        verify(userRepository).findByEmail(userEmail);
        verify(jwtService).isTokenValid(validRefreshToken, userEntity);
        verify(jwtService).generateToken(userEntity);

        verify(authService).saveUserToken(userEntity, newAccessToken);
    }

    @Test
    void testRevokeAllUserTokens() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        TokenEntity tokenEntity = new TokenEntity();
        List<TokenEntity> tokens = List.of(tokenEntity);

        when(tokenService.findAllByExpiredIsFalseOOrRevokedIsFalse(userEntity.getId())).thenReturn(tokens);

        ReflectionTestUtils.invokeMethod(authService, "revokeAllUserTokens", userEntity);

        verify(tokenRepository).saveAll(tokens);

        assertTrue(tokens.get(0).isRevoked());
        assertTrue(tokens.get(0).isExpired());
    }


}