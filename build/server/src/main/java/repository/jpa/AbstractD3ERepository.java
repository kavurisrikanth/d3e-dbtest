package repository.jpa;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import store.D3EEntityManagerProvider;
import store.DatabaseObject;
import store.IEntityManager;

public abstract class AbstractD3ERepository<T extends DatabaseObject> implements D3ERepository<T> {

	@Autowired
	private D3EEntityManagerProvider provider;

	protected IEntityManager em() {
		return this.provider.get();
	}

	protected abstract int getTypeIndex();

	public T findById(long id) {
		return em().getById(getTypeIndex(), id);
	}

	public T getOne(long id) {
		return em().find(getTypeIndex(), id);
	}

	public List<T> findAll() {
		return em().findAll(getTypeIndex());
	}

	protected boolean checkUnique(Query query) {
		return (boolean) query.getSingleResult();
	}

	protected T getXByY(Query query) {
		// y is a unique property in model X
		return (T) query.getSingleResult();
	}

	protected List<T> getAllXsByY(Query query) {
		// getResultList should return List of the proper type since we are only requesting id
		return query.getResultList();
	}

	protected Object getOldValue(Query query) {
		return query.getSingleResult();
	}
}
