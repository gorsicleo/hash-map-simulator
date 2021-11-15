package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;

/**
 * ElementGetter is used as practical elements getter from collection. Note that
 * elements are loaded from collection when object is instantiated and in case
 * of structural change over the collection it was created uppon,
 * {@link ConcurrentModificationException} will be invoked!
 * 
 * @param T - the type of elements that will elements getter handle
 * 
 * @author gorsicleo
 *
 */
public interface ElementsGetter<T> {

	/**
	 * Checks if there are still undelivered elements. Note that calling this method
	 * alone does not decrease (or consume) number of available elements for
	 * delivery.
	 * 
	 * @return true if there is next element available for delivery. False
	 *         otherwise.
	 */
	boolean hasNextElement();

	/**
	 * Returns next object that is waiting for delivery. In case of no available
	 * elements for delivery exception will be invoked!
	 * 
	 * @return Object that is next for delivery
	 */
	T getNextElement();

	/**Calls processor's process method for each undelivered object.
	 * @param p processor.
	 */
	default void processRemaining(Processor<T> p) {
		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}

}
