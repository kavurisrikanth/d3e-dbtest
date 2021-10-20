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
			rs.getObject(i); // Just read id;
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
					FieldPrimitiveType pt = df.getPrimitiveType();
					switch (pt) {
					case Boolean:
						df.setValue(obj, rs.getBoolean(i++));
						break;
					case Date:
						throw new UnsupportedOperationException();
					case DateTime:
						throw new UnsupportedOperationException();
					case Double:
						df.setValue(obj, rs.getDouble(i++));
						break;
					case Duration:
						throw new UnsupportedOperationException();
					case Enum:
						String str = rs.getString(i++);
						DModel<?> enmType = schema.getType(df.getEnumType());
						Object val = enmType.getField(str).getValue(null);
						df.setValue(obj, val);
						break;
					case Integer:
						df.setValue(obj, rs.getLong(i++));
						break;
					case String:
						df.setValue(obj, rs.getString(i++));
						break;
					case Time:
						throw new UnsupportedOperationException();
					default:
						break;
					}
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

	private class EntityManagerImpl implements IEntityManager {

		private D3EPrimaryCache cache;

		public EntityManagerImpl() {
			cache = new D3EPrimaryCache();
		}

		@Override
		public void persist(DatabaseObject entity) {
			if (entity.getId() == 0l) {
				entity.setSaveStatus(DBSaveStatus.Saved);
				D3EQuery query = queryBuilder.generateCreateQuery(entity._typeIdx(), entity);
				execute(query);
			} else {
				BitSet _changes = entity._changes();
				if (_changes.isEmpty()) {
					return;
				}
				D3EQuery query = queryBuilder.generateUpdateQuery(entity._typeIdx(), _changes, entity);
				execute(query);
			}
		}

		@Override
		public void delete(DatabaseObject entity) {
			if (entity.saveStatus != DBSaveStatus.Saved) {
				return;
			}
			D3EQuery query = queryBuilder.generateDeleteQuery(entity._typeIdx(), entity);
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
			sb.append("SELECT _id FROM ").append(dm.getTableName()).append(" WHERE _id = ").append(id);
			String query = sb.toString();
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
			if (query.pre != null) {
				execute(query.pre);
			}
			String q = query.query;
			List<Object> args = query.args;
			D3ELogger.info("Executing query: " + q);

			DatabaseObject obj = query.getObj();
			if (obj instanceof DatabaseObject) {
				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.getJdbcTemplate().update(conn -> {
					PreparedStatement ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < args.size(); i++) {
						ps.setObject(i + 1, args.get(i));
					}
					return ps;
				}, keyHolder);
				long id = (long) keyHolder.getKeys().get("_id");
				query.getObj().setId(id);
			} else {
				jdbcTemplate.getJdbcTemplate().update(q, args.toArray());
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
			jdbcTemplate.getJdbcTemplate().query(query, new SingleObjectMapper(cache, obj, type, selectedFields));
		}

		@Override
		public void unproxyCollection(D3EPersistanceList<?> list) {
			// TODO Auto-generated method stub
			throw new RuntimeException();
		}

		@Override
		public void persistFile(DFile o) {
			// TODO Auto-generated method stub
			throw new RuntimeException();
		}
	}
}
