package ar.edu.utn.frc.tup.lc.iv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "plants")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "readings")
    private int readings;

    @Column(name = "med_alerts")
    private int medAlerts;

    @Column(name = "red_alerts")
    private int redAlerts;

    @Column(name = "sensors_disabled")
    private int sensorsDisabled;

    @Column(name = "status")
    private boolean status;

}
