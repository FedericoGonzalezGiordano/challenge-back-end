package ar.edu.utn.frc.tup.lc.iv.client;
import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class CountryRestTemplate {

    @Autowired
    private RestTemplate restTemplate;
    private static final String RESILIENCE4J_INSTANCE_NAME = "circuitBreakerCountries";
    private static final String FALLBACK_METHOD = "fallback";

    @Value("${app.api-country}")
    private String baseUrl;

    public ResponseEntity<String> fallback(Exception ex) {
        String message = "The service is temporarily unavailable.";
        String errorDetails = "Error: " + ex.getMessage();
        String response = message + " " + errorDetails;
        return ResponseEntity.status(503).body(response);
    }
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<List<CountryDto>> getCountries() {
        try {
            CountryDto[] countries = restTemplate.getForObject(baseUrl, CountryDto[].class);
            return ResponseEntity.ok(Arrays.asList(countries));
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        }
    }

}
