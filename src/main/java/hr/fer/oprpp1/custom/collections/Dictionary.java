package hr.fer.oprpp1.custom.collections;


/**An object that maps keys to values. A dictionary cannot contain duplicate keys; each key can map to at most one value.
 * @author gorsicleo
 *
 * @param <K>  the type of keys maintained by this map
 * @param <V>  the type of mapped values
 */
public class Dictionary<K,V> {

	/**Model for single key-value pair in dictionary.
	 * @author User
	 *
	 * @param <K>  the type of keys maintained by this map
	 * @param <V>  the type of mapped values
	 */
	public static class DictonaryEntry<K,V> {
		private static final String KEY_NULL_ERROR = "Key must not be null.";
		private K key;
		private V value;
		
		/**Constructor.
		 * @param key - must <b>not</b> be null
		 * @param value
		 * @throws NullPointerException if key is null
		 */
		public DictonaryEntry(K key,V value) {
			if (key == null) {
				throw new NullPointerException(KEY_NULL_ERROR);
			}
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean equals(Object other){
		    if (other instanceof DictonaryEntry<?,?>){
		        if ( ((DictonaryEntry<?,?>)other).key.equals(key) ){
		            return true;
		        }
		    }
		    return false;
		}
	}
	
	/**Array used to store key-value pairs (dictionary entries)*/
	private ArrayIndexedCollection<DictonaryEntry<K, V>> dictionary;
	private int size;
	
	/**Constructor. Creates new Dictionary object. 
	 * 
	 */
	public Dictionary(){
		dictionary = new ArrayIndexedCollection<Dictionary.DictonaryEntry<K,V>>();
		size = 0;
	}
	
	
	/**Returns true if dictionary is empty.
	 * @return true if dictionary does not contain any key-value pairs, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
		
	}
	
	/**Returns number of key-value pairs stored in dictionary.
	 * @return size of dictionary.
	 */
	public int size() {
		return size;
	}
	
	/**Deletes all key-value pairs stored in dictionary and sets size to 0.*/
	public void clear() {
		dictionary.clear();
		size = 0;
	}
	
	/**Removes key-value pair from dictionary for given key.
	 * @param key to be removed, together with its value. <b>Null key is not allowed.</b>
	 * @return value that was removed from dictionary.
	 * @throws NullPointerException if given key is null.
	 */
	public V remove(K key) {
		int index = dictionary.indexOf(new DictonaryEntry<K, V>(key, null));
		if (index == -1) {
			return null;
		} else {
			V value = dictionary.get(index).value;
			dictionary.remove(index);
			size--;
			return value;		
		}
	}
	
	/**Stores new or overwrites existing key-value pair in dictionary.
	 * @param key to be stored in dictionary. <b>Must not be null.</b>
	 * @param value to be stored in dictionary. <b>Can be null.</b>
	 * @return value that was previously stored for that key. In case of new entry null is returned.
	 * @throws NullPointerException if given key is null.
	 */
	public V put(K key, V value) {
		int index = dictionary.indexOf(new DictonaryEntry<K, V>(key, null));
		if (index == -1) {
			dictionary.add(new DictonaryEntry<K,V>(key, value));
			size++;
			return null;
		} else {
			DictonaryEntry<K, V> oldEntry = dictionary.get(index);
			dictionary.remove(index);
			dictionary.insert(new DictonaryEntry<K,V>(key, value), index);
			size++;
			return oldEntry.value;
		}
	}
	
	/**Returns value for given key. If key cannot be found null is returned.
	 * @param key must not be null!
	 * @return value for given key, null if key can't be found in dictionary.
	 * @throws NullPointerException if given key is null.
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		try {
			DictonaryEntry<K,V> searchEntry = new DictonaryEntry<K,V>((K) key, null);
			int index = dictionary.indexOf(searchEntry);
			return (index == -1)? null: dictionary.get(index).value;
		} catch (ClassCastException e) {
			return null;
		}
		
		
	}
	
}