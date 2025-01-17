package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;

public interface JwtService {

    String generateToken(UserEntity userEntity);
    String generateRefreshToken(UserEntity userEntity);

    String extractUserName(String refreshToken);
    boolean isTokenValid(String refreshToken ,UserEntity userEntity);

}
