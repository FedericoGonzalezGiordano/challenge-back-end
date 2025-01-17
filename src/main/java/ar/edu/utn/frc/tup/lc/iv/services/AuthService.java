package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.LoginUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.RegisterUserDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.auth.TokenResponseDto;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import org.springframework.stereotype.Service;

public interface AuthService {
    TokenResponseDto register(RegisterUserDto input);
    TokenResponseDto login(LoginUserDto input);
    TokenResponseDto refreshToken(final String authHeader);

    void saveUserToken(UserEntity user,String jwtToken);
}
