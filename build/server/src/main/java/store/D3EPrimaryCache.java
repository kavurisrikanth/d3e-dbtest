package store;

import java.util.HashMap;
import java.util.Map;

import gqltosql.schema.DField;
import gqltosql.schema.DModel;
import gqltosql.schema.FieldType;

public class D3EPrimaryCache {

	private Map<Integer, Map<Long, DatabaseObject>> data = new HashMap<>();

	public DatabaseObject get(int type, long id) {
		Map<Long, DatabaseObject> byType = data.get(type);
		if (byType == null) {
			return null;
		}
		return byType.get(id);
	}

	public void add(DatabaseObject ins, int type) {
		Map<Long, DatabaseObject> byType = data.get(type);
		if (byType == null) {
			byType = new HashMap<>();
			data.put(type, byType);
		}
		byType.put(ins.getId(), ins);
	}

	public DatabaseObject getOrCreate(DModel<?> dm, long id) {
		if (id == 0l) {
			return null;
		}
		DatabaseObject obj = get(dm.getIndex(), id);
		if (obj == null) {
			DatabaseObject ins = (DatabaseObject) dm.newInstance();
			ins.setId(id);
			ins._markProxy();
			ins.postLoad();
			ins.setSaveStatus(DBSaveStatus.Saved);
			markCollectionsAsProxy(dm, ins);
			add(ins, dm.getIndex());
			return ins;
		}
		return obj;
	}

	private void markCollectionsAsProxy(DModel<?> dm, DatabaseObject ins) {
		for (DField df : dm.getFields()) {
			FieldType type = df.getType();
			switch (type) {
			case InverseCollection:
			case PrimitiveCollection:
			case ReferenceCollection:
				D3EPersistanceList list = (D3EPersistanceList) df.getValue(ins);
				list._markProxy();
			}
		}
		if (dm.getParent() != null) {
			markCollectionsAsProxy(dm.getParent(), ins);
		}
	}

}
