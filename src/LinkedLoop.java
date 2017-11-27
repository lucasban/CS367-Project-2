///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoop.java
//Semester:         CS367 Fall 2017
//
//Author:           Lucas Bannister
//Email:            lbannister@wisc.edu
//CS Login:         lbannister
//Lecturer's Name:  Prof. Fischer
//Lab Section:      N/A
//
////////////////////STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//must fully acknowledge and credit those sources of help.
//Instructors and TAs do not have to be credited here,
//but tutors, roommates, relatives, strangers, etc do.
//
//Persons: Sam Fortuna (partner at beginning, but she dropped the class, 
//							and I rewrote most of what she had)
//
//Online sources: StackOverflow.com - general Java information
////////////////////////////80 columns wide //////////////////////////////////

import java.util.Iterator;

/**
 * This class is a doubly linked loop implementation of the LoopADT
 * 
 * @author Lucas Bannister
 *
 * @param <E> - the object type to be stored in the loop
 */
public class LinkedLoop<E> implements LoopADT<E> {
	// The current DblListnode
	private DblListnode<E> currentNode;
	// The current number of nodes
	private int numNodes;

	/**
	 * Default constructor
	 * Constructs an empty LinkedLoop
	 */
	public LinkedLoop() {
		currentNode = null; // initializes the current node to null
		numNodes = 0; // 0 nodes by default
	}

	/**
	 * Adds the given item immediately <em>before</em> the current item. After the
	 * new item has been added, the new item becomes the current item.
	 * 
	 * @param item
	 *            the item to add
	 */
	@Override
	public void add(E item) {
		// Create the new node
		DblListnode<E> tempNode = new DblListnode<E>(item);

		// Special case, if no nodes currently in loop
		if (currentNode == null) {
			// set the current node to the new node
			currentNode = tempNode;
			// link the current node to itself in both directions
			currentNode.setNext(currentNode);
			currentNode.setPrev(currentNode);
		} else {
			// There is already a node in the list
			// establish links in the new node before breaking current nodes' links
			tempNode.setNext(currentNode);
			tempNode.setPrev(currentNode.getPrev());
			// point existing nodes to the new node
			currentNode.getPrev().setNext(tempNode);
			currentNode.setPrev(tempNode);
			// make the new node the current node
			currentNode = tempNode;
		}
		// increase node count by 1
		numNodes++;
	}

	/**
	 * Returns the current item. If the Loop is empty, an
	 * <tt>EmptyLoopException</tt> is thrown.
	 * 
	 * @return the current item
	 * @throws EmptyLoopException
	 *             if the Loop is empty
	 */
	@Override
	public E getCurrent() throws EmptyLoopException {
		// Check for empty loop and throw EmptyLoopException if empty
		if (currentNode == null) {
			throw new EmptyLoopException("No Images Found");
		} else
			return currentNode.getData();
	}

	/**
	 * Removes and returns the current item. The item immediately <em>after</em> the
	 * removed item then becomes the current item. If the Loop is empty initially,
	 * an <tt>EmptyLoopException</tt> is thrown.
	 * 
	 * @return the removed item
	 * @throws EmptyLoopException
	 *             if the Loop is empty
	 */
	@Override
	public E removeCurrent() throws EmptyLoopException {
		// Check for empty loop and throw EmptyLoopException if empty
		if (currentNode == null) {
			throw new EmptyLoopException("No Images Found");
		}

		// keep current node to return
		DblListnode<E> tempNode = currentNode;

		// Handle case where currentNode is the only node
		if (this.size() == 1) {
			currentNode = null;
		}

		// Re-link pointers for the surrounding nodes
		else {
			currentNode = tempNode.getNext();
			currentNode.setPrev(tempNode.getPrev());
			tempNode.getPrev().setNext(currentNode);
		}

		// Decrease numNodes by 1
		numNodes--;

		// Return the data in the removed node
		return tempNode.getData();
	}

	/**
	 * Advances current forward one item resulting in the item that is immediately
	 * <em>after</em> the current item becoming the current item.
	 */
	@Override
	public void next() {
		currentNode = currentNode.getNext();
	}

	/**
	 * Moves current backwards one item resulting in the item that is immediately
	 * <em>before</em> the current item becoming the current item.
	 */
	@Override
	public void previous() {
		currentNode = currentNode.getPrev();
	}

	/**
	 * Determines if this Loop is empty, i.e., contains no items.
	 * 
	 * @return true if the Loop is empty; false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return (numNodes == 0);
	}

	/**
	 * Returns the number of items in this Loop.
	 * 
	 * @return the number of items in this Loop
	 */
	@Override
	public int size() {
		return numNodes;
	}

	/**
	 * Returns an iterator for this Loop.
	 * 
	 * @return an iterator for this Loop
	 */
	@Override
	public Iterator<E> iterator() {
		return new LinkedLoopIterator<E>(currentNode, numNodes);
	}

}
