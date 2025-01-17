package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import ar.edu.utn.frc.tup.lc.iv.client.CountryRestTemplate;
import ar.edu.utn.frc.tup.lc.iv.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRestTemplate countryRestTemplate;

    @Override
    public List<CountryDto> getCountries() {
        List<CountryDto> countries = this.countryRestTemplate.getCountries().getBody();
        return countries;
    }
}
