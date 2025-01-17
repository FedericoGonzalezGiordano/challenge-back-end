package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantFlagDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.NewPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.PlantDto;
import org.springframework.stereotype.Service;

import java.util.List;
public interface PlantService {
    List<GetPlantFlagDto> getPlants();
    PlantDto logicDown(Long id);
    PlantDto newPlant(NewPlantDto newPlantDto);
    PlantDto updatePlant(GetPlantDto getPlantDto);

}
