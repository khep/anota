package pt.inesc.id.l2f.annotation.document.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * A Writable Map.
 */
public class MapWritable<K extends Writable, V extends Writable> extends AbstractMapWritable implements Map<K, V> {
	private Map<K, V> _map;

	/** Default constructor. */
	public MapWritable() {
		super();
		
		_map = new HashMap<K, V>();
	}

	/** {@inheritDoc} */
	public void clear() {
		_map.clear();
	}

	/** {@inheritDoc} */
	public boolean containsKey(Object key) {
		return _map.containsKey(key);
	}

	/** {@inheritDoc} */
	public boolean containsValue(Object value) {
		return _map.containsValue(value);
	}

	/** {@inheritDoc} */
	public Set<Map.Entry<K, V>> entrySet() {
		return _map.entrySet();
	}

	/** {@inheritDoc} */
	public V get(Object key) {
		return _map.get(key);
	}

	/** {@inheritDoc} */
	public boolean isEmpty() {
		return _map.isEmpty();
	}

	/** {@inheritDoc} */
	public Set<K> keySet() {
		return _map.keySet();
	}

	/** {@inheritDoc} */
	public V put(K key, V value) {
		addToMap(key.getClass());
		addToMap(value.getClass());
		
		return _map.put(key, value);
	}

	/** {@inheritDoc} */
	public void putAll(Map<? extends K, ? extends V> t) {
		for (Map.Entry<? extends K, ? extends V> e: t.entrySet()) {
			_map.put(e.getKey(), e.getValue());
		}
	}

	/** {@inheritDoc} */
	public V remove(Object key) {
		return _map.remove(key);
	}

	/** {@inheritDoc} */
	public int size() {
		return _map.size();
	}

	/** {@inheritDoc} */
	public Collection<V> values() {
		return _map.values();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);

		// Write out the number of entries in the map

		out.writeInt(_map.size());

		// Then write out each key/value pair

		for (Map.Entry<K, V> e: _map.entrySet()) {
			out.writeByte(getId(e.getKey().getClass()));
			e.getKey().write(out);
			out.writeByte(getId(e.getValue().getClass()));
			e.getValue().write(out);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);

		// first clear the map (otherwise we will just accumulate entries every time this method is called)
		this._map.clear();

		// read the number of entries in the map
		int entries = in.readInt();

		// then read each key/value pair
		for (int i = 0; i < entries; i++) {
			K key = (K) ReflectionUtils.newInstance(getClass(in.readByte()), getConf());
			key.readFields(in);

			V value = (V) ReflectionUtils.newInstance(getClass(in.readByte()), getConf());
			value.readFields(in);
			
			_map.put(key, value);
		}
	}
}
