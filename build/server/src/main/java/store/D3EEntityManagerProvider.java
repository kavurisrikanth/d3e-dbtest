package store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import d3e.core.D3ELogger;
import d3e.core.DFile;
import gqltosql.schema.DModel;
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

	private class EntityManagerImpl implements IEntityManager {

		private D3EPrimaryCache cache;

		public EntityManagerImpl() {
			cache = new D3EPrimaryCache();
		}

		@Override
		public void persist(DatabaseObject entity) {
			if (entity.getSaveStatus() == DBSaveStatus.New) {
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
			DatabaseObject obj = getFromCache(dm, id);
			return obj;
		}

		private DatabaseObject getFromCache(DModel<?> dm, long id) {
			DatabaseObject obj = cache.get(dm.getIndex(), id);
			if (obj == null) {
				DatabaseObject ins = (DatabaseObject) dm.newInstance();
				ins.setId(id);
				ins._markProxy();
				cache.add(ins, dm.getIndex());
			}
			return obj;
		}

		@Override
		public <T> T getById(int type, long id) {
			PreparedStatement stmt = null;
			try {
				DModel<?> dm = schema.getType(type);
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT _id FROM ").append(dm.getTableName()).append(" WHERE _id = ").append(id);
				String query = sb.toString();
				stmt = null;// TODO
				ResultSet rs = stmt.executeQuery();
				if (rs.first()) {
					DatabaseObject obj = getFromCache(dm, id);
					return (T) obj;
				} else {
					return null;
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public <T> List<T> findAll(int type) {
			// TODO Auto-generated method stub
			throw new RuntimeException();
		}

		private void execute(D3EQuery query) {
			PreparedStatement stmt = null;
			try {
				if (query.pre != null) {
					execute(query.pre);
				}
				String q = query.query;
				List<Object> args = query.args;
				D3ELogger.info("Executing query: " + q);
				stmt = null;
				// TODO set args
				stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public Query createNativeQuery(String sql) {
			return new QueryImpl(sql, jdbcTemplate);
		}

		@Override
		public void unproxy(DatabaseObject obj) {
			PreparedStatement stmt = null;
			try {
				DModel<?> type = schema.getType(obj._typeIdx());
				String query = queryBuilder.generateSelectAllQuery(type, obj.getId());
				stmt = null;//
				ResultSet rs = stmt.executeQuery();
				if (rs.first()) {
					readObject(type, obj, rs);
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void unproxyCollection(D3EPersistanceList<?> list) {
			// TODO Auto-generated method stub
			throw new RuntimeException();
		}
		
		private void readObject(DModel<?> type, DatabaseObject obj, ResultSet rs) {
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
