package hr.fer.oprpp1.custom.collections;

/**
 * Stack-like collection.
 * @author User
 */
public class ObjectStack<T> {

	/** Used collection for memory */
	private ArrayIndexedCollection<T> stack;

	/** Counts number of elements that are currently in stack*/
	int occupied;

	/**Constructor. Creates new empty ObjectStack instance.*/
	public ObjectStack() {
		stack = new ArrayIndexedCollection<T>();
		occupied = 0;

	}

	/**
	 * Returns true if collection is empty, false otherwise.
	 * 
	 * @return true if array is empty.
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * Returns size of the stack.
	 * 
	 * @return size of the stack.
	 */
	public int size() {
		return occupied;
	}

	/**
	 * Pushes non-value <code>value</code> on top of the stack
	 * 
	 * @throws NullPointerException if <code>value</code> is null.
	 * @param value
	 */
	public void push(T value) {
		stack.add(value);
		occupied++;
	}

	/**
	 * Removes last value pushed on stack from stack and returns it.
	 * 
	 * @return Object which is last value that is pushed on stack.
	 * @throws EmptyStackException when stack is empty.
	 */
	public T pop() {
		if (occupied == 0) {
			throw new EmptyStackException();
		}
		T objectToPop = stack.get(occupied - 1);
		stack.remove(occupied - 1);
		occupied--;
		return objectToPop;
	}

	/**
	 * Similar as pop; returns last element placed on stack but does not delete it
	 * from stack.
	 * 
	 * @return Object which is last value that is pushed on stack.
	 * @throws EmptyStackException when stack is empty.
	 */
	public T peek() {
		if (occupied == 0) {
			throw new EmptyStackException();
		}
		return stack.get(occupied - 1);
	}

	/** Removes all elements from stack. */
	public void clear() {
		stack.clear();
	}

}
