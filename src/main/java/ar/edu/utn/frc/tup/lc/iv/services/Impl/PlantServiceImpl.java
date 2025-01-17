package ar.edu.utn.frc.tup.lc.iv.services.Impl;
import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantFlagDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.NewPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.PlantDto;
import ar.edu.utn.frc.tup.lc.iv.entities.PlantEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.PlantRepository;
import ar.edu.utn.frc.tup.lc.iv.services.CountryService;
import ar.edu.utn.frc.tup.lc.iv.services.PlantService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PlantServiceImpl implements PlantService {

    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CountryService countryService;
    private final Random random = new Random();
    @Override
    public List<GetPlantFlagDto> getPlants() {
        List<PlantEntity> plantEntities = this.plantRepository.findAllByStatusIsTrue();

        List<CountryDto> countryDtoList = this.countryService.getCountries();

        List<GetPlantFlagDto> getPlantFlagDtoList = new ArrayList<>();

        if (countryDtoList.isEmpty()) {
            throw new EntityNotFoundException("Not exist countries to match");
        }

        if (plantEntities.isEmpty()) {
            throw new EntityNotFoundException("Not exist plants");
        }

        for (PlantEntity plant : plantEntities) {
            for (CountryDto country : countryDtoList) {
                if (plant.getCountry().equals(country.getName())) {
                    GetPlantFlagDto getPlantFlagDto = modelMapper.map(plant, GetPlantFlagDto.class);
                    getPlantFlagDto.setFlag(country.getUrl());
                    getPlantFlagDtoList.add(getPlantFlagDto);
                }
            }
        }

        return getPlantFlagDtoList;
    }

    @Override
    public PlantDto logicDown(Long id) {
        try {
            Optional<PlantEntity> plantEntityOptional = this.plantRepository.findById(id);
            if (plantEntityOptional.isPresent()) {
                PlantEntity plantEntity = plantEntityOptional.get();
                plantEntity.setStatus(false);
                PlantEntity updatedPlantEntity = this.plantRepository.save(plantEntity);
                return modelMapper.map(updatedPlantEntity, PlantDto.class);
            } else {
                throw new EntityNotFoundException("Plant with ID " + id + " not found");
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the plant status", e);
        }
    }

    @Override
    public PlantDto newPlant(NewPlantDto newPlantDto) {
        PlantEntity plantEntity=this.modelMapper.map(newPlantDto,PlantEntity.class);
        plantEntity.setStatus(true);
        plantEntity.setSensorsDisabled(generateSensorsDisabled());
        plantEntity.setMedAlerts(generateMediumAlert());
        plantEntity.setRedAlerts(generateRedAlert());
        plantEntity.setReadings(generateReading());
        this.plantRepository.save(plantEntity);
        return this.modelMapper.map(plantEntity,PlantDto.class);
    }

    @Override
    public PlantDto updatePlant(GetPlantDto getPlantDto) {
        Optional<PlantEntity> plantEntity = this.plantRepository.findById(getPlantDto.getId());
        if (plantEntity.isPresent()) {
            PlantEntity plantToSave = plantEntity.get();
            plantToSave.setRedAlerts(getPlantDto.getRedAlerts());
            plantToSave.setReadings(getPlantDto.getReadings());
            plantToSave.setMedAlerts(getPlantDto.getMedAlerts());
            plantToSave.setSensorsDisabled(getPlantDto.getSensorsDisabled());
            plantToSave.setCountry(getPlantDto.getCountry());
            plantToSave.setName(getPlantDto.getName());
            this.plantRepository.save(plantToSave);
            return this.modelMapper.map(plantToSave, PlantDto.class);
        } else {
            throw new EntityNotFoundException("Planta no encontrada");
        }
    }

    private  int generateRandomNumber(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private  int generateReading() {
        return generateRandomNumber(100, 500);
    }

    private  int generateMediumAlert() {
        return generateRandomNumber(1, 90);
    }

    private  int generateRedAlert() {
        return generateRandomNumber(1, 50);
    }

    private  int generateSensorsDisabled() {
        return generateRandomNumber(300,900);
    }

}
