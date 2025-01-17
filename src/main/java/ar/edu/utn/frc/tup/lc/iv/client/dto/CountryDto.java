package ar.edu.utn.frc.tup.lc.iv.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {
    private Long id;

    private String name;

    private String flag;
    private  String url;
}
