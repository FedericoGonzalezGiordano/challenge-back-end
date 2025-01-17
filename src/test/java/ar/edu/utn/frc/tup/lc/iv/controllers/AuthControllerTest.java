package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.LoginUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.RegisterUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.TokenResponseDto;
import ar.edu.utn.frc.tup.lc.iv.services.AuthService;
import ar.edu.utn.frc.tup.lc.iv.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;

    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;

    @BeforeEach
    void setUp() {
        registerUserDto = new RegisterUserDto("fede", "fede12345678#", "fede@fede.com");
        loginUserDto = new LoginUserDto("fede@fede.com", "fede12345678#");
    }

    @Test
    void registerUserValid() throws Exception {
        TokenResponseDto tokenResponse = new TokenResponseDto("accessToken", "refreshToken");
        when(authService.register(any(RegisterUserDto.class))).thenReturn(tokenResponse);

        String requestJson = objectMapper.writeValueAsString(registerUserDto);
        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("accessToken"))
                .andExpect(jsonPath("$.refresh_token").value("refreshToken"));

        verify(authService).register(any(RegisterUserDto.class));
    }


    @Test
    void registerShortPassword() throws Exception {
        registerUserDto.setPassword("short");
        String requestJson = objectMapper.writeValueAsString(registerUserDto);
        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerInvalidEmail() throws Exception {
        registerUserDto.setEmail("fedefede");
        String requestJson = objectMapper.writeValueAsString(registerUserDto);
        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUserValid() throws Exception {
        TokenResponseDto tokenResponse = new TokenResponseDto("accessToken", "refreshToken");
        when(authService.login(any(LoginUserDto.class))).thenReturn(tokenResponse);

        String requestJson = objectMapper.writeValueAsString(loginUserDto);
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("accessToken"))
                .andExpect(jsonPath("$.refresh_token").value("refreshToken"));
    }

    @Test
    void loginEmailInvalid() throws Exception {
        loginUserDto.setEmail("asd");

        when(authService.login(any(LoginUserDto.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        String requestJson = objectMapper.writeValueAsString(loginUserDto);

        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Contraseña o email incorrectos."));
    }
    @Test
    void loginPasswordInvalid() throws Exception {
        loginUserDto.setEmail("asd");

        when(authService.login(any(LoginUserDto.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        String requestJson = objectMapper.writeValueAsString(loginUserDto);

        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Contraseña o email incorrectos."));
    }

    @Test
    void refreshTokenValid() throws Exception {
        String secretKey = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

        String refreshToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();


        TokenResponseDto tokenResponse = new TokenResponseDto("newAccessToken", refreshToken);
        when(authService.refreshToken(ArgumentMatchers.anyString())).thenReturn(tokenResponse);

        this.mockMvc.perform(post("/auth/refresh")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("newAccessToken"))
                .andExpect(jsonPath("$.refresh_token").value(refreshToken));
    }

    @Test
    void refreshTokenInvalid() throws Exception {
        String secretKey = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

        String refreshToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();


        TokenResponseDto tokenResponse = new TokenResponseDto("newAccessToken", refreshToken);
        when(authService.refreshToken(ArgumentMatchers.anyString())).thenReturn(tokenResponse);

        this.mockMvc.perform(post("/auth/refresh")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("newAccessToken"))
                .andExpect(jsonPath("$.refresh_token").value(refreshToken));
    }
}
