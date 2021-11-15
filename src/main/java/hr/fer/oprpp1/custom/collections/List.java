package hr.fer.oprpp1.custom.collections;

/**
 * An ordered collection.The user of this interface has precise control over
 * where in the list each element is inserted. The user can access elements by
 * their integer index (position in the list).
 * 
 * @author Leo Goršić
 *
 */
public interface List<T> extends Collection<T> {

	/**
	 * Returns <code>object</code> that is saved at given <code>index</code>
	 * position in <code>List</code>
	 * 
	 * @param index
	 * @return Object at that <code>index</code>
	 * @throws IndexOutOfBoundsException if <code>index</code> is smaller than 0 or greater than
	 *                                   size - 1
	 */
	T get(int index);

	/**Inserts <code>value</code> at given <code>position</code>
	 * @param value to be inserted
	 * @param position in list to insert <code>value</code>
	 * 
	 */
	void insert(T value, int position);

	/**Returns index of given <code>value</code>.
	 * @param value
	 * @return index of <code>value</code>, or -1 if <code>value</code> cannot be found.
	 */
	int indexOf(Object value);

	/**Removes element at given <code>index</code>
	 * @param index of object that will be removed
	 * @throws IndexOutOfBoundsException if <code>index</code> is smaller than 0 or greater than
	 *                                   size - 1
	 */
	void remove(int index);
}
