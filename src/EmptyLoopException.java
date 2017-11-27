///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Title:            Program 2 (ImageLoopEditor)
// Main Class File:  ImageLoopEditor.java
// File:             EmptyLoopException.java
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
/**
 * Exception class to be thrown when the loop is empty.
 * 
 * <p>
 * Bugs: None known
 * 
 * @author Lucas Bannister
 *
 */
public class EmptyLoopException extends Exception {

	// As the class is serializable, the compiler wants this
	private static final long serialVersionUID = 2053411641402478653L;

	/**
	 * constructor for EmptyLoopException, allows specifying a message
	 * 
	 * @param message
	 *            error message to return
	 */
	public EmptyLoopException(String message) {
		super(message);
	}
	// Blank?
}
