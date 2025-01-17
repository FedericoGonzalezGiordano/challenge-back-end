package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CountryService {
    List<CountryDto>getCountries();
}
