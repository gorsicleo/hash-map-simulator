package hr.fer.oprpp1.custom.collections;

/**Exception that should be invoked when user tries to pop, peek from empty stack collection.
 * @author gorsicleo
 */
@SuppressWarnings("serial")
public class EmptyStackException extends RuntimeException{

	/**Constructor.*/
	public EmptyStackException() {
		super();
	}
}
