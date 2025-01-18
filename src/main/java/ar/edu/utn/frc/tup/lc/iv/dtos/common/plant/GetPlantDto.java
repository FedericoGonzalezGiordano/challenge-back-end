package ar.edu.utn.frc.tup.lc.iv.dtos.common.plant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPlantDto {
    private Long id;
    private String name;
    private Integer readings;
    private Integer medAlerts;
    private Integer redAlerts;
    private Integer sensorsDisabled;
    private String country;
    private String flag;
    public boolean status;
}
