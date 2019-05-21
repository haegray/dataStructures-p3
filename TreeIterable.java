/**
 * @author Helena Gray
 * @version 11.4.2018
 * This class provides an interface for the iterators seen in KTree. */
import java.util.Iterator;

interface TreeIterable<T> {
	public Iterator<T> getLevelOrderIterator();
	public Iterator<T> getPreOrderIterator();
	public Iterator<T> getPostOrderIterator();
}