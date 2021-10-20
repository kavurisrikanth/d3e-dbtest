package lists;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import d3e.core.D3ELogger;
import d3e.core.MapExt;
import store.DatabaseObject;
import store.QueryImplUtil;

public abstract class AbsDataQueryImpl {
	protected void setParameter(Query query, String name, DatabaseObject value) {
		QueryImplUtil.setParameter(query, name, value);
	}

	protected void setParameter(Query query, String name, Enum<?> value) {
		QueryImplUtil.setParameter(query, name, value);
	}

	protected void setParameter(Query query, String name, Object value) {
		QueryImplUtil.setParameter(query, name, value);
	}

	protected void setParameter(Query query, String name, LocalDate value) {
		QueryImplUtil.setParameter(query, name, value);
	}
	
	protected void setParameter(Query query, String name, LocalDateTime value) {
		QueryImplUtil.setParameter(query, name, value);
	}
		
	protected void setParameter(Query query, String name, LocalTime value) {
		QueryImplUtil.setParameter(query, name, value);
	}
	
	protected void setParameter(Query query, String name, String value) {
		QueryImplUtil.setParameter(query, name, value);
	}
	
	protected void assertLimitNotNegative(long limit) {
		if (limit < 0) {
			throw new RuntimeException("Limit is negative.");
		}
	}

	protected void logQuery(String sql, Query query) {
		D3ELogger.query(sql, query);
	}
}
