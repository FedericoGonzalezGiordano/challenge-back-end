package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.GetPlantFlagDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.NewPlantDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.plant.PlantDto;
import ar.edu.utn.frc.tup.lc.iv.services.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PatchMapping("/logicDown/{plantId}")
    @Operation(
            summary = "Logic down a plant",
            description = "Sets a plant's status to 'down' without deleting it from the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PlantDto> logicDownPlant(@PathVariable Long plantId) {
        PlantDto plantDto = this.plantService.logicDown(plantId);
        return ResponseEntity.ok(plantDto);
    }

    @GetMapping("/getPlants2")
    @Operation(
            summary = "Get list of plants with flags",
            description = "Fetches a list of plants, including flag data for each plant"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plants retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No plants found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<GetPlantFlagDto>> getPlants2() {
        List<GetPlantFlagDto> plantDtos = this.plantService.getPlants();
        return ResponseEntity.ok(plantDtos);
    }

    @PostMapping("/newPlant")
    @Operation(
            summary = "Create a new plant",
            description = "Creates a new plant with the given data"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PlantDto> createPlant(@RequestBody NewPlantDto newPlantDto) {
        PlantDto plantDto = this.plantService.newPlant(newPlantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(plantDto);
    }

    @PutMapping("/updatePlant")
    @Operation(
            summary = "Update plant information",
            description = "Updates the information of an existing plant"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PlantDto> updatePlant(@RequestBody GetPlantDto getPlantDto) {
        PlantDto plantDto = this.plantService.updatePlant(getPlantDto);
        return ResponseEntity.ok(plantDto);
    }
}
