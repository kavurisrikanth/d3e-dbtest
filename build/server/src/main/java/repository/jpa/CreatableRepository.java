package repository.jpa;

import d3e.core.SchemaConstants;
import java.util.List;
import javax.persistence.Query;
import models.Creatable;
import org.springframework.stereotype.Service;
import store.QueryImplUtil;

@Service
public class CreatableRepository extends AbstractD3ERepository<Creatable> {
  public int getTypeIndex() {
    return SchemaConstants.Creatable;
  }

  public List<Creatable> getByRef(Creatable ref) {
    String queryStr = "SELECT _id from _creatable where _ref_id = :ref";
    Query query = em().createNativeQuery(queryStr);
    QueryImplUtil.setParameter(query, "ref", ref);
    return getAllXsByY(query);
  }

  public List<Creatable> findByRefColl(Creatable refColl) {
    String queryStr = "SELECT _id from _creatable where _ref_coll_id = :refColl";
    Query query = em().createNativeQuery(queryStr);
    QueryImplUtil.setParameter(query, "refColl", refColl);
    return getAllXsByY(query);
  }
}
