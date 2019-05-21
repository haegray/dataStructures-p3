
/**
 * @author Helena Gray
 * @version 11.4.2018
 * 
 * This class creates a tree of nodes and contains various methods
 * that allow for a variety of things including tree traversal,
 * adding, removing, or changing nodes, changing the tree to an array,
 * or printing the array to a string. It also includes a decode method
 * that can generate a secret message by traversing a tree whose
 * leave nodes contain letters.
 * */

import java.util.Iterator;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;

public class KTree<E> implements TreeIterable<E> {
	private Node<E> root;
	private int k;
	private int size;
	private int height;

	/**
	 * This class defines a node that makes up the KTree.
	 */
	private static class Node<E> {
		private E code;
		private int index;
		private Node<E> next;
		private Node<E> leftChild;
		private Node<E> rightSib;

		/**
		 * @param value
		 *            is the value of the code of the node
		 * @param valueIndex
		 *            is the index of the node in the tree
		 */
		public Node(E value, int valueIndex) {
			this.code = value;
			this.index = valueIndex;
		}

		/**
		 * @return the code of the node or its value
		 */
		public E getCode() {
			return code;
		}

		/**
		 * @return the index of the node
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @param value
		 *            the new value of the node
		 */
		public void setCode(E value) {
			this.code = value;
		}

		/**
		 * @param value
		 *            the new index of the node
		 */
		public void setIndex(int value) {
			this.index = value;
		}

		/**
		 * 
		 * @return the left child node of the current node
		 */
		public Node<E> getChild() {
			return this.leftChild;
		}

		/**
		 * @return the sibling node of the current node
		 */
		public Node<E> getSib() {
			return this.rightSib;
		}

		/**
		 * @return the next node of the current node. The next node is used in
		 *         the context of stacks and queues, rather than trees.
		 */
		public Node<E> getNext() {
			return this.next;
		}

		/**
		 * @param child
		 *            the desired child node of the current node
		 */
		public void setChild(Node<E> child) {
			this.leftChild = child;
		}

		/**
		 * 
		 * @param sib
		 *            the desired sibling node of the current node
		 */
		public void setSib(Node<E> sib) {
			this.rightSib = sib;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		/**
		 * @return a string of the code value of the node
		 */
		public String toString() {
			String code = "";
			if (this.getCode() instanceof Integer) {
				code = this.getCode().toString();
			} else {
				code = String.valueOf(this.getCode());
			}
			return code;
		}
	}

	/**
	 * This is the queue class. The queue is filled with nodes and operates in a
	 * FIFO manner.
	 */
	private static class Queue<E> {
		private Node<E> top, bottom;
		private int size;

		/**
		 * Queue class constructor
		 */
		public Queue() {
			this.top = null;
			this.bottom = top;
			size = 0;
		}

		/**
		 * @param item
		 *            the node entering the queue
		 */
		public void enqueue(Node<E> item) {
			Node oldBottom = bottom;
			bottom = item;
			bottom.setNext(null);
			if (top == null) {
				top = bottom;
			} else {
				oldBottom.setNext(bottom);
			}
			size++;
		}

		/**
		 * @return the node at the front of the queue
		 */
		public Node<E> dequeue() {
			if (top == null) {
				return null;
			}
			Node<E> n = top;
			top = top.getNext();
			if (top == null) {
				bottom = null;
			}
			size--;
			return n;
		}

		/**
		 * 
		 * @return if the queue is empty or not
		 */
		public boolean isEmpty() {
			// return whether or not the queue is empty
			// O(1)
			/**
			 * boolean truth = false; try { if (top.getCode() == null && size ==
			 * 0) { truth = true; } } catch (NullPointerException e) { if (size
			 * == 0) { truth = true; } } return truth;
			 */
			return top == null;
		}

		/**
		 * 
		 * @return node at front of queue without actually removing it from the
		 *         queue
		 */
		public Node<E> peek() {
			// return the top of the stack (but don't remove it)
			// if there are no items on the stack, return null
			// O(1)
			if (this.isEmpty()) {
				return null;
			} else {
				return top;
			}
		}
	}

	/**
	 * This is the stack class. The stack is filled with nodes and operates in a
	 * LIFO manner.
	 */
	private static class Stack<E> {
		private Node<E> top;
		private int size;

		/**
		 * Stack constructor method
		 */
		public Stack() {
			size = 0;
		}

		/**
		 * @param item
		 *            node to be added to the top of the stack
		 */
		public void push(Node<E> item) {
			// push an item onto the stack
			// O(1)
			Node n = item;
			if (top == null) {
				top = n;
				top.setNext(null);
			} else {
				n.setNext(top);
				top = n;
			}
			size++;
		}

		/**
		 * @return the node on the top of the stack and removes it from the
		 *         stack
		 */

		public Node<E> pop() {
			if (top == null) {
				return null;
			} else {
				Node oldTop = top;
				top = top.getNext();
				size--;
				return oldTop;
			}
		}

		/**
		 * @return the node on the top of the stack without removing it or
		 *         returns null if empty
		 */
		public Node<E> peek() {
			if (this.isEmpty()) {
				return null;
			} else {
				return top;
			}
		}

		/**
		 * @return the number of items on the stack
		 */
		public int size() {
			// return the number of items on the stack
			// O(1)
			return this.size;
		}

		/**
		 * @return if the stack is empty
		 */
		public boolean isEmpty() {
			// return whether or not the stack is empty
			// O(1)
			boolean truth = false;
			try {
				if (top.getCode() == null && size == 0) {
					truth = true;
				}
			} catch (NullPointerException e) {
				if (size == 0) {
					truth = true;
				}
			}
			return truth;
		}

	}

	/**
	 * This constructs the KTree using an input array and a branching factor k
	 * 
	 * @param arrayTree
	 *            this is the array that contains the values of the nodes for
	 *            the tree. Their position in the array indicates their
	 *            index/position in the tree.
	 * @param k
	 *            is the branching factor of the tree
	 */
	public KTree(E[] arrayTree, int k) {
		// populate tree
		this.k = k;
		if (k < 2) {
			throw new InvalidKException();
		}
		this.root = new Node(arrayTree[0], 0);
		Node current = root;
		if (arrayTree[0] != null) {
			this.height = 1;
			this.size = 1;
		} else {
			this.height = 0;
			this.size = 0;
		}
		for (int arrIndex = 0; arrIndex < arrayTree.length - 1; arrIndex++) {
			if (arrIndex % k != 0) {
				current.rightSib = new Node(arrayTree[arrIndex + 1], arrIndex + 1);
				current = current.rightSib;
			} else {
				current = root;
				int check = 0;
				while (check != (arrIndex / k)) {
					if ((current.getChild().getIndex() > (arrIndex / k)
							|| current.getChild().getSib().getIndex() < (arrIndex / k)) && current != root) {
						current = current.getSib();
					} else {
						current = current.getChild();
					}
					check = current.getIndex();
				}
				current.leftChild = new Node(arrayTree[arrIndex + 1], arrIndex + 1);
				current = current.leftChild;
			}

			if (arrayTree[arrIndex + 1] != null) {
				this.size++;
			}

		}

		// height
		height = 0;
		Stack stack1 = new Stack();
		Stack stack2 = new Stack();
		Stack stack3 = new Stack();
		Stack stack4 = new Stack();
		if (root.getCode() == null || root == null) {
			height = 0;
			return;
		}
		Node level = new Node(0, 0);
		stack1.push(root);
		stack3.push(level);
		while (!stack1.isEmpty()) {

			Node currentNode = stack1.pop();
			int currentLevel = Integer.valueOf(stack3.pop().toString());
			height = Math.max(height, currentLevel);
			if (currentNode.getChild() != null) {
				stack2.push(currentNode.getChild());
				Node levelCheckNode = currentNode.getChild();
				boolean addLevel = false;
				while(levelCheckNode!= null){
					if(levelCheckNode.getCode() !=null){
						addLevel = true;
						break;
					} else {
						levelCheckNode = levelCheckNode.getSib();
					}
				}
				if(addLevel){
					level = new Node(currentLevel + 1, 0);
				} else {
					level = new Node(currentLevel, 0);
				}
				stack4.push(level);
			}
			if (currentNode.getSib() != null) {
				stack1.push(currentNode.getSib());
				level = new Node(currentLevel, 0);
				stack3.push(level);
			} else {
				if (!stack2.isEmpty()) {
					stack1.push(stack2.pop());
					stack3.push(stack4.pop());
				}
			}
			height = Math.max(height, currentLevel);
		}
	}

	/**
	 * 
	 * @return the branching factor of the tree, k
	 */
	public int getK() {
		return this.k;
	}

	/**
	 * 
	 * @return the size of the tree or how many nodes there are in it
	 */
	public int size() {
		return this.size;
	}

	/**
	 * @return the height of the tree or the number of edges from the root to
	 *         the deepest leaf
	 */
	public int height() {
		return this.height;

	}

	/**
	 * 
	 * @param i
	 *            the index of the desired node
	 * @return the value of the node at the indicated index
	 */

	public E get(int i) {
		Node current = root;
		int rightMost = 0;
		while (current != null && current.getIndex() != i) {
			if ((current.getChild() == null || i < (current.getIndex() * k + 1) || i > ((current.getIndex() + 1) * k))
					&& current.getIndex() != 0 && current.getIndex() != rightMost) {
				current = current.getSib();
			} else {
				current = current.getChild();
				rightMost = (k * rightMost) + k;
			}
		}
		if (current == null || current.getCode() == null || current.getCode() == "null") {
			throw new IllegalArgumentException("Node not in tree!");
		}
		return (E) current.getCode();
	}

	/**
	 * 
	 * @param i
	 *            the index of the desired node
	 * @param value
	 *            the value to give the desired node
	 * @return true or false based on if the node is valid or able to be set
	 */
	public boolean set(int i, E value) {
		if (i < 0) {
			return false;
		}
		Node current = root;
		int levelCheck = 1;
		int rightMost = 0;
		if (value == null) {
			try {
				while (current.getIndex() != i) {
					if ((current.getChild() == null || i < (current.getIndex() * k + 1)
							|| i > ((current.getIndex() + 1) * k)) && current.getIndex() != 0
							&& current.getIndex() != rightMost) {
						current = current.getSib();
					} else {
						current = current.getChild();
						levelCheck++;
						rightMost = (k * rightMost) + k;
					}
				}
				if (current == null || current.getCode() == null) {
					return false;
				}

				// set up boolean and check for children in loop
				Node childrenCheck = current.getChild();
				while (childrenCheck != null) {
					if (childrenCheck.getCode() != null) {
						return false;
					} else {
						childrenCheck = childrenCheck.getSib();
					}
				}

			} catch (NullPointerException e) {
				return false;
			}
			size--;
		} else {
			while (current.getIndex() != i) {
				if ((i < (current.getIndex() * k + 1) || i > ((current.getIndex() + 1) * k)) && current.getIndex() != 0
						&& current.getIndex() != (Math.pow(k, levelCheck) - k)) {
					if (current.getSib() == null) {
						// check to make sure if next is null that the method is
						// not trying to give the null node a child
						if (i < ((current.getIndex() + 1) * k + 1) || i > ((current.getIndex() + 2) * k)) {
							Node nullSib = new Node(null, current.getIndex() + 1);
							current.setSib(nullSib);
						} else {
							throw new InvalidTreeException();
						}
					}
					current = current.getSib();
				} else {
					if (current != null && current.getCode() != null) {
						if (current.getChild() == null) {
							Node nullChild = new Node(null, current.getIndex() * k + 1);
							current.setChild(nullChild);
						}
						current = current.getChild();
						levelCheck++;
					} else {
						throw new InvalidTreeException();
					}
				}
			}

			if (current.getCode() == null || current.getCode() == (E) "null") {
				size++;
			}
		}

		current.setCode(value);

		// height
		height = 0;
		Stack stack1 = new Stack();
		Stack stack2 = new Stack();
		Stack stack3 = new Stack();
		Stack stack4 = new Stack();
		if (root.getCode() == null || root == null) {
			height = 0;
			return true;
		}
		Node level = new Node(0, 0);
		stack1.push(root);
		stack3.push(level);
		while (!stack1.isEmpty()) {

			Node currentNode = stack1.pop();
			int currentLevel = Integer.valueOf(stack3.pop().toString());
			height = Math.max(height, currentLevel);
			if (currentNode.getChild() != null) {
				stack2.push(currentNode.getChild());
				Node levelCheckNode = currentNode.getChild();
				boolean addLevel = false;
				while(levelCheckNode!= null){
					if(levelCheckNode.getCode() !=null){
						addLevel = true;
						break;
					} else {
						levelCheckNode = levelCheckNode.getSib();
					}
				}
				if(addLevel){
					level = new Node(currentLevel + 1, 0);
				} else {
					level = new Node(currentLevel, 0);
				}
				stack4.push(level);
			}
			if (currentNode.getSib() != null) {
				stack1.push(currentNode.getSib());
				level = new Node(currentLevel, 0);
				stack3.push(level);
			} else {
				if (!stack2.isEmpty()) {
					stack1.push(stack2.pop());
					stack3.push(stack4.pop());
				}
			}
			height = Math.max(height, currentLevel);
		}
		return true;
	}

	/**
	 * @return the tree as an array full of objects
	 */
	public Object[] toArray() {

		int arrSize = (int) (Math.pow(this.k, (this.height + 1)) - 1) / (this.k - 1);
		int rightMost = 0;
		int lastNonNullIndex = 0;
		Object[] tempArray = new Object[arrSize];
		Queue queue = new Queue();
		Node current = root;
		if (current == null || current.getCode() == null || current.getCode() == "null") {
			return tempArray;
		}
		while (current != null && current.getIndex() < arrSize) {

			if (current.getCode() != null && current.getIndex() >= rightMost) {
				rightMost = (k * rightMost) + k;
			}
			if (current.getCode() != null && current.getCode() != "null") {
				lastNonNullIndex = current.getIndex();

			}

			if (current != null) {
				tempArray[current.getIndex()] = current.getCode();
			}

			if (current.getChild() != null) {
				queue.enqueue(current.getChild());
			}
			if (current.getSib() != null) {
				current = current.getSib();
			} else {
				current = queue.dequeue();
			}
		}

		if (lastNonNullIndex < arrSize - 1) {
			Object[] objArray = new Object[rightMost + 1];
			System.arraycopy(tempArray, 0, objArray, 0, lastNonNullIndex + 1);
			tempArray = objArray;
		}
		return tempArray;
	}

	/**
	 * @return the tree as a String
	 */
	public String toString() {
		Object[] tempArray = this.toArray();
		int rightMost = 0;
		int leftMost = 0;
		int leftMostLevel = 0;
		String tempString = "";
		for (int i = 0; i < tempArray.length; i++) {
			if (tempArray[i] != null) {
				if (i == leftMost) {
					tempString = tempString + tempArray[i].toString();
				} else {
					tempString = tempString + " " + tempArray[i].toString();
				}
			} else {
				if (i == 0) {
					return null;
				} else if (i == leftMost) {
					tempString = tempString + null;
				} else {
					tempString = tempString + " " + null;
				}
			}
			if (i == rightMost && i < tempArray.length - 1) {
				tempString = tempString + "\n";
				rightMost = (k * rightMost) + k;
				leftMost = (int) Math.pow(k, leftMostLevel) + leftMost;
				leftMostLevel++;
			}
		}
		return tempString;
	}

	/**
	 * @return an iterator that traverses the tree in level order
	 */

	public Iterator<E> getLevelOrderIterator() {
		return new Iterator<E>() {
			Queue<Node<E>> queue = new Queue<Node<E>>();
			Node current = root;

			/**
			 * @return true or false based on if the level order iterator has
			 *         another value to return
			 */
			public boolean hasNext() {
				return (current != null);
			}

			/**
			 * @return the value of the next node in the tree
			 */
			public E next() {
				if (!hasNext()) {
					throw new NullPointerException("No more items!");
				}
				while (current.getCode() == null) {
					while (current.getSib() != null) {
						current = current.getSib();
					}
					if (current.getCode() == null) {
						current = queue.dequeue();
					}
					if (!hasNext()) {
						throw new NullPointerException("No more items!");
					}
				}
				E result = (E) current.getCode();

				if (current.getChild() != null) {
					queue.enqueue(current.getChild());
				}
				if (current.getSib() != null) {
					current = current.getSib();
				} else {
					current = queue.dequeue();
				}

				return result;
			}
		};
	}

	/**
	 * @return an String representation of the tree traversed in level order
	 */
	public String toStringLevelOrder() {
		Iterator<E> it = this.getLevelOrderIterator();
		String stringReturned = String.valueOf(it.next()) + " ";
		while (it.hasNext()) {
			try {
				stringReturned = stringReturned + it.next() + " ";
			} catch (NullPointerException e) {
				stringReturned = stringReturned.trim();
				return stringReturned;
			}
		}
		stringReturned = stringReturned.trim();
		return stringReturned;
	}

	/**
	 * @return an iterator that traverses the tree in pre-order
	 */
	public Iterator<E> getPreOrderIterator() {

		return new Iterator<E>() {
			Stack<Node<E>> stack = new Stack<>();
			Node current = root;

			/**
			 * @return true or false based on if the level order iterator has
			 *         another value to return
			 */
			public boolean hasNext() {
				return (current != null);
			}

			/**
			 * @return the value of the next node in the tree
			 */
			public E next() {

				if (!hasNext()) {
					throw new NullPointerException("No more items!");
				}

				int check = 0;
				while (current.getCode() == null) {
					current = stack.pop();
					if (!hasNext()) {
						throw new NullPointerException("No more items!");
					}
				}

				E result = (E) current.getCode();
				if (current.getChild() != null && current.getChild().getSib() != null) {
					stack.push(current.getChild().getSib());
					current = current.getChild();
				} else {
					current = stack.pop();
				}

				return result;
			}
		};
	}

	/**
	 * @return an String representation of the tree traversed in pre-order
	 */
	public String toStringPreOrder() {
		String stringReturned = "";
		Iterator<E> it = this.getPreOrderIterator();
		while (it.hasNext()) {
			try {
				stringReturned = stringReturned + it.next() + " ";
			} catch (NullPointerException e) {
				stringReturned = stringReturned.trim();
				return stringReturned;
			}
		}
		stringReturned = stringReturned.trim();
		return stringReturned;
	}

	/**
	 * @return an iterator that traverses the tree in post-order
	 */
	public Iterator<E> getPostOrderIterator() {
		return new Iterator<E>() {
			Stack<Node<E>> stack = new Stack();
			Node current = root;
			Stack returned = this.getStack(current);

			/**
			 * @return true or false based on if the stack is empty
			 */
			public boolean hasNext() {
				return !returned.isEmpty();
			}

			/**
			 * @return the code of the next node
			 */
			public E next() {
				if (!hasNext()) {
					throw new NullPointerException("No more items!");
				}

				E toBeReturned = null;
				while (toBeReturned == null) {
					toBeReturned = (E) returned.pop().getCode();
				}
				return toBeReturned;
			}

			/**
			 * 
			 * @param n
			 *            the root node of the tree
			 * @return a stack containing the nodes of three in post-order
			 */
			public Stack getStack(Node n) {
				Stack<Node<E>> stack1 = new Stack();
				Stack<Node<E>> stack2 = new Stack();
				Node current = n;
				stack1.push(current);
				while (!stack1.isEmpty()) {
					stack2.push(stack1.pop());
					if (current.getChild() != null) {
						stack1.push(current.getChild());
					}
					if (current.getChild() != null && current.getChild().getSib() != null) {
						stack1.push(current.getChild().getSib());
					}
					current = stack1.peek();
				}

				while (stack2.peek().getCode() == null) {
					stack2.pop();
				}
				return stack2;
			}
		};
	}

	/**
	 * @return an String representation of the tree traversed in post-order
	 */
	public String toStringPostOrder() {
		String stringReturned = "";
		Iterator<E> it = this.getPostOrderIterator();
		while (it.hasNext()) {
			stringReturned = stringReturned + it.next() + " ";
		}
		stringReturned = stringReturned.trim();
		return stringReturned;
	}

	/**
	 * 
	 * @param tree
	 *            the tree containing the letters in the message
	 * @param codedMessage
	 *            the "instructions" for getting the letters of the message
	 * @return a String of the decoded messaged
	 */
	public static String decode(KTree<String> tree, String codedMessage) {
		int[] codedArray = new int[codedMessage.length()];
		for (int i = 0; i < codedMessage.length(); i++) {
			codedArray[i] = Character.getNumericValue(codedMessage.charAt(i));
		}
		Node current = tree.root;
		String decoded = "";
		for (int i = 0; i < codedArray.length; i++) {
			int k = 0;
			current = current.getChild();

			if (codedArray[i] != '0') {
				while (k < codedArray[i]) {
					current = current.getSib();
					k++;
				}
			}

			if (current.getCode() != "_") {
				decoded = decoded + (String) current.getCode();
				current = tree.root;
			}
		}
		return decoded;

	}

	/**
	 * 
	 * @param i
	 *            is the index of the node that should be the root node in the
	 *            new subtree
	 * @return array of elements in the subtree
	 */
	public E[] subtree(int i) {

		// get new root node

		Node current = root;
		int rightMost = 0;
		while (current != null && current.getIndex() != i) {
			if ((current.getChild() == null || i < (current.getIndex() * k + 1) || i > ((current.getIndex() + 1) * k))
					&& current.getIndex() != 0 && current.getIndex() != rightMost) {
				current = current.getSib();
			} else {
				current = current.getChild();
				rightMost = (k * rightMost) + k;
			}
		}
		if (current == null || current.getCode() == null || current.getCode() == "null") {
			throw new IllegalArgumentException("Node not in tree!");
		}

		Node newRoot = current;

		// get the array
		int arrSize = (int) (Math.pow(this.k, (this.height + 1)) - 1) / (this.k - 1);
		rightMost = 0;
		int lastNonNullIndex = 0;
		E[] tempArray = (E[]) new Object[arrSize];
		Queue queue = new Queue();

		current = newRoot;
		if (current == null || current.getCode() == null || current.getCode() == "null") {
			return tempArray;
		}
		int newIndex = 0;
		while (current != null && current.getIndex() < arrSize) {
			if (current.getCode() != null && current.getIndex() >= rightMost) {
				rightMost = (k * rightMost) + k;
			}
			if (current.getCode() != null && current.getCode() != "null") {
				lastNonNullIndex = current.getIndex();
			}

			if (current != null) {
				tempArray[newIndex] = (E) current.getCode();
			}

			if (current.getChild() != null) {
				queue.enqueue(current.getChild());
			}
			if (current.getSib() != null && current != newRoot) {
				current = current.getSib();
			} else {
				current = queue.dequeue();
			}
			newIndex++;
		}

		if (lastNonNullIndex < arrSize - 1) {
			E[] objArray = (E[]) new Object[rightMost + 1];
			System.arraycopy(tempArray, 0, objArray, 0, lastNonNullIndex + 1);
			tempArray = objArray;
		}

		return tempArray;
	}

	public E[] mirror() {
		Stack<Node<E>> stackParents = new Stack();
		Queue<Node<E>> queueChildren = new Queue();
		E[] tempArray = (E[]) new Object[this.size()];
		Node current = this.root;
		Node child = current.getChild();
		queueChildren.enqueue(child);
		int indexTempArray = 0;
		while (!queueChildren.isEmpty()) {
			while (current != null) {
					while (child != null) {
						if(child!=root.getChild()){
						queueChildren.enqueue(child);
						}
						child = child.getSib();
					}
				stackParents.push(current);
				current = current.getSib();
			}
			while (!stackParents.isEmpty()) {
				tempArray[indexTempArray] = (E) stackParents.pop().getCode();
				indexTempArray++;
			}
			current = queueChildren.dequeue();
		}

		/**
		for (int i = 0; i < tempArray.length; i++) {
			System.out.println(tempArray[i]);
		}
		*/
		return tempArray;
	}

	/****************************************/
	/* EDIT THIS MAIN METHOD FOR TESTS. PUT */
	/* HELPER TEST METHODS IN THIS SECTION */
	/* AS WELL. TESTS REQUIRED FOR FULL */
	/* CREDIT. */
	/****************************************/
	
/**
 * @param the supplied command line arguments
 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		/**
		// Node method tests

		// Node constructor
		System.out.println("\nNode constructor test\n");
		Node<Integer> test1 = new Node(0, 4);
		Node<Integer> test2 = new Node(1, 5);
		Node<Integer> test3 = new Node(2, 6);

		Node<Integer> child1 = new Node(0, 4);
		Node<Integer> child2 = new Node(1, 5);
		Node<Integer> child3 = new Node(2, 6);

		Node<Integer> sib1 = new Node(0, 4);
		Node<Integer> sib2 = new Node(1, 5);
		Node<Integer> sib3 = new Node(2, 6);

		Node<Integer> next1 = new Node(0, 4);
		Node<Integer> next2 = new Node(1, 5);
		Node<Integer> next3 = new Node(2, 6);
		if (test1 != null && test2 != null && test3 != null) {
			System.out.println("You passed tests 1 through 3!");
		}

		// getIndex()
		System.out.println("\ngetIndex() Tests:\n");
		if (test1.getIndex() == 4) {
			System.out.println("You passed test 1!");
		}

		if (test2.getIndex() == 5) {
			System.out.println("You passed test 2!");
		}
		if (test3.getIndex() == 6) {
			System.out.println("You passed test 3!");
		}

		// setIndex()
		System.out.println("\nsetIndex() Tests:\n");
		test1.setIndex(0);
		if (test1.getIndex() == 0) {
			System.out.println("You passed test 1!");
		}

		test2.setIndex(1);
		if (test2.getIndex() == 1) {
			System.out.println("You passed test 2!");
		}

		test3.setIndex(2);
		if (test3.getIndex() == 2) {
			System.out.println("You passed test 3!");
		}

		// getCode()
		System.out.println("\ngetCode() Tests:\n");

		if (test1.getCode() == 0) {
			System.out.println("You passed test 1!");
		}

		if (test2.getCode() == 1) {
			System.out.println("You passed test 2!");
		}
		if (test3.getCode() == 2) {
			System.out.println("You passed test 3!");
		}

		// setCode()
		System.out.println("\nsetCode() test\n");
		test1.setCode(3);
		if (test1.getCode() == 3) {
			System.out.println("You passed test 1!");
		}

		test2.setCode(4);
		if (test2.getCode() == 4) {
			System.out.println("You passed test 2!");
		}

		test3.setCode(5);
		if (test3.getCode() == 5) {
			System.out.println("You passed test 3!");
		}

		// getChild()
		System.out.println("\ngetChild() Tests:\n");
		if (test1.getChild() == null & test2.getChild() == null & test3.getChild() == null) {
			System.out.println("You passed tests 1 through 3!");
		}

		// setChild()
		System.out.println("\nsetChild() test\n");
		test1.setChild(child1);
		if (test1.getChild() == child1) {
			System.out.println("You passed test 1!");
		}

		test2.setChild(child2);
		if (test2.getChild() == child2) {
			System.out.println("You passed test 2!");
		}

		test3.setChild(child3);
		if (test3.getChild() == child3) {
			System.out.println("You passed test 3!");
		}

		// getSib()
		System.out.println("\ngetSib() Tests:\n");
		if (test1.getSib() == null & test2.getSib() == null & test3.getSib() == null) {
			System.out.println("You passed tests 1 through 3!");
		}

		// setSib()

		System.out.println("\nsetSib() Tests:\n");
		test1.setSib(sib1);
		if (test1.getSib() == sib1) {
			System.out.println("You passed test 1!");
		}

		test2.setSib(sib2);
		if (test2.getSib() == sib2) {
			System.out.println("You passed test 2!");
		}

		test3.setSib(sib3);
		if (test3.getSib() == sib3) {
			System.out.println("You passed test 3!");
		}

		// getNext()
		System.out.println("\ngetNext() Tests:\n");
		if (test1.getNext() == null & test2.getNext() == null & test3.getNext() == null) {
			System.out.println("You passed tests 1 through 3!");
		}

		// setNext()
		System.out.println("\nsetNext() Tests:\n");
		test1.setNext(next1);
		if (test1.getNext() == next1) {
			System.out.println("You passed test 1!");
		}

		test2.setNext(next2);
		if (test2.getNext() == next2) {
			System.out.println("You passed test 2!");
		}

		test3.setNext(next3);
		if (test3.getNext() == next3) {
			System.out.println("You passed test 3!");
		}

		// Stack method tests
		System.out.println("\nStack constructor Tests:\n");

		// constructor
		Stack<Integer> stack1 = new Stack();
		if (stack1 != null) {
			System.out.println("You passed test 1!");
		}

		// push
		System.out.println("\npush() Tests:\n");

		stack1.push(test1);
		stack1.push(test2);
		stack1.push(test3);

		if (stack1.top == test3 && stack1.top.getNext() == test2 && stack1.top.getNext().getNext() == test1) {
			System.out.println("You passed tests 1 through 3!");
		}

		// peek
		System.out.println("\npeek() Tests:\n");
		if (stack1.peek() == test3) {
			System.out.println("You passed test 1!");
		}

		// pop
		System.out.println("\npop() Tests:\n");

		if (stack1.pop() == test3 && stack1.pop() == test2 && stack1.pop() == test1) {
			System.out.println("You passed tests 1 through 3!");
		}

		// isEmpty
		System.out.println("\nStack isEmpty() Tests:\n");

		if (stack1.isEmpty()) {
			System.out.println("You passed test 1!");
		}

		// Queue constructor
		System.out.println("\nQueue constructor Tests:\n");
		Queue<Integer> queue1 = new Queue();
		if (queue1 != null) {
			System.out.println("You passed test 1!");
		}

		// enqueue
		System.out.println("\nenqueue() Tests:\n");

		queue1.enqueue(test1);
		queue1.enqueue(test2);
		queue1.enqueue(test3);

		if (queue1.top == test1 && queue1.top.getNext() == test2 && queue1.bottom == test3) {
			System.out.println("You passed tests 1 through 3!");
		}

		// peek
		System.out.println("\npeek() Tests:\n");
		if (queue1.peek() == test1) {
			System.out.println("You passed test 1!");
		}

		// dequeue
		System.out.println("\ndequeue() Tests:\n");

		if (queue1.dequeue() == test1 && queue1.dequeue() == test2 && queue1.dequeue() == test3) {
			System.out.println("You passed tests 1 through 3!");
		}

		// isEmpty
		System.out.println("\nisEmpty() Tests:\n");
		if (queue1.isEmpty()) {
			System.out.println("You passed test 1!");
		}

		queue1.enqueue(test1);
		queue1.enqueue(test2);
		queue1.enqueue(test3);

		if (!queue1.isEmpty()) {
			System.out.println("You passed test 2!");
		}

		// public KTree(E[] arrayTree, int k)
		System.out.println("\nK-Tree Constructor Tests: \n");
		String[] strings = { "_", "_", "A", "B", "N", null, null };

		// 1. check that invalid exception is thrown for k<2
		try {
			KTree<String> tree = new KTree<>(strings, 1);
		} catch (InvalidKException e) {
			System.out.println("You passed test 1!");
		}

		try {
			KTree<String> tree = new KTree<>(strings, 0);
		} catch (InvalidKException e) {
			System.out.println("You passed test 2!");
		}

		// 2. check that tree by checking each value is at the appropriate
		// location
		KTree<String> tree = new KTree<>(strings, 2);
		if (tree.root.getCode().equals("_") && tree.root.getChild().getCode().equals("_")
				&& tree.root.getChild().getSib().getCode().equals("A")
				&& tree.root.getChild().getChild().getCode().equals("B")
				&& tree.root.getChild().getChild().getSib().getCode().equals("N")) {
			System.out.println("You passed test 3!");

		}

		Integer[] input = { 0, null, 2, null, null, 5, 6 };
		KTree<Integer> tree2 = new KTree<>(input, 2);
		if (tree2.root.getCode().equals(0) && tree2.root.getChild().getCode() == null
				&& tree2.root.getChild().getSib().getCode() == 2 && tree2.root.getChild().getChild().getCode() == null
				&& tree2.root.getChild().getChild().getSib().getCode() == null
				&& tree2.root.getChild().getSib().getChild().getCode() == 5
				&& tree2.root.getChild().getSib().getChild().getSib().getCode() == 6) {
			System.out.println("You passed test 4!");

		}

		Integer[] input2 = { 0, null, 2, 3, null, null, null, null, 5, 6, null, null, null };
		KTree<Integer> tree3 = new KTree<>(input2, 3);

		if (tree3.root.getCode() == 0 && tree3.root.getChild().getCode() == null
				&& tree3.root.getChild().getSib().getCode() == 2
				&& tree3.root.getChild().getSib().getSib().getCode() == 3
				&& tree3.root.getChild().getChild().getCode() == null
				&& tree3.root.getChild().getChild().getSib().getCode() == null
				&& tree3.root.getChild().getChild().getSib().getSib().getCode() == null
				&& tree3.root.getChild().getSib().getChild().getCode() == null
				&& tree3.root.getChild().getSib().getChild().getSib().getCode() == 5
				&& tree3.root.getChild().getSib().getChild().getSib().getSib().getCode() == 6) {
			System.out.println("You passed test 5!");

		}

		Integer[] input3 = { 0, null, null };
		KTree<Integer> tree4 = new KTree<>(input3, 2);

		if (tree4.root.getCode() == 0 && tree4.root.getChild().getCode() == null
				&& tree4.root.getChild().getSib().getCode() == null) {
			System.out.println("You passed test 6!");

		}

		Integer[] input4 = { 0, null, null, null, null, null, null };
		KTree<Integer> tree5 = new KTree<>(input4, 2);

		if (tree5.root.getCode() == 0 && tree5.root.getChild().getCode() == null
				&& tree5.root.getChild().getSib().getCode() == null) {
			System.out.println("You passed test 7!");

		}

		// getK()
		System.out.println("\ngetK Tests: \n");

		// 1. check that k tree is equal to the intended value
		if (tree.getK() == 2) {
			System.out.println("You passed test 1!");
		}

		if (tree2.getK() == 2) {
			System.out.println("You passed test 2!");
		}

		if (tree3.getK() == 3) {
			System.out.println("You passed test 3!");
		}

		// size()
		System.out.println("\nSize Tests: \n");

		// 1. check if number of elements in tree is the intended value
		if (tree.size() == 5 && tree2.size() == 4 && tree3.size() == 5) {
			System.out.println("You passed test 1!");
		}
		// 2. check an empty tree
		Integer[] input5 = { null, null, null };
		KTree<Integer> tree6 = new KTree<>(input5, 2);

		if (tree6.size() == 0) {
			System.out.println("You passed test 2!");
		}

		// 3. check tree with one node
		tree6.set(0, 1);
		if (tree6.size() == 1) {
			System.out.println("You passed test 3!");
		}

		// 4. remove node and check number of elements
		tree6.set(0, null);
		if (tree6.size() == 0) {
			System.out.println("You passed test 4!");
		}

		if (tree5.size() == 1) {
			System.out.println("You passed test 5!");
		}

		// 5. add nodes and check number of elements
		tree6.set(0, 1);
		tree6.set(1, 2);
		tree6.set(2, 3);
		tree6.set(3, 1);
		if (tree6.size() == 4) {
			System.out.println("You passed test 6!");
		}

		// height()
		System.out.println("\nHeight Tests: \n");

		// 1. check if height is the intended value
		if (tree.height() == 2) {
			System.out.println("You passed test 1!");
		}
		tree.set(3, null);
		if (tree.height() == 2) {
			System.out.println("You passed test 2!");
		}

		tree.set(4, null);
		if (tree.height() == 1) {
			System.out.println("You passed test 3!");
		}

		tree.set(4, "K");
		if (tree.height() == 2) {
			System.out.println("You passed test 4!");
		}

		// 2. check height of empty tree

		if (tree4.height() == 0) {
			tree4.set(0, null);

			if (tree4.height == 0) {
				System.out.println("You passed test 5!");
			}
		}

		// 3. check height of tree with one node
		if (tree5.height() == 0) {
			System.out.println("You passed test 6!");
		}

		// 4. check height of tree with two nodes
		tree5.set(1, 5);
		if (tree5.height() == 1) {
			System.out.println("You passed test 7!");
		}

		tree5.set(1, null);
		tree5.set(2, 3);
		if (tree5.height() == 1) {
			System.out.println("You passed test 8!");
		}

		// get(int i)
		System.out.println("\nGet Tests: \n");

		// 1. check if it returns correct value for given location
		tree = new KTree<>(strings, 2);

		if (tree.get(0).equals("_") && tree.get(1).equals("_") && tree.get(2).equals("A") && tree.get(3).equals("B")
				&& tree.get(4).equals("N")) {
			System.out.println("You passed test 1!");
		}

		// 2. if location does not exist in tree return does not exist
		try {
			tree.get(5);
		} catch (IllegalArgumentException e) {
			System.out.println("You passed test 2!");
		}

		try {
			tree.get(-1);
		} catch (IllegalArgumentException e) {
			System.out.println("You passed test 3!");
		}

		tree.set(3, null);
		if (tree.get(0).equals("_") && tree.get(1).equals("_") && tree.get(2).equals("A") && tree.get(4).equals("N")) {
			System.out.println("You passed test 4!");
		}

		try {
			tree.get(3);
		} catch (IllegalArgumentException e) {
			System.out.println("You passed test 5!");
		}

		tree2.set(5, null);
		try {
			tree2.get(5);
		} catch (IllegalArgumentException e) {
			System.out.println("You passed test 6!");
		}

		if (tree2.get(0) == 0 && tree2.get(2).equals(2) && tree2.get(6).equals(6)) {
			System.out.println("You passed test 7!");
		}

		// set(int i, E value)
		System.out.println("\nSet Tests: \n");

		KTree<Integer> tree7 = new KTree<>(input, 2);

		// 1. if value is null check if false is returned if node is not in tree
		if (!tree7.set(1, null)) {
			System.out.println("You passed test 1!");

		}
		// 2. check that InvalidTreeException is thrown if adding node would not
		// make tree valid
		try {
			tree7.set(3, 3);
		} catch (InvalidTreeException e) {
			System.out.println("You passed test 2!");
		}

		// 3. if value is not null and there is no node at location i check that
		// value was added to tree if valid

		if (tree7.set(14, 14)) {
			System.out.println("You passed test 3!");
		} // but this should work and return true

		// 4. if value is null check if false is returned if the node has
		// children
		if (!tree7.set(2, null)) {
			System.out.println("You passed test 4!");
		}

		// 5. check if invalid index returns false

		if (!tree7.set(-1, null)) {
			System.out.println("You passed test 5!");
		}

		// 6. if value is null and node is in tree and does not have children
		// check if true is returned and check that value is null after
		// node is removed

		if (tree7.set(14, null)) {
			try {
				tree7.get(14);
			} catch (IllegalArgumentException e) {
				System.out.println("You passed test 6!");
			}
		}

		if (!tree7.set(14, null)) {
			System.out.println("You passed test 7!");
		}

		// toArray()
		System.out.println("\ntoArray Tests: \n");

		// 1. check if each node is stored in appropriate index
		String[] input7 = { "_", "_", "A", "B", "N", null, null };
		tree = new KTree<>(input7, 2);
		Object[] testArray = tree.toArray();
		boolean passed = true;

		if (tree.height() == 2) {
			for (int i = 0; i < input7.length; i++) {
				if (testArray[i] != null && input7[i] != null) {
					if (!testArray[i].toString().equals(input7[i].toString())) {
						passed = false;
					}
				} else if (testArray[i] == null) {
					if (input7[i] != null && !input7[i].toString().equals("null")) {
						passed = false;
					}
				} else if (input7[i] == null) {
					if (testArray[i] != null && !testArray[i].toString().equals("null")) {
						passed = false;
					}

				}
			}
		}
		if (passed) {
			System.out.println("You passed test 1!");
		}

		Integer[] inputRandom = { 0, null, 2, null, null, 5, 6 };
		KTree treeRandom = new KTree<>(inputRandom, 2);
		testArray = treeRandom.toArray();
		passed = true;

		if (treeRandom.height() == 2) {
			for (int i = 0; i < inputRandom.length; i++) {
				if (testArray[i] != null && inputRandom[i] != null) {
					if (!testArray[i].toString().equals(inputRandom[i].toString())) {
						passed = false;
					}
				} else if (testArray[i] == null) {
					if (inputRandom[i] != null && !inputRandom[i].toString().equals("null")) {
						passed = false;
					}
				} else if (inputRandom[i] == null) {
					if (testArray[i] != null && !testArray[i].toString().equals("null")) {
						passed = false;
					}

				}
			}
		}
		if (passed) {
			System.out.println("You passed test 2!");
		}

		// 2. check that empty array is returned if no elements
		treeRandom = new KTree<>(input5, 2);
		testArray = treeRandom.toArray();

		if (treeRandom.height() == 0 && treeRandom.size() == 0) {
			if (testArray.length == 1 && testArray[0] == null) {
				System.out.println("You passed test 3!");
			}
		}

		// toString()
		System.out.println("\ntoString Tests: \n");

		// check that each level is printed appropriately

		Integer[] input8 = { 0, null, 2, null, null, 5, 6 };
		tree2 = new KTree<>(input8, 2);
		if (tree2.toString().equals("0\nnull 2\nnull null 5 6")) {
			System.out.println("You passed test 1!");
		}
		// check that an empty tree prints null
		Integer[] input9 = { null, null, null };
		tree2 = new KTree<>(input9, 2);
		if (tree2.toString() == null) {
			System.out.println("You passed test 2!");
		}

		// change tree and do toString again
		tree2 = new KTree<>(input8, 2);
		tree2.set(6, null);
		if (tree2.toString().equals("0\nnull 2\nnull null 5 null")) {
			System.out.println("You passed test 3!");
		}

		tree2.set(5, null);
		if (tree2.toString().equals("0\nnull 2")) {
			System.out.println("You passed test 4!");
		}

		if (tree6.toString().equals("1\n2 3\n1 null null null")) {
			System.out.println("You passed test 5!");
		}

		tree6.set(3, null);
		if (tree6.toString().equals("1\n2 3")) {
			System.out.println("You passed test 6!");
		}

		// Iterator<E> getLevelOrderIterator()
		System.out.println("\ngetLevelOrderIterator Tests \n");

		// check that iterator returns elements in appropriate order
		Integer[] input10 = { 0, 1, 2, null, 4, 5, null };
		KTree<Integer> tree8 = new KTree<>(input10, 2);
		Iterator<Integer> it = tree8.getLevelOrderIterator();

		if (it.next() == 0 && it.next() == 1 && it.next() == 2 && it.next() == 4 && it.next() == 5) {

			System.out.println("You passed test 1!");

		}

		// check that iterator does not return any more elements
		try {
			System.out.println(it.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 2!");
		}

		tree = new KTree<>(strings, 2);
		Iterator<String> itString = tree.getLevelOrderIterator();

		if (itString.next() == tree.root.getCode() && itString.next() == tree.root.getChild().getCode()
				&& itString.next() == tree.root.getChild().getSib().getCode()
				&& itString.next() == tree.root.getChild().getChild().getCode()
				&& itString.next() == tree.root.getChild().getChild().getSib().getCode()) {

			System.out.println("You passed test 3!");

		}
		try {
			System.out.println(itString.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 4!");
		}

		tree8.set(4, null);

		it = tree8.getLevelOrderIterator();
		if (it.next() == 0 && it.next() == 1 && it.next() == 2 && it.next() == 5) {
			System.out.println("You passed test 5!");
		}

		try {
			System.out.println(it.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 6!");
		}

		it = tree8.getLevelOrderIterator();
		tree8.set(5, null);
		if (it.next() == 0 && it.next() == 1 && it.next() == 2) {
			System.out.println("You passed test 7!");
		}

		try {
			System.out.println(it.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 8!");
		}

		tree8.set(1, null);
		it = tree8.getLevelOrderIterator();
		if (it.next() == 0 && it.next() == 2) {
			System.out.println("You passed test 9!");
		}

		try {
			System.out.println(it.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 10!");
		}

		tree8.set(2, null);
		it = tree8.getLevelOrderIterator();
		if (it.next() == 0) {
			System.out.println("You passed test 11!");
		}

		try {
			System.out.println(it.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 12!");
		}

		// toStringLevelOrder()
		System.out.println("\ntoStringLevelOrder Tests \n");
		// Integer[] input10 = { 0, 1, 2, null, 4, 5, null };
		tree8 = new KTree<>(input10, 2);

		if (tree8.toStringLevelOrder().equals("0 1 2 4 5")) {
			System.out.println("You passed test 1!");
		}

		tree8.set(4, null);
		if (tree8.toStringLevelOrder().equals("0 1 2 5")) {
			System.out.println("You passed test 2!");
		}

		tree8.set(5, null);
		if (tree8.toStringLevelOrder().equals("0 1 2")) {
			System.out.println("You passed test 3!");
		}

		tree8.set(1, null);
		if (tree8.toStringLevelOrder().equals("0 2")) {
			System.out.println("You passed test 4!");
		}

		tree8.set(2, null);
		if (tree8.toStringLevelOrder().equals("0")) {
			System.out.println("You passed test 5!");
		}
		// check that nodes are returned in correct order
		// check that empty tree returns null

		// Iterator<E> getPreOrderIterator()
		// check that nodes are returned in correct order
		// check that empty tree returns null
		System.out.println("\ngetPreOrderIterator Tests \n");

		tree8 = new KTree<>(input10, 2);
		Iterator<Integer> itPre = tree8.getPreOrderIterator();

		if (itPre.next() == 0 && itPre.next() == 1 && itPre.next() == 4 && itPre.next() == 2 && itPre.next() == 5) {

			System.out.println("You passed test 1!");

		}

		try {
			System.out.println(itPre.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 2!");
		}

		tree = new KTree<>(strings, 2);
		Iterator<String> itPreString = tree.getPreOrderIterator();

		if (itPreString.next() == tree.root.getCode() && itPreString.next() == tree.root.getChild().getCode()
				&& itPreString.next() == tree.root.getChild().getChild().getCode()
				&& itPreString.next() == tree.root.getChild().getChild().getSib().getCode()
				&& itPreString.next() == tree.root.getChild().getSib().getCode()) {

			System.out.println("You passed test 3!");
		}

		try {
			System.out.println(itPreString.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 4!");
		}

		tree8.set(4, null);

		itPre = tree8.getPreOrderIterator();
		if (itPre.next() == 0 && itPre.next() == 1 && itPre.next() == 2 && itPre.next() == 5) {
			System.out.println("You passed test 5!");
		}

		try {
			System.out.println(itPre.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 6!");
		}

		itPre = tree8.getPreOrderIterator();
		tree8.set(5, null);
		if (itPre.next() == 0 && itPre.next() == 1 && itPre.next() == 2) {
			System.out.println("You passed test 7!");
		}

		try {
			System.out.println(itPre.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 8!");
		}

		tree8.set(1, null);
		itPre = tree8.getPreOrderIterator();
		if (itPre.next() == 0 && itPre.next() == 2) {
			System.out.println("You passed test 9!");
		}

		try {
			System.out.println(itPre.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 10!");
		}

		tree8.set(2, null);
		itPre = tree8.getPreOrderIterator();
		if (itPre.next() == 0) {
			System.out.println("You passed test 11!");
		}

		try {
			System.out.println(itPre.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 12!");
		}

		itPreString = tree.getPreOrderIterator();
		if (itPreString.next().equals("_") && itPreString.next().equals("_") && itPreString.next().equals("B")
				&& itPreString.next().equals("N") && itPreString.next().equals("A")) {
			System.out.println("You passed test 13!");
		}

		try {
			System.out.println(itPreString.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 14!");
		}

		System.out.println("\ntoStringPreOrder Tests \n");
		if (tree.toStringPreOrder().equals("_ _ B N A")) {
			System.out.println("You passed test 1!");
		}

		tree8 = new KTree<>(input10, 2);
		if (tree8.toStringPreOrder().equals("0 1 4 2 5")) {
			System.out.println("You passed test 2!");
		}

		tree8.set(4, null);
		if (tree8.toStringPreOrder().equals("0 1 2 5")) {
			System.out.println("You passed test 3!");
		}

		tree8.set(5, null);
		if (tree8.toStringPreOrder().equals("0 1 2")) {
			System.out.println("You passed test 4!");
		}

		tree8.set(1, null);
		if (tree8.toStringPreOrder().equals("0 2")) {
			System.out.println("You passed test 5!");
		}

		tree8.set(2, null);
		if (tree8.toStringPreOrder().equals("0")) {
			System.out.println("You passed test 6!");
		}

		// Iterator<E> getPostOrderIterator
		System.out.println("\ngetPostOrderIterator Tests \n");

		// check that nodes are returned in correct order
		// check that empty tree returns null

		tree8 = new KTree<>(input10, 2);
		Iterator<Integer> itPost = tree8.getPostOrderIterator();
		if (itPost.next() == 4 && itPost.next() == 1 && itPost.next() == 5 && itPost.next() == 2
				&& itPost.next() == 0) {
			System.out.println("You passed test 1!");
		}

		try {
			System.out.println(itPost.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 2!");
		}

		tree8.set(4, null);
		itPost = tree8.getPostOrderIterator();
		if (itPost.next() == 1 && itPost.next() == 5 && itPost.next() == 2 && itPost.next() == 0) {
			System.out.println("You passed test 3!");
		}

		try {
			System.out.println(itPost.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 4!");
		}

		tree8.set(1, null);
		itPost = tree8.getPostOrderIterator();
		if (itPost.next() == 5 && itPost.next() == 2 && itPost.next() == 0) {
			System.out.println("You passed test 5!");
		}

		try {
			System.out.println(itPost.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 6!");
		}

		tree8.set(5, null);
		itPost = tree8.getPostOrderIterator();
		if (itPost.next() == 2 && itPost.next() == 0) {
			System.out.println("You passed test 7!");
		}

		try {
			System.out.println(itPost.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 8!");
		}

		// "B N _ A _"
		Iterator<String> itPostString = tree.getPostOrderIterator();
		if (itPostString.next().equals("B") && itPostString.next().equals("N") && itPostString.next().equals("_")
				&& itPostString.next().equals("A") && itPostString.next().equals("_")) {
			System.out.println("You passed test 9!");
		}

		try {
			System.out.println(itPostString.next());
		} catch (NullPointerException e) {
			System.out.println("You passed test 10!");
		}

		// toStringPostOrder()

		System.out.println("\ntoStringPostOrder Tests \n");
		// check that string matches post order node order
		tree8 = new KTree<>(input10, 2);
		if (tree8.toStringPostOrder().equals("4 1 5 2 0")) {
			System.out.println("You passed test 1!");
		}
		// check that empty tree returns null

		tree8.set(4, null);
		if (tree8.toStringPostOrder().equals("1 5 2 0")) {
			System.out.println("You passed test 2!");
		}

		tree8.set(1, null);
		if (tree8.toStringPostOrder().equals("5 2 0")) {
			System.out.println("You passed test 3!");
		}

		tree8.set(5, null);
		if (tree8.toStringPostOrder().equals("2 0")) {
			System.out.println("You passed test 4!");
		}

		// decode(KTree<String> tree, String codedMessage)
		System.out.println("\ndecode Tests \n");

		// check that returned string matches decoded message

		if (decode(new KTree<String>(new String[] { "_", "_", "A", "B", "N", null, null }, 2), "001011011")
				.equals("BANANA")) {
			System.out.println("You passed test 1!");
		}

		if (decode(
				new KTree<String>(
						new String[] { "_", "_", "_", "B", "S", null, null, "N", null, "A", null, null, null }, 3),
				"2121012101200").equals("BANANAS")) {
			System.out.println("You passed test 2!");
		}

		// public E[] subtree(int i)
		System.out.println("\nsubtree Tests \n");

		Object[] checkArray = tree.subtree(1);
		String value1 = (String) checkArray[0];
		String value2 = (String) checkArray[1];
		String value3 = (String) checkArray[2];

		if (value1.equals("_") && value2.equals("B") && value3.equals("N")) {
			System.out.println("You passed test 1!");
		}

		tree7 = new KTree<>(input, 2);
		checkArray = tree7.subtree(0);
		passed = false;
		for (int i = 0; i < checkArray.length; i++) {
			if (checkArray[i] == input[i]) {
				passed = true;
			}
		}
		if (passed) {
			System.out.println("You passed test 2!");
		}

		// public E[] mirror()
		System.out.println("\nmirror Tests \n");

		Integer[] finalInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
		Integer[] finalMirror = { 0, 2, 1, 6, 5, 4, 3, 14, 13, 12, 11, 10, 9, 8, 7 };

		KTree<Integer> treeFinal = new KTree<>(finalInput, 2);

		checkArray = treeFinal.mirror();
		passed = false;
		for (int i = 0; i < checkArray.length; i++) {
			//System.out.println(checkArray[i]);
			if (checkArray[i] == finalMirror[i]) {
				passed = true;
			}
		}
		if (passed) {
			System.out.println("You passed test 1!");
		}
		
		tree = new KTree<>(strings, 2);
		String[] finalCheckStrings = {"_", "A", "_", null, null, "N", "B"};

		
		checkArray = tree.mirror();
		
		for (int i = 0; i < checkArray.length; i++) {
			//System.out.println(checkArray[i]);
			if (checkArray[i] == finalCheckStrings[i]) {
				passed = true;
			}
		}
		
		if(passed){
			System.out.println("You passed test 2!");
		}
		*/

		methodSigCheck();
	}

	/****************************************/
	/* DO NOT EDIT ANYTHING BELOW THIS LINE */
	/****************************************/

	public static void methodSigCheck() {
		// This ensures that you've written your method signatures correctly
		// and understand how to call the various methods from the assignment
		// description.

		String[] strings = { "_", "_", "A", "B", "N", null, null };

		KTree<String> tree = new KTree<>(strings, 2);
		int x = tree.getK(); // should return 2
		int y = tree.size(); // should return 5
		int z = tree.height(); // should return 2

		String v = tree.get(0); // should be "_"
		boolean b = tree.set(0, "x"); // should set the root to "x"
		Object[] o = tree.toArray(); // should return [ "x", "_", "A", "B","N",
										// null, null ]

		String s = tree.toString(); // should be "x\n_ A\nB N null null"
		String s2 = "" + tree; // should also be "x\n_ A\nB N null null"

		Iterator<String> it1 = tree.getLevelOrderIterator(); // gets an iterator
		Iterator<String> it2 = tree.getPreOrderIterator(); // gets an iterator
		Iterator<String> it3 = tree.getPostOrderIterator(); // gets an iterator

		String s3 = tree.toStringLevelOrder(); // should be "_ _ A B N"
		String s4 = tree.toStringPreOrder(); // should be "_ _ B N A"
		String s5 = tree.toStringPostOrder(); // should be "B N _ A _"

		String s6 = decode(tree, "001011011"); // should be "BANANA"

		Object[] o2 = tree.mirror(); // should return [ "x", "A", "_", null,
										// null, "N", "B" ]
		Object[] o3 = tree.subtree(1); // should return [ "_", "B", "N" ]
	}
}
