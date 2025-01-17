package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.LoginUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.RegisterUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.TokenResponseDto;
import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.TokenRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.UserRepository;
import ar.edu.utn.frc.tup.lc.iv.services.AuthService;
import ar.edu.utn.frc.tup.lc.iv.services.JwtService;
import ar.edu.utn.frc.tup.lc.iv.services.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public TokenResponseDto register(RegisterUserDto input) {
        UserEntity userEntity = modelMapper.map(input, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(input.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);
        String jwtToken = jwtService.generateToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponseDto(jwtToken, refreshToken);
    }

    @Override
    public TokenResponseDto login(LoginUserDto input) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        UserEntity userEntity = userRepository.findByEmail(input.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        revokeAllUserTokens(userEntity);
        saveUserToken(userEntity, jwtToken);
        return new TokenResponseDto(jwtToken, refreshToken);
    }

    @Override
    public TokenResponseDto refreshToken(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUserName(refreshToken);
        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        final UserEntity userEntity = userRepository.findByEmail(userEmail).
                orElseThrow(() -> new UsernameNotFoundException(userEmail));

        if (!jwtService.isTokenValid(refreshToken, userEntity)) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        final String accessToken = jwtService.generateToken(userEntity);
        revokeAllUserTokens(userEntity);
        saveUserToken(userEntity, accessToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Override
    public void saveUserToken(UserEntity user, String jwtToken) {
        tokenService.saveUserToken(user, jwtToken);
    }

    private void revokeAllUserTokens(final UserEntity user) {
        final List<TokenEntity> validUserTokens = tokenService.findAllByExpiredIsFalseOOrRevokedIsFalse(user.getId());
        if (!validUserTokens.isEmpty()) {
            for (final TokenEntity tokenEntity : validUserTokens) {
                tokenEntity.setRevoked(true);
                tokenEntity.setExpired(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }

    }
}
