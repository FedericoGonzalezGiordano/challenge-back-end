package ar.edu.utn.frc.tup.lc.iv.dtos.common.plant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPlantDto {
    private String name;
    private  String country;
}