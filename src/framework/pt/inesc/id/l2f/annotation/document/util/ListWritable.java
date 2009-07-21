package pt.inesc.id.l2f.annotation.document.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * A Writable Map.
 */
public class ListWritable<K extends Writable> extends AbstractListWritable implements List<K> {
	private List<K> _list;

	/** Default constructor. */
	public ListWritable() {
		super();
		
		_list = new ArrayList<K>();
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);

		// write out the number of entries in the map
		out.writeInt(_list.size());

		// then write out each key/value pair
		for (Writable e: _list) {
			out.writeByte(getId(e.getClass()));
			e.write(out);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);

		// first clear the map (otherwise we will just accumulate entries every time this method is called)
		this._list.clear();

		// read the number of entries in the map
		int entries = in.readInt();

		// then read each key/value pair
		for (int i = 0; i < entries; i++) {
			K value = (K) ReflectionUtils.newInstance(getClass(in.readByte()), getConf());
			value.readFields(in);
			
			_list.add(value);
		}
	}

	public void add(int index, K element) {
		super.addToList(element.getClass());
		
		_list.add(index, element);
	}

	public boolean contains(Object o) {
		return _list.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return _list.containsAll(c);
	}

	public K get(int index) {
		return _list.get(index);
	}

	public int indexOf(Object o) {
		return _list.indexOf(o);
	}

	public Iterator<K> iterator() {
		return _list.iterator();
	}

	public int lastIndexOf(Object o) {
		return _list.lastIndexOf(o);
	}

	public ListIterator<K> listIterator() {
		return _list.listIterator();
	}

	public ListIterator<K> listIterator(int index) {
		return _list.listIterator(index);
	}

	public K remove(int index) {
		return _list.remove(index);
	}

	public boolean removeAll(Collection<?> c) {
		return _list.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return _list.retainAll(c);
	}

	public K set(int index, K element) {
		return _list.set(index, element);
	}

	public List<K> subList(int fromIndex, int toIndex) {
		return _list.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return _list.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return _list.toArray(a);
	}

	public boolean add(K e) {
		super.addToList(e.getClass());
		
		return _list.add(e);
	}

	public void clear() {
		_list.clear();
	}

	public boolean isEmpty() {
		return _list.isEmpty();
	}

	public boolean remove(Object o) {
		return _list.remove(o);
	}

	public int size() {
		return _list.size();
	}

	public boolean addAll(Collection<? extends K> c) {
		throw new RuntimeException("Not implemented");
	}

	public boolean addAll(int index, Collection<? extends K> c) {
		throw new RuntimeException("Not implemented");
	}
}
