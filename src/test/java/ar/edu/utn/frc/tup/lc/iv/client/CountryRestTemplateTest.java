package ar.edu.utn.frc.tup.lc.iv.client;

import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CountryRestTemplateTest {
    @MockBean
    private RestTemplate restTemplate;
    @SpyBean
    private CountryRestTemplate countryRestTemplate;

    @Test
    public void testGetCountries_success() {
        CountryDto[] mockCountries = {
                new CountryDto(1L, "Argentina", "ar","sd"),
                new CountryDto(2L, "Brazil", "br","ds")
        };

        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(CountryDto[].class)))
                .thenReturn(mockCountries);

        ResponseEntity<List<CountryDto>> response = countryRestTemplate.getCountries();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Argentina", response.getBody().get(0).getName());
        assertEquals("Brazil", response.getBody().get(1).getName());
    }

    @Test
    public void testGetCountries_fallback() {
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(CountryDto[].class)))
                .thenThrow(new RuntimeException("Service unavailable"));
        ResponseEntity<List<CountryDto>> response = countryRestTemplate.getCountries();
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        String expectedMessage = "The service is temporarily unavailable. Error: Service unavailable";
        assertEquals(expectedMessage, response.getBody());
    }

}