package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.client.CountryRestTemplate;
import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantFlagDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.NewPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.PlantDto;
import ar.edu.utn.frc.tup.lc.iv.services.CountryService;
import ar.edu.utn.frc.tup.lc.iv.services.PlantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plant")
@Tag(name = "Plant", description = "Endpoints for plant management")
public class PlantController {

    @Autowired
    private PlantService plantService;
    @Autowired
    private CountryService countryService;

    @PatchMapping("/logicDown/{plantId}")
    public ResponseEntity<PlantDto> logicDownPlant(@PathVariable Long plantId) {
            PlantDto plantDto = this.plantService.logicDown(plantId);
            return ResponseEntity.ok(plantDto);
    }

    @GetMapping("/getPlants")
    public List<CountryDto> getCountries() {
        return this.countryService.getCountries();
    }

    @GetMapping("/getPlants2")
    public ResponseEntity<List<GetPlantFlagDto>> getPlants2() {
        List<GetPlantFlagDto> plantDtos = this.plantService.getPlants();
        return ResponseEntity.ok(plantDtos);
    }

    @PostMapping("/newPlant")
    public ResponseEntity<PlantDto> createPlant(@RequestBody NewPlantDto newPlantDto) {
            PlantDto plantDto = this.plantService.newPlant(newPlantDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(plantDto);
    }

    @PutMapping("/updatePlant")
    public ResponseEntity<PlantDto> updatePlant(@RequestBody GetPlantDto getPlantDto) {
            PlantDto plantDto = this.plantService.updatePlant(getPlantDto);
            return ResponseEntity.ok(plantDto);

    }
}
