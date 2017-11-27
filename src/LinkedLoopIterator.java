///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoopIterator.java
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
 * This class is an implementation of Iterator for the LinkedLoop class
 * @author Lucas Bannister
 *
 * @param <E> the object type being iterated 
 */
public class LinkedLoopIterator<E> implements Iterator<E> {
	// The current DblListnode
	private DblListnode<E> currentNode;
	// The total number of nodes
	private int numNodes;
	// The current position within the loop
	private int currentPos;

	/**
	 * Package-access constructor
	 * 
	 * @param currentNode
	 *            current node
	 * @param numNodes
	 *            the total number of nodes
	 */
	LinkedLoopIterator(DblListnode<E> currentNode, int numNodes) {
		// initialize private variables
		this.currentNode = currentNode;
		this.numNodes = numNodes;
		this.currentPos = 0;
	}

	/**
	 * Returns true if the iteration has more elements. (In other words, returns
	 * true if next() would return an element rather than throwing an exception.)
	 * 
	 * @return true if the iteration has more elements
	 */
	@Override
	public boolean hasNext() {
		// Return true if there are more nodes remaining after currentNode
		return (this.currentPos < this.numNodes);
	}

	/**
	 * Returns the next element in the iteration
	 * 
	 * @return the next element in the iteration
	 */
	@Override
	public E next() {
		// store the data of current node
		E temp = currentNode.getData();
		// set the current node to the next node and update currentPos
		currentNode = currentNode.getNext();
		currentPos++;
		// return the data of (what was the) current node
		return temp;
	}

	/**
	 * Removes a node (unimplemented)
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
