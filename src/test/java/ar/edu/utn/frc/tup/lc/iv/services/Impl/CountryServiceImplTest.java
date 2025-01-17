package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.client.CountryRestTemplate;
import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import ar.edu.utn.frc.tup.lc.iv.repositories.TokenRepository;
import ar.edu.utn.frc.tup.lc.iv.services.CountryService;
import ar.edu.utn.frc.tup.lc.iv.services.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CountryServiceImplTest {


    @MockBean
    private CountryRestTemplate countryRestTemplate;
    @SpyBean
    private CountryService countryService;


    @Test
    void getCountries() {
        List<CountryDto> countriesToGet = new ArrayList<>();
        CountryDto countryDto1 = new CountryDto();
        countryDto1.setId(1L);
        countryDto1.setName("Argentina");

        CountryDto countryDto2 = new CountryDto();
        countryDto2.setId(2L);
        countryDto2.setName("Brazil");

        countriesToGet.add(countryDto1);
        countriesToGet.add(countryDto2);

        when(countryRestTemplate.getCountries()).thenReturn(ResponseEntity.ok(countriesToGet));

        List<CountryDto> response = countryService.getCountries();

        assertNotNull(response);
        assertEquals("Argentina", response.get(0).getName());
        assertEquals("Brazil", response.get(1).getName());
    }

}