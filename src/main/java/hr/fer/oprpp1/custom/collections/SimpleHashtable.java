package hr.fer.oprpp1.custom.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Models simple hash map that can be iterated.
 * 
 * @author gorsicleo
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	private static final String NULL_KEY_ERROR = "Key must not be null";
	private static final String SIZE_TOO_SMALL_ERROR = "Size must not be smaller than 1";

	/**
	 * Class that models single key-value pair for maps or dictionaries.
	 * 
	 * @author gorsicleo
	 *
	 * @param <K> the type of keys in this entry
	 * @param <V> the type of values in this entry
	 */
	public static class TableEntry<K, V> {
		private K key;
		private V value;
		private TableEntry<K, V> next;

		/**
		 * Constructor. Creates new key-value pair.
		 * 
		 * @param key   to be stored
		 * @param value to be stored
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * Returns key in this key-value pair
		 * 
		 * @return key in this key-value pair.
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Returns value in this key-value pair
		 * 
		 * @return value in this key-value pair.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets value for this key-value pair
		 * 
		 * @param value that will overwrite old value stored in this key-value pair.
		 */
		public void setValue(V value) {
			this.value = value;
		}
	}

	private class IteratorImpl implements Iterator<TableEntry<K, V>> {

		/** Last visited slot in array of entries */
		private int lastVisitedSlot = 0;

		/** Last visited key-value pair in current slot */
		private TableEntry<K, V> lastVisitedEntry = null;

		private int savedModificationCount;

		/** Indicates if current element is removed */
		private boolean isLastRemoved = false;

		/**
		 * Constructor. Creates new Iterator for SimpleHashTable.
		 * 
		 * @param modificationCount of collection.
		 */
		public IteratorImpl(int modificationCount) {
			savedModificationCount = modificationCount;
		}

		/**
		 * @throws ConcurrentModificationException if collection has been modified after
		 *                                         iterator is being created
		 */
		private void checkForModifications() {
			if (modificationCount != savedModificationCount) {
				throw new ConcurrentModificationException();
			}
		}

		@Override
		public boolean hasNext() {
			checkForModifications();
			int savedLastVisitedSlot = lastVisitedSlot;
			if (lastVisitedEntry != null && lastVisitedEntry.next != null) {
				return true;
			} else {
				savedLastVisitedSlot++;
				while (savedLastVisitedSlot < table.length) {
					if (table[savedLastVisitedSlot] == null) {
						savedLastVisitedSlot++;
					} else {
						return true;
					}
				}
				return false;
			}

		}

		@Override
		public TableEntry<K, V> next() {
			checkForModifications();
			if (lastVisitedEntry != null && lastVisitedEntry.next != null) {
				lastVisitedEntry = lastVisitedEntry.next;
				isLastRemoved = false;
				return lastVisitedEntry;
			} else {
				lastVisitedSlot++;
				while (lastVisitedSlot < table.length) {
					if (table[lastVisitedSlot] == null) {
						lastVisitedSlot++;
					} else {
						lastVisitedEntry = table[lastVisitedSlot];
						isLastRemoved = false;
						return lastVisitedEntry;
					}
				}
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			checkForModifications();
			if (isLastRemoved == true) {
				throw new IllegalStateException();
			} else {
				SimpleHashtable.this.remove(lastVisitedEntry.key);
				isLastRemoved = true;
				savedModificationCount++;
			}
		}

	}

	/** Array in which entries are stored */
	private TableEntry<K, V>[] table;

	/** Number of key-value pairs stored in this map */
	private int size = 0;

	/** Stores number of structural modifications on this hash map. */
	private int modificationCount = 0;

	/** Creates new SimpleHashTeable object with storage array of 16 slots. */
	@SuppressWarnings("unchecked")
	public SimpleHashtable() {
		table = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, 16);
	}

	/**
	 * Creates new SimpleHashTeable object with storage array of size.
	 * 
	 * @param size of internal storage array. <b>Note: size will be rounded to
	 *             nearest power of two.</b>
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int size) {
		if (size < 1) {
			throw new IllegalArgumentException(SIZE_TOO_SMALL_ERROR);
		}

		int nearestPowerOfTwo = 1;
		while (nearestPowerOfTwo < size) {
			nearestPowerOfTwo = nearestPowerOfTwo << 1;
		}

		table = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, nearestPowerOfTwo);
	}

	/**
	 * Returns number of elements stored in hash map.
	 * 
	 * @return number of elements stored in hash map.
	 */
	public int size() {
		return size;
	}

	/**
	 * Stores new or overwrites existing key-value pair in hash map.
	 * 
	 * @param key   to be stored in dictionary. <b>Must not be null.</b>
	 * @param value to be stored in dictionary. <b>Can be null.</b>
	 * @return value that was previously stored for that key. In case of new entry
	 *         null is returned.
	 * @throws NullPointerException if given key is null.
	 */
	public V put(K key, V value) {
		checkKeyValidity(key);
		if (Double.valueOf(size) / Double.valueOf(table.length) >= 0.75) {
			doubleTableSize();
		}
		size++;
		modificationCount++;
		return putEntryToTable(key, value);
	}

	/**
	 * Manager for storing elements in hash map. If key exists its value will be
	 * overwritten, otherwise new pair will be appended to the end of table.
	 * 
	 * @param key   to be stored.
	 * @param value to be stored.
	 * @return value that was previously stored for given key.
	 */
	private V putEntryToTable(K key, V value) {
		int slot = calculateSlot(key);
		TableEntry<K, V> foundEntry = findEntry(slot, key);

		if (foundEntry == null) {
			putEntryOnEnd(slot, key, value);
			return null;
		} else {
			return overwriteEntry(value, foundEntry);
		}

	}

	/**
	 * Overwrites found tableEntry with new given <code>value</code>.
	 * 
	 * @param value      to overwrite existing.
	 * @param foundEntry to be overwritten.
	 * @return value that was previously stored for given key.
	 */
	private V overwriteEntry(V value, TableEntry<K, V> foundEntry) {
		size--;
		modificationCount--;
		V previousValue = foundEntry.value;
		foundEntry.value = value;
		return previousValue;
	}

	/**
	 * Creates new array for storing key-value pairs but size two times bigger than
	 * previous array. <b>All elements are copied into new array but with new slot
	 * numbers!</b>
	 */
	@SuppressWarnings("unchecked")
	private void doubleTableSize() {
		TableEntry<K, V>[] oldEntries = toArray();
		table = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, table.length * 2);
		for (TableEntry<K, V> entry : oldEntries) {
			if (entry != null) {
				putEntryToTable(entry.key, entry.value);
			}
		}
	}

	/**
	 * Returns true if given key can be found in hash map.
	 * 
	 * @param key <b>must not be null!</b>
	 * @return true if key exists in hash map.
	 */
	public boolean containsKey(Object key) {
		checkKeyValidity(key);
		int slot = calculateSlot(key);
		TableEntry<K, V> currentEntry = table[slot];

		while (currentEntry != null) {
			if (currentEntry.key.equals(key)) {
				return true;
			} else {
				currentEntry = currentEntry.next;
			}
		}
		return false;
	}

	/**
	 * Returns true if given value can be found in hash map.
	 * 
	 * @param key <b>can be null!</b>
	 * @return true if value exists in hash map.
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> currentEntry : table) {

			while (currentEntry != null) {
				if (currentEntry.value.equals(value)) {
					return true;
				} else {
					currentEntry = currentEntry.next;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if there are no key-value pairs stored in this hash map.
	 * 
	 * @return true if hash map is empty.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns string representation of this hash map in format of [key1=value1,
	 * key2=value2, ... ]
	 */
	@Override
	public String toString() {
		String string = new String("[");
		for (TableEntry<K, V> currentEntry : table) {
			while (currentEntry != null) {
				string += currentEntry.key + "=" + currentEntry.value + ", ";
				currentEntry = currentEntry.next;
			}
		}
		return string.substring(0, string.length() - 2) + "]";
	}

	/**
	 * Creates array filled with key-value pairs that are stored in this hash map.
	 * 
	 * @return new array filled with all key-value pairs from this hash-map.
	 */
	@SuppressWarnings("unchecked")
	public TableEntry<K, V>[] toArray() {
		TableEntry<K, V>[] array = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, size);
		int index = 0;
		for (TableEntry<K, V> currentEntry : table) {
			while (currentEntry != null) {
				array[index++] = currentEntry;
				currentEntry = currentEntry.next;
			}
		}
		return array;
	}

	/**
	 * Removes key-value pair from hash map for given key. If key cannot be found
	 * nothing is removed and null value is returns. Otherwise pair is removed and
	 * its value is returned.
	 * 
	 * @param key from key-value pair that needs to be removed.
	 * @return value of removed key-value pair.
	 */
	public V remove(Object key) {
		if (key == null) {
			return null;
		}
		int slot = calculateSlot(key);
		TableEntry<K, V> currentEntry = table[slot];
		if (currentEntry.key.equals(key)) {
			V value = table[slot].value;
			table[slot] = table[slot].next;
			size--;
			modificationCount++;
			return value;
		}

		while (currentEntry.next != null) {
			if (currentEntry.next.key.equals(key)) {
				V value = currentEntry.next.value;
				currentEntry.next = currentEntry.next.next;
				size--;
				modificationCount++;
				return value;
			} else {
				currentEntry = currentEntry.next;
			}
		}

		return null;
	}

	/**Returns value that is stored in pair with given key in this hash map.
	 * @param key for value that needs to be returned.
	 * @return value to return for given key.
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		if (key == null) {
			return null;
		}
		int slot = calculateSlot(key);
		try {
			TableEntry<K, V> foundEntry = findEntry(slot, (K) key);
			return (foundEntry == null) ? null : foundEntry.value;
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**Creates new entry and stores it in array (at the beginning if slot is empty, otherwise at the end of list in that slot.)
	 * @param slot place in array to store new entry
	 * @param key for new entry
	 * @param value for new entry
	 */
	private void putEntryOnEnd(int slot, K key, V value) {
		if (table[slot] == null) {
			table[slot] = new TableEntry<K, V>(key, value);
		} else {
			TableEntry<K, V> currentEntry = table[slot];
			while (currentEntry.next != null) {
				currentEntry = currentEntry.next;
			}
			currentEntry.next = new TableEntry<K, V>(key, value);
		}
	}

	/**Uses hashCode function of given key and calculates slot for array.
	 * @param key to calculate slot for
	 * @return number in range 0-table.length-1
	 */
	private int calculateSlot(Object key) {
		checkKeyValidity(key);
		return Math.abs(key.hashCode()) % table.length;
	}

	
	/**Iterates over all entires in slot until it finds entry with the same key.*/
	private TableEntry<K, V> findEntry(int slot, K key) {
		TableEntry<K, V> currentEntry = table[slot];

		while (currentEntry != null && !(key.equals(currentEntry.key))) {
			currentEntry = currentEntry.next;
		}

		return currentEntry;
	}

	/**Validator for key - <b>key must not be null!</b>
	 * @throws NullPointerException if key is null*/
	private static void checkKeyValidity(Object key) {
		if (key == null) {
			throw new NullPointerException(NULL_KEY_ERROR);
		}
	}

	/**Removes all elements from hash maps and puts null <b>it doesn't the change size of internal array.</b>
	 * 
	 */
	public void clear() {
		Arrays.fill(table, null);
		size = 0;
	}

	/**Returns the iterator for this SimpleHashTable*/
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl(modificationCount);
	}

}
