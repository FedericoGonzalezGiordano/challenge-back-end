package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.PlantEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class PlantRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PlantRepository plantRepository;
    @Test
    void findAllByStatusIsTrue() {
        PlantEntity plantEntityInactive =new PlantEntity();
        plantEntityInactive.setStatus(false);
        plantEntityInactive.setReadings(30);
        plantEntityInactive.setName("quilmes");
        plantEntityInactive.setCountry("Argentina");
        plantEntityInactive.setMedAlerts(90);
        plantEntityInactive.setRedAlerts(60);
        plantEntityInactive.setSensorsDisabled(12);
        entityManager.persist(plantEntityInactive);
        entityManager.flush();

        PlantEntity plantEntityActive =new PlantEntity();
        plantEntityActive.setStatus(true);
        plantEntityActive.setReadings(60);
        plantEntityActive.setName("quilmes");
        plantEntityActive.setCountry("Argentina");
        plantEntityActive.setMedAlerts(30);
        plantEntityActive.setRedAlerts(90);
        plantEntityActive.setSensorsDisabled(21);
        entityManager.persist(plantEntityActive);
        entityManager.flush();

        List<PlantEntity> plantEntities = plantRepository.findAllByStatusIsTrue();
        assertNotNull(plantEntities);
        assertFalse(plantEntities.isEmpty());
        assertEquals(plantEntityActive.getId(), plantEntities.get(0).getId());

    }
}