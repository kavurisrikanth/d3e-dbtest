package store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import d3e.core.D3ELogger;
import d3e.core.DFile;
import gqltosql.schema.DField;
import gqltosql.schema.DModel;
import gqltosql.schema.FieldPrimitiveType;
import gqltosql.schema.FieldType;
import gqltosql.schema.IModelSchema;

@Service
public class D3EEntityManagerProvider {

	@Autowired
	private D3EQueryBuilder queryBuilder;

	@Autowired
	private IModelSchema schema;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private ThreadLocal<IEntityManager> entityManager = new ThreadLocal<>();

	public boolean create() {
		IEntityManager mgr = entityManager.get();
		if (mgr != null) {
			return false;
		}
		entityManager.set(new EntityManagerImpl());
		return true;
	}

	public void clear() {
		entityManager.remove();
	}

	public IEntityManager get() {
		return entityManager.get();
	}

	static class RowField {
		DField field;
		List<RowField> subFields;

		public RowField(DField df) {
			this.field = df;
		}

		public RowField(DField df, List<RowField> subFields) {
			this.field = df;
			this.subFields = subFields;
		}
	}

	private class SingleObjectMapper implements RowMapper<DatabaseObject> {

		private D3EPrimaryCache cache;
		private DatabaseObject obj;
		private DModel dm;
		private List<RowField> selectedFields;

		public SingleObjectMapper(D3EPrimaryCache cache, DatabaseObject obj, DModel dm, List<RowField> selectedFields) {
			this.cache = cache;
			this.obj = obj;
			this.dm = dm;
			this.selectedFields = selectedFields;
		}

		@Override
		public DatabaseObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			int i = 1;
			rs.getObject(i++); // Just read id;
			readObject(rs, i, obj, selectedFields);
			return obj;
		}

		private int readObject(ResultSet rs, int i, Object obj, List<RowField> fields) throws SQLException {
			for (RowField rf : fields) {
				DField df = rf.field;
				if (rf.subFields != null) { // Embedded
					i = readObject(rs, i, df.getValue(obj), rf.subFields);
					continue;
				}
				FieldType type = df.getType();
				switch (type) {
				case Primitive:
					Object pri = readPrimitive(df, rs, i);
					df.setValue(obj, pri);
					i++;
					break;
				case Reference:
					DModel ref = df.getReference();
					long id = rs.getLong(i++);
					DatabaseObject val = cache.getOrCreate(ref, id);
					df.setValue(obj, val);
					break;
				case InverseCollection:
				case PrimitiveCollection:
				case ReferenceCollection:
					break;
				default:
					break;

				}
			}
			return i;
		}
	}

	private class CollectionMapper implements RowMapper<Object> {

		private D3EPrimaryCache cache;
		private DField field;

		public CollectionMapper(D3EPrimaryCache cache, DField field) {
			this.cache = cache;
			this.field = field;
		}

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			switch (field.getType()) {
			case PrimitiveCollection:
				return readPrimitive(field, rs, 1);
			case InverseCollection:
			case ReferenceCollection:
				DModel ref = field.getReference();
				long id = rs.getLong(1);
				return cache.getOrCreate(ref, id);
			}
			return null;
		}
	}

	private Object readPrimitive(DField df, ResultSet rs, int i) throws SQLException {
		FieldPrimitiveType pt = df.getPrimitiveType();
		switch (pt) {
		case Boolean:
			return rs.getBoolean(i);
		case Date:
			throw new UnsupportedOperationException();
		case DateTime:
			throw new UnsupportedOperationException();
		case Double:
			return rs.getDouble(i);
		case Duration:
			throw new UnsupportedOperationException();
		case Enum:
			String str = rs.getString(i);
			DModel<?> enmType = schema.getType(df.getEnumType());
			Object val = enmType.getField(str).getValue(null);
			return val;
		case Integer:
			return rs.getLong(i);
		case String:
			return rs.getString(i);
		case Time:
			throw new UnsupportedOperationException();
		default:
			throw new UnsupportedOperationException();
		}
	}

	private class EntityManagerImpl implements IEntityManager {

		private D3EPrimaryCache cache;

		public EntityManagerImpl() {
			cache = new D3EPrimaryCache();
		}

		@Override
		public void persist(DatabaseObject entity) {
			if (entity.getId() == 0l) {
				entity.setSaveStatus(DBSaveStatus.Saved);
				D3EQuery query = queryBuilder.generateCreateQuery(schema.getType(entity._typeIdx()), entity);
				execute(query);
			} else {
				BitSet _changes = entity._changes();
				if (_changes.isEmpty()) {
					return;
				}
				D3EQuery query = queryBuilder.generateUpdateQuery(schema.getType(entity._typeIdx()), entity);
				execute(query);
			}
		}

		@Override
		public void delete(DatabaseObject entity) {
			if (entity.saveStatus != DBSaveStatus.Saved) {
				return;
			}
			D3EQuery query = queryBuilder.generateDeleteQuery(schema.getType(entity._typeIdx()), entity);
			execute(query);
		}

		@Override
		public <T> T find(int type, long id) {
			DModel<?> dm = schema.getType(type);
			return (T) load(dm, id);
		}

		private DatabaseObject load(DModel<?> dm, long id) {
			return cache.getOrCreate(dm, id);
		}

		@Override
		public <T> T getById(int type, long id) {
			DModel<?> dm = schema.getType(type);
			StringBuilder sb = new StringBuilder();
			sb.append("select _id from ").append(dm.getTableName()).append(" where _id = ").append(id);
			String query = sb.toString();
			D3ELogger.info("By Id: type: " + type + ", id: " + id + " , " + query);
			List<Map<String, Object>> list = jdbcTemplate.getJdbcTemplate().queryForList(query);
			if (list.isEmpty()) {
				return null;
			}
			return (T) cache.getOrCreate(dm, id);
		}

		@Override
		public <T> List<T> findAll(int type) {
			// TODO Auto-generated method stub
			throw new RuntimeException();
		}

		private void execute(D3EQuery query) {
			if (query == null) {
				return;
			}
			if (query.pre != null) {
				execute(query.pre);
			}
			if (query.query != null) {
				String q = query.query;
				List<Object> args = query.args;
				D3ELogger.info("Insert/Update: " + q);

				DatabaseObject obj = query.getObj();
				if (obj instanceof DatabaseObject) {
					KeyHolder keyHolder = new GeneratedKeyHolder();
					jdbcTemplate.getJdbcTemplate().update(conn -> {
						PreparedStatement ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
						for (int i = 0; i < args.size(); i++) {
							Object arg = args.get(i);
							if (arg instanceof DatabaseObject) {
								arg = ((DatabaseObject) arg).getId();
							}
							ps.setObject(i + 1, arg);
						}
						return ps;
					}, keyHolder);
					long id = (long) keyHolder.getKeys().get("_id");
					query.getObj().setId(id);
				} else {
					Object[] argsArray = new Object[args.size()];
					for (int i = 0; i < args.size(); i++) {
						Object arg = args.get(i);
						if (arg instanceof DatabaseObject) {
							arg = ((DatabaseObject) arg).getId();
						}
						argsArray[i] = arg;
					}
					jdbcTemplate.getJdbcTemplate().update(q, argsArray);
				}
			}
			if (query.next != null) {
				execute(query.next);
			}
		}

		@Override
		public Query createNativeQuery(String sql) {
			return new QueryImpl(sql, jdbcTemplate);
		}

		@Override
		public void unproxy(DatabaseObject obj) {
			DModel<?> type = schema.getType(obj._typeIdx());
			List<RowField> selectedFields = new ArrayList<>();
			String query = queryBuilder.generateSelectAllQuery(type, selectedFields, obj.getId());
			D3ELogger.info("Unproxy Object: " + obj.getId() + " , " + query);
			jdbcTemplate.getJdbcTemplate().query(query, new SingleObjectMapper(cache, obj, type, selectedFields));
		}

		@Override
		public void unproxyCollection(D3EPersistanceList<?> list) {
			DatabaseObject master = list.getMaster();
			DModel<?> type = schema.getType(master._typeIdx());
			DField<?, ?> field = type.getField(list.getField());
			String query = queryBuilder.generateSelectCollectionQuery(type, field, master.getId());
			D3ELogger.info("Unproxy Collection: " + master.getId() + ", " + field.getName() + " , " + query);
			List<Object> result = jdbcTemplate.getJdbcTemplate().query(query, new CollectionMapper(cache, field));
			list._unproxy(result);
		}

		@Override
		public void persistFile(DFile o) {
			// TODO Auto-generated method stub
			throw new RuntimeException();
		}
	}
}
