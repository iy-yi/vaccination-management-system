package cmpe275.vms.repositories;

import cmpe275.vms.models.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Disease, String> {
    Disease findByName(String name);
    void deleteByName(String name);
}