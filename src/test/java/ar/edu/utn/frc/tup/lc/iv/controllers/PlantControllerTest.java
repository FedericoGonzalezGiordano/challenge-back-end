package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantFlagDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.NewPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.PlantDto;
import ar.edu.utn.frc.tup.lc.iv.services.PlantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlantService plantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void logicDown() throws Exception {
        PlantDto expectedResponse = new PlantDto("Test Plant", false);
        when(plantService.logicDown(anyLong())).thenReturn(expectedResponse);

        mockMvc.perform(patch("/plant/logicDown/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void getPlants() throws Exception {
        List<GetPlantFlagDto> expectedPlants = Arrays.asList(
                new GetPlantFlagDto(1L, "Plant 1", 10, 2, 1, 1, "Argentina", "🇦🇷", true),
                new GetPlantFlagDto(2L, "Plant 2", 15, 3, 2, 0, "Brasil", "🇧🇷", true)
        );
        when(plantService.getPlants()).thenReturn(expectedPlants);

        mockMvc.perform(get("/plant/getPlants"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPlants)));
    }

    @Test
    void createPlant() throws Exception {
        NewPlantDto newPlantDto = new NewPlantDto("New Plant","Argentina");
        PlantDto expectedResponse = new PlantDto("New Plant", true);

        when(plantService.newPlant(any(NewPlantDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/plant/newPlant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlantDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void updatePlant() throws Exception {
        GetPlantDto updatePlantDto = new GetPlantDto(1L, "Updated Plant", 20, 3, 2, 1, "Argentina","asd", true);
        PlantDto expectedResponse = new PlantDto("Updated Plant", true);

        when(plantService.updatePlant(any(GetPlantDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(put("/plant/updatePlant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePlantDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }


}