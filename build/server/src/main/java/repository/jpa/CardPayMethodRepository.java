package repository.jpa;

import models.CardPayMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardPayMethodRepository extends JpaRepository<CardPayMethod, Long> {}
