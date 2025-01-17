package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.enums.TokenType;
import ar.edu.utn.frc.tup.lc.iv.repositories.TokenRepository;
import ar.edu.utn.frc.tup.lc.iv.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Override
    public void saveUserToken(UserEntity user, String jwtToken) {
        TokenEntity tokenEntity=new TokenEntity();
        tokenEntity.setToken(jwtToken);
        tokenEntity.setUser(user);
        tokenEntity.setType(TokenType.BEARER);
        tokenEntity.setExpired(false);
        tokenEntity.setRevoked(false);
        this.tokenRepository.save(tokenEntity);
    }

    @Override
    public List<TokenEntity> findAllByExpiredIsFalseOOrRevokedIsFalse(Long userId) {
        List<TokenEntity> tokens = this.tokenRepository.findAllValidTokenByUser(userId);
        if (tokens.isEmpty()) {
            throw new NoSuchElementException("No tiene tokens");
        }
        return tokens;
    }
}
