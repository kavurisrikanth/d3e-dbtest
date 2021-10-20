package store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class D3EPersistanceList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int field;
	private DatabaseObject master;
	private transient boolean proxy;

	public D3EPersistanceList(DatabaseObject master, int field) {
		this.master = master;
		this.field = field;
	}
	
	public DatabaseObject getMaster() {
		return master;
	}
	
	public int getField() {
		return field;
	}
	
	public void _markProxy() {
		this.proxy = true;
	}
	
	private void _checkProxy() {
		if(this.proxy) {
			Database.get().unproxyCollection(this);
			this.proxy = false;
		}
	}

	private void _fieldChanged() {
		master.collFieldChanged(field, this);
	}

	@Override
	public int size() {
		_checkProxy();
		return super.size();
	}

	@Override
	public boolean isEmpty() {
		_checkProxy();
		return super.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		_checkProxy();
		return super.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		_checkProxy();
		return super.iterator();
	}

	@Override
	public Object[] toArray() {
		_checkProxy();
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		_checkProxy();
		return super.toArray(a);
	}

	@Override
	public boolean add(E e) {
		_checkProxy();
		boolean res = super.add(e);
		if (res) {
			_fieldChanged();
		}
		return res;
	}

	@Override
	public boolean remove(Object o) {
		_checkProxy();
		boolean res = super.remove(o);
		if (res) {
			_fieldChanged();
		}
		return res;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		_checkProxy();
		return super.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		_checkProxy();
		boolean res = super.addAll(c);
		if (res) {
			_fieldChanged();
		}
		return res;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		_checkProxy();
		boolean res = super.addAll(index, c);
		if (res) {
			_fieldChanged();
		}
		return res;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		_checkProxy();
		boolean res = super.removeAll(c);
		if (res) {
			_fieldChanged();
		}
		return res;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		_checkProxy();
		boolean res = super.retainAll(c);
		if (res) {
			_fieldChanged();
		}
		return res;
	}

	@Override
	public void clear() {
		_checkProxy();
		super.clear();
		_fieldChanged();

	}

	@Override
	public E get(int index) {
		_checkProxy();
		return super.get(index);
	}

	@Override
	public E set(int index, E element) {
		_checkProxy();
		E res = super.set(index, element);
		_fieldChanged();
		return res;
	}

	@Override
	public void add(int index, E element) {
		_checkProxy();
		super.add(index, element);
		_fieldChanged();
	}

	@Override
	public E remove(int index) {
		_checkProxy();
		E res = super.remove(index);
		_fieldChanged();
		return res;
	}

	@Override
	public int indexOf(Object o) {
		_checkProxy();
		return super.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		_checkProxy();
		return super.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		_checkProxy();
		return super.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		_checkProxy();
		return super.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		_checkProxy();
		return super.subList(fromIndex, toIndex);
	}

}
