package pt.inesc.id.l2f.annotation.document.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;

/**
 * Abstract base class for MapWritable.
 * 
 * 
 * @author Tiago Luis
 *
 */
public abstract class AbstractMapWritable implements Writable, Configurable {
	private Configuration _conf;

	// class to id mappings
	@SuppressWarnings("unchecked")
	private Map<Class, Byte> classToIdMap = new HashMap<Class, Byte>();

	// id to class mappings
	@SuppressWarnings("unchecked")
	private Map<Byte, Class> idToClassMap = new HashMap<Byte, Class>();

	// the number of new classes (those not established by the constructor)
	private volatile byte newClasses = 0;

	public AbstractMapWritable() {
		this._conf = new Configuration();
	}

	/**
	 * 
	 * 
	 * @return the number of known classes
	 */
	byte getNewClasses() {
		return newClasses;
	}

	/**
	 * Used to add "predefined" classes and by Writable to copy "new" classes.
	 * 
	 * @param clazz
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	private synchronized void addToMap(Class clazz, byte id) {

		if (classToIdMap.containsKey(clazz)) {
			byte b = classToIdMap.get(clazz);
			if (b != id) {
				throw new IllegalArgumentException ("Class " + clazz.getName() + " already registered but maps to " + b + " and not " + id);
			}
		}
		if (idToClassMap.containsKey(id)) {
			Class c = idToClassMap.get(id);
			if (!c.equals(clazz)) {
				throw new IllegalArgumentException("Id " + id + " exists but maps to " + c.getName() + " and not " + clazz.getName());
			}
		}

		classToIdMap.put(clazz, id);
		idToClassMap.put(id, clazz);
	}

	/**
	 * Add a Class to the maps if it is not already present.
	 * 
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	protected synchronized void addToMap(Class clazz) {

		if (classToIdMap.containsKey(clazz)) {
			return;
		}

		if (newClasses + 1 > Byte.MAX_VALUE) {
			throw new IndexOutOfBoundsException("adding an additional class would" + " exceed the maximum number allowed");
		}

		byte id = ++newClasses;
		this.addToMap(clazz, id);
	}

	/**
	 * 
	 * 
	 * @param id
	 * @return the Class class for the specified id
	 */
	@SuppressWarnings("unchecked")
	protected Class getClass(byte id) {
		return idToClassMap.get(id);
	}

	/**
	 * 
	 * 
	 * @param clazz
	 * @return the id for the specified Class
	 */
	@SuppressWarnings("unchecked")
	protected byte getId(Class clazz) {
		return classToIdMap.containsKey(clazz) ? classToIdMap.get(clazz) : -1;
	}

	public Configuration getConf() {
		return _conf;
	}

	public void setConf(Configuration conf) {
		_conf = conf;
	}

	public void write(DataOutput out) throws IOException {
		// first write out the size of the class table and any classes that are "unknown" classes
		out.writeByte(newClasses);

		for (byte i = 1; i <= newClasses; i++) {
			out.writeByte(i);
			out.writeUTF(getClass(i).getName());
		}
	}

	public void readFields(DataInput in) throws IOException {
		// get the number of "unknown" classes
		newClasses = in.readByte();

		// then read in the class names and add them to our tables
		for (int i = 0; i < newClasses; i++) {
			byte id = in.readByte();
			String className = in.readUTF();

			try {
				addToMap(Class.forName(className), id);
			} catch (ClassNotFoundException e) {
				throw new IOException("can't find class: " + className + " because "+ e.getMessage());
			}
		}
	}    
}
