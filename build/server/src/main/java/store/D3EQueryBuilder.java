package store;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import d3e.core.DFile;
import d3e.core.ListExt;
import d3e.core.SchemaConstants;
import gqltosql.schema.DField;
import gqltosql.schema.DModel;
import gqltosql.schema.DModelType;
import gqltosql.schema.FieldType;
import gqltosql.schema.IModelSchema;
import gqltosql2.AliasGenerator;
import store.D3EEntityManagerProvider.RowField;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class D3EQueryBuilder {

	@Autowired
	private IModelSchema schema;

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

		if (type.getParent() != null) {
			query.addPreQuery(generateCreateQuery(type.getParent(), _this));
		}
		return query;
	}

	private void addInsertColumns(D3EQuery query, DModel type, Object _this, List<String> cols, List<String> params,
			List<Object> args) {
		for (DField field : type.getFields()) {
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
					}
					cols.add(field.getColumnName());
					params.add("?");
					args.add(field.getValue(_this));
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

	public D3EQuery generateUpdateQuery(DModel type, DatabaseObject _this) {
		List<String> updates = ListExt.List();
		List<Object> args = ListExt.List();
		D3EQuery query = new D3EQuery();

		addUpdateColumns(query, type, _this, updates, args);

		if (!updates.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("update ").append(type.getTableName()).append(" set ").append(ListExt.join(updates, ", "))
					.append(" where _id = ").append(_this.getId());
			query.setQuery(sb.toString());
			query.setArgs(args);
		}

		if (type.getParent() != null) {
			D3EQuery parent = generateCreateQuery(type.getParent(), _this);
			if (parent != null) {
				query.addPreQuery(parent);
			}
		}
		return query;
	}

	private void addUpdateColumns(D3EQuery query, DModel type, DBObject _this, List<String> updates,
			List<Object> args) {
		BitSet changes = _this._changes();
		for (DField field : type.getFields()) {
			if (field.getType() == FieldType.InverseCollection || !changes.get(field.getIndex())) {
				continue;
			}
			switch (field.getType()) {
			case Primitive:
				updates.add(field.getColumnName() + " = ?");
				addPrimitiveArg(args, field, field.getValue(_this));
				break;
			case Reference:
				DModel ref = field.getReference();
				if (ref.getIndex() == SchemaConstants.DFile) {
					Object value = field.getValue(_this);
					if (value != null) {
						updates.add(field.getColumnName() + " = ?");
						args.add(((DFile) value).getId());
					}
				} else if (ref.isDocument()) {
					// TODO Unable to get the document Doc
				} else if (ref.isEmbedded()) {
					addUpdateColumns(query, ref, (DBObject) field.getValue(_this), updates, args);
				} else {
					if (field.isChild()) {
						DatabaseObject obj = (DatabaseObject) _this;
						DatabaseObject old = obj.getOld();
						Object oldChild = field.getValue(old);
						Object newChild = field.getValue(_this);
						if (oldChild == newChild) {
							continue;
						}
						if (newChild != null) {
							D3EQuery chq = generateCreateQuery(ref, (DatabaseObject) newChild);
							query.addPreQuery(chq);
						}
						if (oldChild != null) {
							D3EQuery chq = generateDeleteQuery(ref, (DatabaseObject) oldChild);
							query.addPreQuery(chq);
						}
					}
					updates.add(field.getColumnName() + " = ?");
					args.add(field.getValue(_this));
				}
				break;
			case PrimitiveCollection:
				D3EQuery priDel = generatePrimitiveCollectionDeleteQuery(field, ListExt.asList(_this));
				query.addPreQuery(priDel);
				D3EQuery priColl = generatePrimitiveCollectionCreateQuery(field, (List) field.getValue(_this), _this);
				query.addNextQuery(priColl);
				break;
			case ReferenceCollection:
				D3EQuery refDel = generateReferenceCollectionDeleteQuery(field, ListExt.asList(_this));
				query.addPreQuery(refDel);
				List values = (List) field.getValue(_this);
				if (field.isChild()) {
					for (Object v : values) {
						D3EQuery chq = generateCreateQuery(field.getReference(), (DatabaseObject) v);
						query.addPreQuery(chq);
					}
					ListChanges old = (ListChanges) _this._oldValue(field.getIndex());
					D3EQuery chq = generateMultiDeleteQuery(old.getOld());
					query.addPreQuery(chq);
				}
				D3EQuery refColl = generateReferenceCollectionCreateQuery(field, values, _this);
				query.addNextQuery(refColl);
				break;
			default:
				break;
			}
		}
	}

	private D3EQuery generateReferenceCollectionDeleteQuery(DField<?, ?> field, List masterList) {
		return generatePrimitiveCollectionDeleteQuery(field, masterList);
	}

	private D3EQuery generatePrimitiveCollectionDeleteQuery(DField<?, ?> field, List masterList) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ").append(field.getCollTableName(null));
		sb.append(" where ");
		sb.append(field.declType().toColumnName()).append(" in (");
		sb.append(ListExt.join(ListExt.map(masterList, v -> ((DBObject) v).getId()), ", "));
		sb.append(")");
		D3EQuery query = new D3EQuery();
		query.setArgs(new ArrayList<>());
		query.setQuery(sb.toString());
		return query;
	}

	public D3EQuery generateDeleteQuery(DModel<?> type, DatabaseObject _this) {
		D3EQuery query = new D3EQuery();
		addDeleteColumns(query, type, ListExt.asList(_this));
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ").append(type.getTableName()).append(" where _id = ").append(_this.getId());
		query.setQuery(sb.toString());
		if (type.getParent() != null) {
			query.addNextQuery(generateDeleteQuery(type.getParent(), _this));
		}
		return query;
	}

	public D3EQuery generateMultiDeleteQuery(List<DatabaseObject> values) {
		if (values.isEmpty()) {
			return null;
		}
		// Group by types and it's parents;
		Map<Integer, List<DatabaseObject>> groupByTypes = new HashMap<>();
		for (DatabaseObject v : values) {
			DModel<?> type = schema.getType(v._typeIdx());
			while (type != null) {
				List<DatabaseObject> list = groupByTypes.get(type.getIndex());
				if (list == null) {
					list = new ArrayList<>();
					groupByTypes.put(type.getIndex(), list);
				}
				list.add(v);
				type = type.getParent();
			}
		}
		D3EQuery query = new D3EQuery();
		groupByTypes.forEach((t, list) -> {
			DModel<?> type = schema.getType(t);
			addDeleteColumns(query, type, list);
			StringBuilder sb = new StringBuilder();
			sb.append("delete from ").append(type.getTableName()).append(" where _id in (");
			sb.append(ListExt.join(ListExt.map(list, v -> v.getId()), ", "));
			sb.append(")");
			D3EQuery q = new D3EQuery();
			q.setQuery(sb.toString());
			query.addPreQuery(q);
		});
		return query.pre;
	}

	private void addDeleteColumns(D3EQuery query, DModel<?> type, List multiValues) {
		for (DField field : type.getFields()) {
			if (field.getType() == FieldType.InverseCollection) {
				continue;
			}
			switch (field.getType()) {
			case Reference:
				if (!field.getReference().isEmbedded() && field.isChild()) {
					List childs = new ArrayList<>();
					for (Object o : multiValues) {
						Object value = field.getValue(o);
						if (value != null) {
							childs.add(value);
						}
					}
					D3EQuery chq = generateMultiDeleteQuery(childs);
					query.addPreQuery(chq);
				}
				break;
			case PrimitiveCollection:
				D3EQuery priDel = generatePrimitiveCollectionDeleteQuery(field, multiValues);
				query.addPreQuery(priDel);
				break;
			case ReferenceCollection:
				D3EQuery refDel = generateReferenceCollectionDeleteQuery(field, multiValues);
				query.addPreQuery(refDel);
				if (field.isChild()) {
					List childs = new ArrayList<>();
					for (Object o : multiValues) {
						Object value = field.getValue(o);
						if (value != null) {
							childs.addAll((List<DatabaseObject>) value);
						}
					}
					D3EQuery chq = generateMultiDeleteQuery(childs);
					query.addPreQuery(chq);
				}
				break;
			default:
				break;
			}
		}

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
				if (ref.isDocument()) {
					sb.append(", ").append(alias).append(".").append(df.getColumnName());
					selectedFields.add(new RowField(df));
				} else if (ref.isEmbedded()) {
					List<RowField> subFields = new ArrayList<>();
					appendAllColumns(sb, ref, subFields, joins, ag, alias);
					selectedFields.add(new RowField(df, subFields));
				} else {
					sb.append(", ").append(alias).append(".").append(df.getColumnName());
					selectedFields.add(new RowField(df));
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
