package ar.edu.utn.frc.tup.lc.iv.services.Impl;
import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.UserEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.TokenRepository;
import ar.edu.utn.frc.tup.lc.iv.services.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class TokenServiceImplTest {
    @MockBean
    private TokenRepository tokenRepository;
    @SpyBean
    private TokenService tokenService;
    @Test
    void saveUserToken() {
        UserEntity userArgument=new UserEntity();
        String jwtToken="asdasdasdadadadadadadad";
        TokenEntity tokenEntityToSave=new TokenEntity();
        tokenEntityToSave.setToken(jwtToken);
        tokenEntityToSave.setUser(userArgument);
        when(this.tokenRepository.save(Mockito.any(TokenEntity.class))).thenReturn(tokenEntityToSave);
        tokenService.saveUserToken(userArgument, jwtToken);
        verify(tokenRepository, times(1)).save(Mockito.any(TokenEntity.class));
    }

    @Test
    void findAllByExpiredIsFalseOrRevokedIsFalse_ShouldReturnTokens() {
        Long userId = 1L;
        TokenEntity token1 = new TokenEntity();
        token1.setUser(new UserEntity());
        token1.setExpired(false);
        token1.setRevoked(false);
        TokenEntity token2 = new TokenEntity();
        token2.setUser(new UserEntity());
        token2.setExpired(false);
        token2.setRevoked(false);
        when(tokenRepository.findAllValidTokenByUser(userId)).thenReturn(Arrays.asList(token1, token2));
        List<TokenEntity> tokens = tokenService.findAllByExpiredIsFalseOOrRevokedIsFalse(userId);
        assertNotNull(tokens);
        assertEquals(2, tokens.size());
        verify(tokenRepository, times(1)).findAllValidTokenByUser(userId);
    }

}