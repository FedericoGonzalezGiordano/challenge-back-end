package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
public interface TokenService {
    void saveUserToken(UserEntity user, String jwtToken);
    List<TokenEntity>findAllByExpiredIsFalseOOrRevokedIsFalse(Long userId);

}
