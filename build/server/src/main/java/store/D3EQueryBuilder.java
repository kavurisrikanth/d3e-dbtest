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
import store.D3EEntityManagerProvider.RowField;

@Service
public class D3EQueryBuilder {

	@Autowired
	private IModelSchema schema;

	@SuppressWarnings("rawtypes")
	public D3EQuery generateCreateQuery(DModel type, DatabaseObject _this) {
		List<String> cols = ListExt.List();
		List<String> params = ListExt.List();
		List<Object> args = ListExt.List();
		D3EQuery query = new D3EQuery();
		query.setObj(_this);
		cols.add("_save_status");
		params.add("?");
		args.add(DBSaveStatus.Saved.ordinal());

		addInsertColumns(query, type, _this, cols, params, args);

		StringBuilder sb = new StringBuilder();
		sb.append("insert into ").append(type.getTableName()).append(" (").append(ListExt.join(cols, ", "))
				.append(") values (").append(ListExt.join(params, ", ")).append(")");
		query.setQuery(sb.toString());
		query.setArgs(args);
		return query;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addInsertColumns(D3EQuery query, DModel type, Object _this, List<String> cols, List<String> params,
			List<Object> args) {
		for (DField field : type.getFields()) {
			// TODO transient We should skip those fields
			if (field.getType() == FieldType.InverseCollection) {
				continue;
			}
			switch (field.getType()) {
			case Primitive:
				cols.add(field.getColumnName());
				params.add("?");
				addPrimitiveArg(args, field, field.getValue(_this));
				break;
			case Reference:
				DModel ref = field.getReference();
				if (ref.getIndex() == SchemaConstants.DFile) {
					Object value = field.getValue(_this);
					if (value != null) {
						cols.add(field.getColumnName());
						params.add("?");
						args.add(((DFile) value).getId());
					}
				} else if (ref.isDocument()) {
					// TODO Unable to get the document Doc
				} else if (ref.isEmbedded()) {
					addInsertColumns(query, ref, field.getValue(_this), cols, params, args);
				} else {
					if (field.isChild()) {
						Object value = field.getValue(_this);
						if (value != null) {
							D3EQuery chq = generateCreateQuery(ref, (DatabaseObject) value);
							query.addPreQuery(chq);
						}
					} else {
						cols.add(field.getColumnName());
						params.add("?");
						args.add(field.getValue(_this));
					}
				}
				break;
			case PrimitiveCollection:
				D3EQuery priColl = generatePrimitiveCollectionCreateQuery(field, (List) field.getValue(_this), _this);
				query.addNextQuery(priColl);
				break;
			case ReferenceCollection:
				List values = (List) field.getValue(_this);
				if (field.isChild()) {
					for (Object v : values) {
						D3EQuery chq = generateCreateQuery(field.getReference(), (DatabaseObject) v);
						query.addPreQuery(chq);
					}
				}
				D3EQuery refColl = generateReferenceCollectionCreateQuery(field, values, _this);
				query.addNextQuery(refColl);
				break;
			default:
				break;
			}
		}
	}

	private void addPrimitiveArg(List<Object> args, DField<?, ?> field, Object value) {
		args.add(value);
	}

	private D3EQuery generateReferenceCollectionCreateQuery(DField<?, ?> field, List<?> value, Object master) {
		if (value.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ").append(field.getCollTableName(null));
		sb.append(" (").append(field.declType().toColumnName()).append(", ").append(field.getColumnName()).append(", ")
				.append(field.getColumnName().replaceAll("_id$", "_order")).append(") values ");

		List<Object> args = ListExt.List();
		for (int i = 0; i < value.size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			args.add(master);
			args.add(value.get(i));
			sb.append("( ?, ?, ").append(i).append(")");
		}
		sb.append(";");
		D3EQuery query = new D3EQuery();
		query.setArgs(args);
		query.setQuery(sb.toString());
		return query;
	}

	private D3EQuery generatePrimitiveCollectionCreateQuery(DField<?, ?> field, List<?> value, Object master) {
		if (value.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ").append(field.getCollTableName(null));
		sb.append(" (").append(field.declType().toColumnName()).append(", ").append(field.getColumnName())
				.append(") values ");

		List<Object> args = ListExt.List();
		for (int i = 0; i < value.size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			args.add(master);
			addPrimitiveArg(args, field, value.get(i));
			sb.append("(?, ?)");
		}
		sb.append(";");
		D3EQuery query = new D3EQuery();
		query.setArgs(args);
		query.setQuery(sb.toString());
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
							D3EQuery childQuery = null;// generateCreateQuery(_child._typeIdx(), _child);
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
			D3EQuery createQuery = null;// generateCollectionCreateQueryInternal(masterModel, master, collField,
										// inserted,
//					fieldType, oldSize);
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

	public String generateSelectAllQuery(DModel type, List<RowField> selectedFields, long id) {
		AliasGenerator ag = new AliasGenerator();
		StringBuilder sb = new StringBuilder();
		sb.append("select _id");
		List<String> joins = new ArrayList<>();
		String alias = ag.next();
		appendAllColumns(sb, type, selectedFields, joins, ag, alias);
		sb.append(" from ").append(type.getTableName()).append(" ").append(alias);
		for (String j : joins) {
			sb.append(" ").append(j);
		}
		sb.append(" where _id = ").append(id);
		return sb.toString();
	}

	private void appendAllColumns(StringBuilder sb, DModel type, List<RowField> selectedFields, List<String> joins,
			AliasGenerator ag, String alias) {
		DField[] fields = type.getFields();
		for (DField df : fields) {
			FieldType ft = df.getType();
			switch (ft) {
			case Primitive:
				sb.append(", ").append(alias).append(".").append(df.getColumnName());
				selectedFields.add(new RowField(df));
				break;
			case Reference:
				DModel ref = df.getReference();
				DModelType mt = ref.getModelType();
				if (mt == DModelType.MODEL) {
					if (ref.isDocument()) {
						sb.append(", ").append(alias).append(".").append(df.getColumnName());
						selectedFields.add(new RowField(df));
					} else if (ref.isEmbedded()) {
						List<RowField> subFields = new ArrayList<>();
						appendAllColumns(sb, ref, subFields, joins, ag, alias);
						selectedFields.add(new RowField(df, subFields));
					} else if (ref.isNormal()) {
						sb.append(", ").append(alias).append(".").append(df.getColumnName());
						selectedFields.add(new RowField(df));
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
			appendAllColumns(sb, parent, selectedFields, joins, ag, ja);
		}
	}

	public String generateSelectCollectionQuery(DModel<?> type, DField<?, ?> field, long id) {
		StringBuilder b = new StringBuilder();
		b.append("select ");
		switch (field.getType()) {
		case InverseCollection:
			b.append("_id");
			break;
		case PrimitiveCollection:
		case ReferenceCollection:
			b.append(field.getColumnName());
			break;
		default:
			break;
		}

		b.append(" from ");

		switch (field.getType()) {
		case InverseCollection:
			b.append(field.getReference().getTableName());
			break;
		case PrimitiveCollection:
		case ReferenceCollection:
			b.append(field.getCollTableName(null));
			break;
		default:
			break;
		}

		b.append(" where ");

		switch (field.getType()) {
		case InverseCollection:
			b.append(field.getColumnName());
			break;
		case PrimitiveCollection:
		case ReferenceCollection:
			b.append(type.getTableName() + (type.getParent() == null ? "_id" : "__id"));
			break;
		default:
			break;
		}

		b.append(" = ").append(id);
		if (field.getType() == FieldType.ReferenceCollection) {
			b.append(" order by ");
			b.append(field.getColumnName().replaceAll("_id$", "_order"));
		}
		return b.toString();
	}
}
