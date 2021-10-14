package lists;

import classes.Customers;
import d3e.core.SchemaConstants;
import gqltosql.GqlToSql;
import gqltosql.SqlRow;
import gqltosql2.OutObject;
import gqltosql2.OutObjectList;
import graphql.language.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import models.Customer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.AbstractQueryService;
import rest.ws.RocketQuery;

@Service
public class CustomersImpl extends AbsDataQueryImpl {
  @Autowired private EntityManager em;
  @Autowired private GqlToSql gqlToSql;
  @Autowired private gqltosql2.GqlToSql gqlToSql2;

  public Customers get() {
    List<NativeObj> rows = getNativeResult();
    return getAsStruct(rows);
  }

  public Customers getAsStruct(List<NativeObj> rows) {
    List<Customer> result = new ArrayList<>();
    for (NativeObj _r1 : rows) {
      result.add(NativeSqlUtil.get(em, _r1.getRef(0), Customer.class));
    }
    Customers wrap = new Customers();
    wrap.setItems(result);
    return wrap;
  }

  public JSONObject getAsJson(Field field) throws Exception {
    List<NativeObj> rows = getNativeResult();
    return getAsJson(field, rows);
  }

  public JSONObject getAsJson(Field field, List<NativeObj> rows) throws Exception {
    JSONArray array = new JSONArray();
    List<SqlRow> sqlDecl0 = new ArrayList<>();
    for (NativeObj _r1 : rows) {
      array.put(NativeSqlUtil.getJSONObject(_r1, sqlDecl0));
    }
    gqlToSql.execute("Customer", AbstractQueryService.inspect(field, ""), sqlDecl0);
    JSONObject result = new JSONObject();
    result.put("items", array);
    return result;
  }

  public OutObject getAsJson(gqltosql2.Field field) throws Exception {
    List<NativeObj> rows = getNativeResult();
    return getAsJson(field, rows);
  }

  public OutObject getAsJson(gqltosql2.Field field, List<NativeObj> rows) throws Exception {
    OutObjectList array = new OutObjectList();
    OutObjectList sqlDecl0 = new OutObjectList();
    for (NativeObj _r1 : rows) {
      array.add(NativeSqlUtil.getOutObject(_r1, SchemaConstants.Customer, sqlDecl0));
    }
    gqlToSql2.execute("Customer", RocketQuery.inspect2(field, ""), sqlDecl0);
    OutObject result = new OutObject();
    result.addType(SchemaConstants.Customers);
    result.add("items", array);
    return result;
  }

  public List<NativeObj> getNativeResult() {
    String sql = "select a._id a0 from _customer a";
    Query query = em.createNativeQuery(sql);
    this.logQuery(sql, query);
    List<NativeObj> result = NativeSqlUtil.createNativeObj(query.getResultList(), 0);
    return result;
  }
}
