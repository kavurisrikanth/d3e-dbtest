package store;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import d3e.core.DFile;
import d3e.core.ListExt;
import d3e.core.MapExt;
import d3e.core.SchemaConstants;
import gqltosql.schema.DField;
import gqltosql.schema.DModel;
import gqltosql.schema.DModelType;
import gqltosql.schema.FieldType;
import gqltosql.schema.IModelSchema;
import gqltosql2.AliasGenerator;

@Service
public class D3EQueryBuilder {
	@Autowired
	private IModelSchema schema;

	@SuppressWarnings("rawtypes")
	public D3EQuery generateCreateQuery(int index, Object _this) {
		DModel type = schema.getType(index);
		StringBuilder sb = new StringBuilder();

		D3EQuery query = new D3EQuery();
		List<Object> args = ListExt.List(); // The actual values to be passed to the SQL query

		List<String> cols = ListExt.List(); // The column names
		List<String> params = ListExt.List(); // Just a list of ?s

		if (index != SchemaConstants.DFile) {
			// DFile has _id as part of its fields, so it will be covered in the below loop
			DatabaseObject asDbObj = (DatabaseObject) _this;
			query.setObj(asDbObj);
			cols.add("_save_status");
			params.add("?");
			args.add(asDbObj.getSaveStatus().ordinal());
		}

		for (DField field : type.getFields()) {
			Object arg = getValue(field, _this);
			FieldType fieldType = field.getType();
			if (fieldType == FieldType.InverseCollection) {
				continue;
			}

			boolean isRef = fieldType == FieldType.Reference;
			if (fieldType == FieldType.Primitive || isRef) {
				if (isRef) {
					DModel<?> ref = field.getReference();
					if (ref.isEmbedded()) {
						handleEmbedded(ref, cols, params, args, arg);
						continue;
					}
					
					if(arg == null) {
						continue;
					}

					if (field.isChild()) {
						// Add the value in the table; need query for that.
						int idx = field.getIndex();
						D3EQuery pre = generateCreateQuery(idx, arg);
						query.addPreQuery(pre);
					}

					// Value should already be there for references because we will collect
					// creatable references and call persist on all of them
					args.add(((DatabaseObject) arg).getId());
				} else {
					// Add normally
					args.add(arg);
				}

				cols.add(field.getColumnName());
				params.add("?");
			} else {
				// For collections
				D3EQuery pre = generateCollectionCreateQuery(type, _this, field, (List) arg, fieldType);
				query.addPreQuery(pre);
			}
		}

		sb.append("INSERT INTO ").append(type.getTableName()).append(" (").append(ListExt.join(cols, ", "))
				.append(") VALUES (").append(ListExt.join(params, ", ")).append(")");

		query.setQuery(sb.toString());
		query.setArgs(args);

		return query;
	}

	@SuppressWarnings("rawtypes")
	public D3EQuery generateCollectionCreateQuery(DModel masterModel, Object master, DField collField, List value,
			FieldType fieldType) {
		// Exactly the same as generateCreateQuery, except that the table to insert in
		// is different
		return generateCollectionCreateQueryInternal(masterModel, master, collField, value, fieldType, 0);
	}

	@SuppressWarnings("rawtypes")
	private D3EQuery generateCollectionCreateQueryInternal(DModel masterModel, Object master, DField collField,
			List value, FieldType fieldType, int startIdx) {
		String tableName = masterModel.getTableName();
		String masterColumn = tableName + (masterModel.getParent() != null ? "_" : "") + "_id";
		String collTableName = collField.getCollTableName(tableName);
		String collColumnName = collField.getColumnName();

		D3EQuery query = null;
		List<String> cols = ListExt.asList(masterColumn, collColumnName);
		List<String> params = ListExt.asList("?", "?");

		boolean isRC = fieldType == FieldType.ReferenceCollection;
		DModel<?> ref = null;
		if (isRC) {
			ref = collField.getReference();
			cols.add(collColumnName + "_order"); // Order column name
			params.add("?");
		}

		int _collIdx = startIdx;
		for (int i = 0; i < value.size(); i++) {
			Object one = value.get(i);
			StringBuilder sb2 = new StringBuilder();
			sb2.append("INSERT INTO ").append(collTableName).append(" (").append(ListExt.join(cols, ", "))
					.append(") VALUES (").append(ListExt.join(params, ", ")).append(")");

			List<Object> args = ListExt.asList(((DatabaseObject) master).getId(), getValue(collField, one));
			if (isRC) {
				args.add(_collIdx++);
			}

			D3EQuery oneQuery = new D3EQuery();
			oneQuery.setQuery(sb2.toString());
			oneQuery.setArgs(args);

			if (isRC) {
				// Add id
				// create insert query for that table ONLY if child
				args.add(((DatabaseObject) one).getId());
				if (collField.isChild()) {
					int idx = ref.getIndex();
					D3EQuery pre = generateCreateQuery(idx, one);
					oneQuery.addPreQuery(pre);
				}
			}

			query = mergeQueries(query, oneQuery);
		}

		return query;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getValue(DField field, Object _this) {
		Object value = field.getValue(_this);
		if (value instanceof DatabaseObject) {
			return ((DatabaseObject) value).getId();
		}
		return value;
	}

	private D3EQuery mergeQueries(D3EQuery old, D3EQuery _new) {
		if (old == null && _new == null) {
			return null;
		}
		if (old == null) {
			old = _new;
		} else {
			old.addPreQuery(_new);
		}
		return old;
	}

	@SuppressWarnings("rawtypes")
	private void handleEmbedded(DModel emb, List<String> defs, List<String> params, List<Object> args, Object _this) {
		// TODO: Collection fields?
		for (DField field : emb.getFields()) {
			String columnName = field.getColumnName();

			defs.add(columnName);
			params.add("?");

			Object fieldValue = getValue(field, _this);
			args.add(fieldValue);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public D3EQuery generateUpdateQuery(int _typeIdx, BitSet _changes, DatabaseObject _this) {
		// Assume that _changes is not empty.
		/*
		 * query: update <TABLE> set (X1 = ?, X2 = ?...) where _a._id = <ID> args: x1,
		 * x2...
		 */
		DModel type = schema.getType(_typeIdx);
		StringBuilder sb = new StringBuilder();

		List<String> assigns = ListExt.List();
		List<Object> args = ListExt.List();

		D3EQuery query = new D3EQuery();
		String tableName = type.getTableName();
		sb.append("UPDATE ").append(tableName).append(" SET ");

		for (int one : _changes.stream().toArray()) {
			DField field = type.getField(one);

			FieldType fieldType = field.getType();
			if (fieldType == FieldType.InverseCollection) {
				continue;
			}
			boolean isRef = fieldType == FieldType.Reference;
			if (fieldType == FieldType.Primitive || isRef) {
				if (isRef) {
					DModel ref = field.getReference();
					if (ref.isEmbedded()) {
						// Embedded - Individual field assigns
						List<String> defs = ListExt.List();
						List<String> params = ListExt.List(); // Maybe unnecessary, since it's just a list of ?s, but
																// need it to reuse handleEmbedded
						handleEmbedded(ref, defs, params, args, field.getValue(_this));
						for (int i = 0; i < defs.size(); i++) {
							String def = defs.get(i);
							String param = params.get(i);

							assigns.add(def + " = " + param);
						}
						continue;
					}
					if (field.isChild()) {
						/*
						 * Child - Check if the child is a new object. If new, Insert query and assign
						 * Else, just assign
						 */
						DatabaseObject _child = (DatabaseObject) field.getValue(_this);
						if (_child.getSaveStatus() == DBSaveStatus.New) {
							// Insert query
							D3EQuery childQuery = generateCreateQuery(_child._typeIdx(), _child);
							query = mergeQueries(query, childQuery);
						}
					}
				}
				String columnName = field.getColumnName();
				assigns.add(columnName + " = ?");
				args.add(getValue(field, _this));
			} else {
				// Collection
				ListChanges _oldValue = (ListChanges) _this._oldValue(field.getIndex());
				D3EQuery q = generateCollectionUpdateQuery(type, _this, field, (List) getValue(field, _this),
						_oldValue.getOld(), fieldType);

				query = mergeQueries(query, q);
			}
		}

		sb.append(ListExt.join(assigns, ", "));
		sb.append(" WHERE _id = ?");
		args.add(_this.getId());

		query.setQuery(sb.toString());
		query.setArgs(args);

		return query;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private D3EQuery generateCollectionUpdateQuery(DModel masterModel, Object master, DField collField, List value,
			List oldValue, FieldType fieldType) {
		String tableName = masterModel.getTableName();
		String masterColumn = tableName + (masterModel.getParent() != null ? "_" : "") + "_id";
		String collTableName = collField.getCollTableName(tableName);
		String collColumnName = collField.getColumnName();

		D3EQuery query = null;
		List<String> cols = ListExt.asList(masterColumn, collColumnName);
		List<String> params = ListExt.asList("?", "?");

		boolean isRC = fieldType == FieldType.ReferenceCollection;
		String orderColumnName = null;
		if (isRC) {
			orderColumnName = collColumnName + "_order";
			cols.add(orderColumnName); // Order column name
			params.add("?");
		}

		// Insert queries for these
		List inserted = ListExt.List();
		Map<Integer, Object> updated = MapExt.Map();
		int oldSize = oldValue.size();
		for (int i = 0; i < value.size(); i++) {
			Object one = value.get(i);
			if (i < oldSize) {
				// Counts as value being updated at this index
				Object oldOne = oldValue.get(i);
				if (!Objects.equals(one, oldOne)) {
					// Update at this index
					updated.put(i, one);
				}
			} else {
				// Counts as new value
				inserted.add(one);
			}
		}

		if (!inserted.isEmpty()) {
			D3EQuery createQuery = generateCollectionCreateQueryInternal(masterModel, master, collField, inserted,
					fieldType, oldSize);
			query = mergeQueries(query, createQuery);
		}

		// Query for updates
		long masterId = ((DatabaseObject) master).getId();
		if (!updated.isEmpty()) {
			// update <CollTable> set <CollColumn> = <value> where <MasterColumn> =
			// <masterId> and <OrderColumn> = <index>
			Set<Integer> keySet = updated.keySet();
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE ").append(collTableName).append(" SET ").append(collColumnName).append(" = ? WHERE ")
					.append(masterColumn).append(" = ?");
			if (isRC) {
				sb.append(" and ").append(orderColumnName).append(" = ?");
			}
			String queryStr = sb.toString();
			for (Integer idx : keySet) {
				Object update = updated.get(idx);
				List<Object> args = ListExt.asList(getValue(collField, update), masterId);
				if (isRC) {
					args.add(idx);
				}

				D3EQuery q = new D3EQuery();
				q.setQuery(queryStr);
				q.setArgs(args);

				query = mergeQueries(query, q);
			}
		}

		Map<Integer, Object> removed = MapExt.Map();
		for (int i = 0; i < oldValue.size(); i++) {
			Object one = oldValue.get(i);
			if (!value.contains(one)) {
				removed.put(i, one);
			}
		}

		if (!removed.isEmpty()) {
			Set<Integer> keySet = removed.keySet();
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM ").append(collTableName).append(" WHERE ").append(masterColumn).append(" = ?");
			if (isRC) {
				sb.append(" AND ").append(orderColumnName).append(" = ?");
			}
			String queryStr = sb.toString();
			for (int idx : keySet) {
				// delete from <CollTable> where <MasterColumn> = <masterId> and <OrderColumn> =
				// <index>
				D3EQuery q = new D3EQuery();
				List<Object> args = ListExt.asList(masterId);
				if (isRC) {
					args.add(idx);
				}

				q.setQuery(queryStr);
				q.setArgs(args);
				query = mergeQueries(query, q);
			}
		}

		return query;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public D3EQuery generateDeleteQuery(int index, DatabaseObject _this) {
		// TODO Auto-generated method stub
		// delete from <tableName> where _id = ?
		// If any of the props are childs or collections (primitive & child only), then
		// need query for those also
		DModel type = schema.getType(index);
		String tableName = type.getTableName();

		D3EQuery query = new D3EQuery();
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(tableName).append(" WHERE _id = ?");
		query.setQuery(sb.toString());
		query.setArgs(ListExt.asList(_this.getId()));

		for (DField field : type.getFields()) {
			FieldType fieldType = field.getType();
			if (fieldType == FieldType.InverseCollection || fieldType == FieldType.Primitive) {
				// Nothing to do
				continue;
			}
			if (fieldType == FieldType.Reference) {
				if (field.isChild()) {
					// Single query
					DModel child = field.getReference();
					DatabaseObject childValue = (DatabaseObject) field.getValue(_this);
					D3EQuery childQuery = generateDeleteQuery(child.getIndex(), childValue);

					query = mergeQueries(query, childQuery);
				}
			} else {
				// Collection query
				D3EQuery collectionQuery = generateCollectionDeleteQuery(type, _this, field,
						(List) field.getValue(_this));
				query = mergeQueries(query, collectionQuery);
			}
		}

		return query;
	}

	@SuppressWarnings("rawtypes")
	private D3EQuery generateCollectionDeleteQuery(DModel masterModel, DatabaseObject master, DField collField,
			List value) {
		// delete from <collTableName> where <masterId> = ? and <order> = ?
		String tableName = masterModel.getTableName();
		String masterColumn = tableName + (masterModel.getParent() != null ? "_" : "") + "_id";
		String collTableName = collField.getCollTableName(tableName);

		D3EQuery query = new D3EQuery();
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(collTableName).append(" WHERE ").append(masterColumn).append(" = ?");
		query.setQuery(sb.toString());
		query.setArgs(ListExt.asList(master.getId()));
		return query;
	}

	public D3EQuery generateDeleteQuery(DFile obj) {
		// delete from <tableName> where _id = ?
		D3EQuery query = new D3EQuery();
		query.setQuery("DELETE FROM _dfile WHERE _id = ?");
		query.setArgs(ListExt.asList(obj.getId()));
		return query;
	}

	public String generateSelectAllQuery(DModel type, long id) {
		AliasGenerator ag = new AliasGenerator();
		StringBuilder sb = new StringBuilder();
		sb.append("select _id");
		List<String> joins = new ArrayList<>();
		String alias = ag.next();
		appendAllColumns(sb, type, joins, ag, alias);
		sb.append(" from ").append(type.getTableName()).append(" ").append(alias);
		for (String j : joins) {
			sb.append(" ").append(j);
		}
		sb.append(" where _id = ").append(id);
		return sb.toString();
	}

	private void appendAllColumns(StringBuilder sb, DModel type, List<String> joins, AliasGenerator ag, String alias) {
		DField[] fields = type.getFields();
		for (DField df : fields) {
			FieldType ft = df.getType();
			switch (ft) {
			case Primitive:
				sb.append(", ").append(alias).append(".").append(df.getColumnName());
				break;
			case Reference:
				DModel ref = df.getReference();
				DModelType mt = ref.getModelType();
				if (mt == DModelType.MODEL) {
					if (ref.isDocument()) {
						sb.append(", ").append(alias).append(".").append(df.getColumnName());
					} else if (ref.isEmbedded()) {
						appendAllColumns(sb, ref, joins, ag, alias);// TODO
					} else if (ref.isNormal()) {
						sb.append(", ").append(alias).append(".").append(df.getColumnName());
					}
				}
				break;
			case PrimitiveCollection:
			case InverseCollection:
			case ReferenceCollection:
				break;
			default:
				break;
			}
		}
		DModel parent = type.getParent();
		if (parent != null) {
			String ja = ag.next();
			joins.add(parent.getTableName() + " " + ja);
			appendAllColumns(sb, parent, joins, ag, ja);
		}
	}
}
