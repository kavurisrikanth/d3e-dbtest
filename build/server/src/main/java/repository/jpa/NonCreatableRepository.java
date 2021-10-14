package repository.jpa;

import models.NonCreatable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonCreatableRepository extends JpaRepository<NonCreatable, Long> {}
