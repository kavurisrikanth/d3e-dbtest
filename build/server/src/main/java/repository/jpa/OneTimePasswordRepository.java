package repository.jpa;

import d3e.core.SchemaConstants;
import java.util.List;
import javax.persistence.Query;
import models.OneTimePassword;
import models.User;
import org.springframework.stereotype.Service;
import store.QueryImplUtil;

@Service
public class OneTimePasswordRepository extends AbstractD3ERepository<OneTimePassword> {
  public int getTypeIndex() {
    return SchemaConstants.OneTimePassword;
  }

  public boolean checkTokenUnique(Long id, String token) {
    String queryStr =
        "SELECT CASE WHEN COUNT(x) > 0 THEN false ELSE true END as _a from _one_time_password where _token = :token and _id != :id";
    Query query = em().createNativeQuery(queryStr);
    QueryImplUtil.setParameter(query, "token", token);
    QueryImplUtil.setParameter(query, "id", id);
    return checkUnique(query);
  }

  public OneTimePassword getByToken(String token) {
    String queryStr = "SELECT _id from _one_time_password where _token = :token";
    Query query = em().createNativeQuery(queryStr);
    QueryImplUtil.setParameter(query, "token", token);
    return ((OneTimePassword) getXByY(query));
  }

  public List<OneTimePassword> getByUser(User user) {
    String queryStr = "SELECT _id from _one_time_password where _user_id = :user";
    Query query = em().createNativeQuery(queryStr);
    QueryImplUtil.setParameter(query, "user", user);
    return getAllXsByY(query);
  }
}
