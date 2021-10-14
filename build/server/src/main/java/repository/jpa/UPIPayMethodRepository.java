package repository.jpa;

import models.UPIPayMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UPIPayMethodRepository extends JpaRepository<UPIPayMethod, Long> {}
