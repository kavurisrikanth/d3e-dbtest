package repository.jpa;

import java.util.List;
import models.Creatable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatableRepository extends JpaRepository<Creatable, Long> {
  public List<Creatable> getByRef(Creatable ref);

  @Query("From models.Creatable creatable where :refColl member creatable.refColl")
  public List<Creatable> findByRefColl(@Param("refColl") Creatable refColl);
}
