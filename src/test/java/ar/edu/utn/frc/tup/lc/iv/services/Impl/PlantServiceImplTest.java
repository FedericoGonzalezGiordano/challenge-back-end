package ar.edu.utn.frc.tup.lc.iv.services.Impl;

import ar.edu.utn.frc.tup.lc.iv.client.dto.CountryDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantFlagDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.NewPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.PlantDto;
import ar.edu.utn.frc.tup.lc.iv.entities.PlantEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.TokenEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.PlantRepository;
import ar.edu.utn.frc.tup.lc.iv.services.CountryService;
import ar.edu.utn.frc.tup.lc.iv.services.PlantService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlantServiceImplTest {
    @MockBean
    private PlantRepository plantRepository;
    @MockBean
    private CountryService countryService;
    @SpyBean
    private PlantService plantService;
    @MockBean
    @Qualifier("modelMapper")
    private ModelMapper modelMapper;
    @Test
    void getPlants() {
        List<PlantEntity> plantEntities = new ArrayList<>();
        PlantEntity plantEntity = new PlantEntity();
        plantEntity.setId(1L);
        plantEntity.setName("Cordoba");
        plantEntity.setCountry("Argentina");
        plantEntity.setStatus(true);
        plantEntities.add(plantEntity);
        when(plantRepository.findAllByStatusIsTrue()).thenReturn(plantEntities);

        List<CountryDto> countryDtoList = new ArrayList<>();
        CountryDto countryDto = new CountryDto();
        countryDto.setName("Argentina");
        countryDto.setFlag("AR");
        countryDtoList.add(countryDto);
        when(countryService.getCountries()).thenReturn(countryDtoList);

        GetPlantFlagDto expectedDto = new GetPlantFlagDto();
        expectedDto.setName("Cordoba");
        expectedDto.setCountry("Argentina");
        expectedDto.setFlag("AR");
        when(modelMapper.map(plantEntity, GetPlantFlagDto.class)).thenReturn(expectedDto);

        List<GetPlantFlagDto> result = plantService.getPlants();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cordoba", result.get(0).getName());
        assertEquals("Argentina", result.get(0).getCountry());
        assertEquals("AR", result.get(0).getFlag());
        verify(plantRepository, times(1)).findAllByStatusIsTrue();
        verify(countryService, times(1)).getCountries();
        verify(modelMapper, times(1)).map(plantEntity, GetPlantFlagDto.class);
    }


    @Test
    void logicDown() {
        Long plantId=1L;
        PlantEntity existingPlantEntity = new PlantEntity();
        existingPlantEntity.setName("Cordoba");
        existingPlantEntity.setId(plantId);
        existingPlantEntity.setStatus(true);
        PlantEntity updatedPlantEntity = new PlantEntity();
        existingPlantEntity.setName("Cordoba");
        updatedPlantEntity.setId(plantId);
        updatedPlantEntity.setStatus(false);
        PlantDto expectedDto = new PlantDto();
        expectedDto.setName("Cordoba");
        expectedDto.setStatus(false);
        when(this.plantRepository.findById(1L)).thenReturn(Optional.of(existingPlantEntity));
        when(this.plantRepository.save(any(PlantEntity.class))).thenReturn(updatedPlantEntity);
        when(modelMapper.map(any(PlantEntity.class), eq(PlantDto.class))).thenReturn(expectedDto);

        PlantDto result = plantService.logicDown(1L);
        verify(modelMapper, times(1)).map(updatedPlantEntity, PlantDto.class);
        assertNotNull(result);
        assertEquals("Cordoba", result.getName());
        assertFalse(result.status);
    }

    @Test
    void newPlant() {
        NewPlantDto newPlantDto = new NewPlantDto();
        newPlantDto.setCountry("Argentina");
        newPlantDto.setName("Cordoba Tapizadora");


        PlantEntity plantEntity = new PlantEntity();
        plantEntity.setCountry("Argentina");
        plantEntity.setReadings(150);
        plantEntity.setName("Cordoba Tapizadora");
        plantEntity.setRedAlerts(180);
        plantEntity.setMedAlerts(30);
        plantEntity.setSensorsDisabled(12);
        plantEntity.setStatus(true);
        when(plantRepository.save(any(PlantEntity.class))).thenReturn(plantEntity);
        when(modelMapper.map(any(NewPlantDto.class), eq(PlantEntity.class))).thenReturn(plantEntity);

        PlantDto expectedPlantDto = new PlantDto();
        expectedPlantDto.setName("Cordoba Tapizadora");
        expectedPlantDto.setStatus(true);
        when(modelMapper.map(any(PlantEntity.class), eq(PlantDto.class))).thenReturn(expectedPlantDto);

        PlantDto result = plantService.newPlant(newPlantDto);

        assertNotNull(result);
        assertEquals("Cordoba Tapizadora", result.getName());
        assertTrue(result.isStatus());

        verify(modelMapper, times(1)).map(newPlantDto, PlantEntity.class);
        verify(plantRepository, times(1)).save(plantEntity);
        verify(modelMapper, times(1)).map(plantEntity, PlantDto.class);
    }


    @Test
    void updatePlant() {
        GetPlantDto getPlantDto = new GetPlantDto();
        getPlantDto.setId(1L);
        getPlantDto.setCountry("Argentina");
        getPlantDto.setName("cactus");

        PlantEntity plantEntityToSearch = new PlantEntity();
        plantEntityToSearch.setId(1L);
        plantEntityToSearch.setCountry("Argentina");

        when(this.plantRepository.findById(getPlantDto.getId())).thenReturn(Optional.of(plantEntityToSearch));

        PlantEntity plantEntityToUpdate = new PlantEntity();
        plantEntityToUpdate.setId(1L);
        plantEntityToUpdate.setCountry("Argentina");
        plantEntityToUpdate.setName("cactus");

        when(plantRepository.save(any(PlantEntity.class))).thenReturn(plantEntityToUpdate);

        PlantDto expectedPlantDto = new PlantDto();
        expectedPlantDto.setName("cactus");
        expectedPlantDto.setStatus(true);

        when(modelMapper.map(any(PlantEntity.class), eq(PlantDto.class))).thenReturn(expectedPlantDto);

        PlantDto result = plantService.updatePlant(getPlantDto);

        assertNotNull(result);
        assertEquals(getPlantDto.getName(), result.getName());
        verify(plantRepository, times(1)).findById(getPlantDto.getId());
        verify(plantRepository, times(1)).save(any(PlantEntity.class));
        verify(modelMapper, times(1)).map(any(PlantEntity.class), eq(PlantDto.class));
    }


    @Test
    void getPlants_throwEntityNotFoundException_whenNoCountries() {
        when(countryService.getCountries()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            plantService.getPlants();
        });

        assertEquals("Not exist countries to match", exception.getMessage());
    }


    @Test
    void logicDown_throwEntityNotFoundException_whenPlantNotFound() {
        when(plantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            plantService.logicDown(1L);
        });

        assertEquals("Plant with ID 1 not found", exception.getMessage());
    }
    @Test
    void updatePlant_throwEntityNotFoundException_whenPlantNotFound() {
        when(plantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            plantService.updatePlant(new GetPlantDto(1L, "Cosquin", 0, 0, 0, 0, "Argentina", true));
        });

        assertEquals("Planta no encontrada", exception.getMessage());
    }
    @Test
    void logicDown_throwRuntimeException_whenUnknownErrorOccurs() {
        when(plantRepository.findById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            plantService.logicDown(1L);
        });

        assertEquals("An error occurred while updating the plant status", exception.getMessage());
    }




}