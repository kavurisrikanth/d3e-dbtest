package repository.jpa;

import java.util.List;
import models.Creatable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatableRepository extends JpaRepository<Creatable, Long> {
  @Query(
      "SELECT CASE WHEN COUNT(x) > 0 THEN false ELSE true END from models.Creatable x where x.name = :name and x.id != :id")
  public boolean checkNameUnique(@Param("id") Long id, @Param("name") String name);

  public models.Creatable getByName(String name);

  @Query(
      "select e.name from models.Creatable e where e.name = (select max(f.name) from models.Creatable f)")
  public String getOldName();

  public List<Creatable> getByRef(Creatable ref);

  @Query("From models.Creatable creatable where :refColl member creatable.refColl")
  public List<Creatable> findByRefColl(@Param("refColl") Creatable refColl);
}
