package repository.jpa;

import d3e.core.SchemaConstants;
import models.NonCreatable;
import org.springframework.stereotype.Service;

@Service
public class NonCreatableRepository extends AbstractD3ERepository<NonCreatable> {
  public int getTypeIndex() {
    return SchemaConstants.NonCreatable;
  }
}
