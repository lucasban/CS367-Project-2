import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Title:            Program 2 (ImageLoopEditor)
//Files:            ImageLoopEditor.java,LinkedLoop.java,EmptyLoopException.java,
//					LinkedLoopIterator.Java
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
 * This class implements a GUI-based editor for an Image Loop editor. A
 * LinkedLoop<Image> named loop is declared. You must complete the code in the
 * methods named pushXXX to implement the individual editor commands.
 * 
 * <p>
 * Bugs: none known
 * 
 * @author Lucas Bannister
 * 
 */
public class ImageLoopEditor {

	// This is the main LinkedLoop holding the loop of Images
	protected static LoopADT<Image> loop = new LinkedLoop<Image>();

	// Methods that implement the GUI buttons' actions

	/**
	 * Searches for an image with a given string in the title. If the image loop is
	 * empty, display "no images". Otherwise, find (by searching forward in the
	 * image loop) the first image whose title contains the given string (which may
	 * be quoted). If no image with a title containing string is found, display "not
	 * found"; otherwise, make that image the current image and display the current
	 * context (see note below). Comparison is case-sensitive, so "rin tin tin" does
	 * not match "Rin Tin Tin".
	 * 
	 * @param title
	 *            the string to search for in the title of the images
	 * @return a string indicating the result of the action, see description
	 */
	static String pushFind(String title) {
		// If the loop is empty, return "no images"
		if (loop.isEmpty()) {
			return "no images\n";
		} else if (title.isEmpty()) {
			// If we aren't searching for anything, we haven't found it
			return "not found\n";
		}

		// Initialize variables
		// The count of the offset between the current image and the found image
		int cnt = 0;
		// Whether or not the image has been found
		boolean imgFound = false;
		// Create iterator
		Iterator<Image> itr = loop.iterator();

		// iterate through until we found it or run out of images
		while (itr.hasNext() && (imgFound == false)) {
			Image imgNode = itr.next();
			// if the title contains the search string, we found it
			if (imgNode.getTitle().contains(title)) {
				imgFound = true;
			} else {
				// keep track of how many times we will have to jump
				cnt++;
			}
		}
		// If we found it, jump forward to make it the current image
		if (imgFound) {
			jumpHelper(cnt);
		}
		// If we didn't find it, say so.
		else {
			return "not found\n";
		}

		// return current context
		return currentContext();
	}

	/**
	 * If the image loop is empty, display "no images to save". Otherwise, save all
	 * the images to a file named filename, one image per line starting with the
	 * current image and proceeding forward through the loop. For each image, save
	 * the file name, the duration and the title. A null title (zero characters) is
	 * allowed; see example. If filename already exists, display "warning: file
	 * already exists, will be overwritten" before saving the images. If filename
	 * cannot be written to, display "unable to save".
	 * 
	 * @param filename
	 *            the name of the file to save to
	 * @return a string indicating the result of the action, see description
	 */
	static String pushSave(String filename) {
		// First check that there is something to save
		if (loop.isEmpty()) {
			return "no images to save\n";
		}

		// initialize variables
		// the string that will be returned
		String outStr = "";
		// the printstream that will be used to write to the file
		PrintStream printStream = null;

		try {
			// Validate filename
			filename = filenameValidationHelper(filename);

			// open the file
			File outFile = new File(filename);

			// Check if existing file is going to be overwritten
			if (outFile.exists()) {
				outStr = "warning: file already exists, will be overwritten\n";
			}

			// open the stream
			printStream = new PrintStream(outFile);

			// Loop through images and write to the file
			Iterator<Image> itr = loop.iterator();
			// Initialize the image to be processed
			Image tmpImg;

			while (itr.hasNext()) {
				// set image to be processed
				tmpImg = itr.next();
				// format the output as "filename duration title"
				String line = tmpImg.getFile() + " " + tmpImg.getDuration() + " " + tmpImg.getTitle();
				// actually save it to the file
				printStream.println(line);
			}
		} catch (IOException E) {
			return "unable to save\n";
		} finally {
			// Close out the PrintStream
			if (printStream != null) {
				printStream.close();
			}
		}
		// return any warning
		return outStr;
	}

	/**
	 * If a file named filename does not exist or cannot be read from, display
	 * "unable to load". Otherwise, load the images from filename in the order they
	 * are given and set the current image to be the first image read from the file.
	 * You may assume that there is one image per line, that there are no blank
	 * lines, and that the file is not empty, i.e., it has at least one line. Each
	 * line contains a filename (a string), a duration (an integer) and a title
	 * (possible null) (see example). If a filename on a line is not in the images
	 * folder display "Warning: filename is not in images folder"
	 * 
	 * @param filename
	 *            the name of the file to load from
	 * @return a string indicating the result of the action, see description
	 */
	static String pushLoad(String filename) {
		// FileInputStream to load the file
		FileInputStream fileInputStream;
		try {
			// validate filename
			filename = filenameValidationHelper(filename);
			// open the file
			fileInputStream = new FileInputStream(filename);
		} catch (IOException E) {
			return "unable to load\n";
		}

		// create scanner
		Scanner scnr = new Scanner(fileInputStream);

		// set up variables needed while reading the file
		String line; // the line of the file being loaded
		String[] splitAry; // an array to hold the pieces of the line once they are split
		Image img; // the temporary image object
		String outStr = ""; // the output string to be returned

		// loop through all lines
		while (scnr.hasNextLine()) {
			// get the next line as a string
			line = scnr.nextLine();

			// Split the line into 3 pieces, breaking the first two times on whitespace with
			// the remainder being the third piece
			splitAry = line.split("\\s+", 3);

			// check that the file exists, first
			if (fileCheckHelper(splitAry[0]) == false) {
				outStr = outStr + "Warning: " + splitAry[0] + "is not in images folder\n";
			} else {
				// only try to load after we know it exists
				// create the image object
				img = new Image(splitAry[0], splitAry[2], Integer.parseInt(splitAry[1]));
				// Actually add it to the loop
				addImageHelper(img);
			}
		}

		// Close out scanner
		scnr.close();

		// go back to the beginning
		loop.next();

		// Return any warnings
		return outStr;
	}

	/**
	 * Helper function to validate filename formatting to ensure it meets the
	 * requirements: must start with at least one non-whitespace character and must
	 * contain only letters (a - z, A - Z), digits (0 - 9), underscores ( _ ),
	 * periods ( . ), slashes (/) and dashes ( - ).
	 * 
	 * @param filename
	 *            the filename to be validated
	 * @return filename if it is valid, otherwise ""
	 * @throws IOException
	 */
	private static String filenameValidationHelper(String filename) throws IOException {
		// if filename passed is "filename", treat it as null
		if (filename.equals("filename")) {
			filename = "";
		}
		// Using regex to ensure that filename matches the pattern specified (1+
		// characters, of the types specified in the description)
		if (filename.matches("^[a-zA-Z0-9_./\\\\-]+$")) {
			return filename;
		} else {
			throw new IOException();
		}
	}

	/**
	 * Add new image AFTER current image. If the image loop is empty, add a new
	 * image with the given filename, a null title, and a duration of 5 seconds to
	 * the loop and make it the current image. Otherwise, add the new image
	 * immediately after the current image and make the new image the new current
	 * image. In either case, display the current context (see note below). If
	 * filename is not in the images folder display "Warning: filename is not in
	 * images folder"
	 * 
	 * @param filename
	 *            the name of the file to add
	 * @return a string indicating the result of the action, see description
	 */
	static String pushAddImage(String filename) {
		// Check that the file actually exists
		if (fileCheckHelper(filename)) {
			// if it does, add it to the loop and return context
			addImageHelper(new Image(filename));
			return currentContext();
		}
		// otherwise, return the warning
		else {
			return ("Warning: " + filename + " is not in images folder\n");
		}
	}

	/**
	 * Helper function to add an image after the current image
	 * 
	 * @param img
	 *            the image to be added
	 */
	private static void addImageHelper(Image img) {
		// If loop is empty, don't advance
		if (loop.isEmpty()) {
			loop.add(img);
		} else {
			loop.next();
			loop.add(img);
		}
	}

	/**
	 * Insert new image BEFORE current image. If the image loop is empty, add a new
	 * image with the given filename, a null title, and a duration of 5 seconds to
	 * the loop and make it the current image. Otherwise, insert the new image
	 * immediately before the current image and make new image the new current
	 * image. In either case, display the current context. If filename is not in the
	 * images folder display "Warning: filename is not in images folder"
	 * 
	 * @param filename
	 *            the name of the file to add
	 * @return a string indicating the result of the action, see description
	 */
	static String pushInsertImage(String filename) {
		// Check that the file actually exists
		if (fileCheckHelper(filename)) {
			// if it does, add it to the loop and return context
			loop.add(new Image(filename));
			return currentContext();
		}
		// otherwise, return the warning
		else {
			return ("Warning: " + filename + " is not in images folder\n");
		}
	}

	/**
	 * Helper function to check if a file with a given name exists
	 * 
	 * @param filename
	 *            the name of the file being checked
	 * @return true if the file exists, false if it doesn't
	 */
	private static boolean fileCheckHelper(String filename) {
		// attempt to open the file
		File file = new File("images\\" + filename);
		// check if the file exists, and return true if it does
		if (file.exists()) {
			return true;
		}
		// otherwise, return false
		else {
			return false;
		}
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, jump count images
	 * in the loop (forward if count > 0, backwards if count < 0) and display the
	 * current context.
	 * 
	 * @param count
	 *            the number of images to jump by
	 * @return a string indicating the result of the action, see description
	 */
	static String pushJump(String count) {
		// First check for empty
		if (loop.isEmpty()) {
			return "no images\n";
		}

		// Initialize variables
		int intCount = 0;
		// covert input to int
		try {
			intCount = Integer.valueOf(count);
		}
		// if it isn't actually a number, display invalid input message
		catch (NumberFormatException E) {
			return "invalid jump count\n";
		}

		// pass off to helper function
		jumpHelper(intCount);

		// return context
		return currentContext();
	}

	/**
	 * Helper function to jump by a number of images
	 * 
	 * @param intCount
	 *            the number of images to jump by
	 */
	private static void jumpHelper(int intCount) {
		// For positive counts, move forward
		if (intCount > 0) {
			for (int i = 1; i <= intCount; i++) {
				loop.next();
			}
		}

		// for negative count, move backwards
		else if (intCount < 0) {
			for (int i = 1; i <= (-1 * intCount); i++) {
				loop.previous();
			}
		}

		// if count is 0 nothing needs to be done

	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, update the
	 * duration for current image to the given time and display the current context.
	 * 
	 * @param time
	 *            the duration for the current image
	 * @return a string indicating the result of the action, see description
	 */
	static String pushUpdate(String time) {
		// Convert input to int
		int intTime = Integer.valueOf(time);

		// Validate input
		if (intTime < 0) {
			throw new IllegalArgumentException();
		}

		// Update duration
		try {
			loop.getCurrent().setDuration(intTime);
		}
		// If no images, display that
		catch (EmptyLoopException e) {
			return "no images\n";
		}

		// Return context
		return currentContext();
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, edit the title
	 * for current image to the given title (which may be quoted) and display the
	 * current context.
	 * 
	 * @param title
	 *            the title to set for the current image
	 * @return a string indicating the result of the action, see description
	 */
	static String pushEdit(String title) {
		// Strip quotes, if needed
		title = titleValidationHelper(title);

		// Update title
		try {
			loop.getCurrent().setTitle(title);
		}

		// If no images, return that
		catch (EmptyLoopException e) {
			return "no images\n";
		}

		// Return context
		return currentContext();
	}

	/**
	 * Helper function to remove quotes from title strings
	 * 
	 * @param title
	 *            input title string to have quotes stripped from
	 * @return new title string with stripped quotes
	 */
	private static String titleValidationHelper(String title) {
		// Default output is the same as input
		String outStr = title;
		// If the title is 2+ chars long, and both starts and ends with a quote, strip
		// the first and last character from the title
		if (title.length() > 1 && title.startsWith("\"") && title.endsWith("\"")) {
			outStr = outStr.substring(1, outStr.length() - 1);
		}
		return outStr;
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, display all of
	 * the images in the loop, starting with the current image, one image per line
	 * (going forward through the entire loop). Each line is of the form: title
	 * [duration,filename].
	 * 
	 * @return If the image loop is empty, the string "no images". Otherwise, A
	 *         string listing all of the images in the loop, starting with the
	 *         current image, one image per line (going forward through the entire
	 *         loop). Each line is of the form: title [duration,filename].
	 */
	static String pushDisplay() {
		// initialize variables
		String content = "";
		Iterator<Image> itr = loop.iterator();

		// check for empty loop
		if (loop.isEmpty()) {
			return "no images\n";
		}
		// loop through all
		while (itr.hasNext()) {
			Image tempImage = itr.next();
			// add the context string for that image
			content = content + imageContextHelper(tempImage);
			content = content + "\n";
		}
		return content;
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, display the
	 * current image as a photograph, in a window with the image's title and for the
	 * specified duration.
	 * 
	 * @return If the image loop is empty, the string "no images".
	 */
	static String pushShow() {
		// Display the image
		try {
			loop.getCurrent().displayImage();
			return "";
		}
		// if the loop is empty, return "no images"
		catch (EmptyLoopException empty) {
			return "no images\n";
		}
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, test the loop,
	 * starting with the current image, by displaying each image as a photograph in
	 * a window with the image's title and for the specified duration.
	 * 
	 * @return If the image loop is empty, the string "no images".
	 */
	static String pushTest() {
		// Check for empty loop
		if (loop.isEmpty()) {
			return "no images\n";
		}
		// initialize iterator and list to store the images into
		Iterator<Image> itr = loop.iterator();
		List<Image> imageList = new ArrayList<Image>();
		// loop through and add images to list.
		while (itr.hasNext()) {
			Image tmpImage = itr.next();
			imageList.add(tmpImage);
		}
		// Display the list
		Image.displayImageList(imageList);
		return "";
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, remove the
	 * current image. If the image loop becomes empty as a result of the removal,
	 * display "no images". Otherwise, make the image after the removed image the
	 * current image and display the current context.
	 * 
	 * @return If the image loop is empty, or becomes empty as a result of the
	 *         removal, returns the string "no images". Otherwise, returns the
	 *         current context.
	 */
	static String pushRemove() {
		// Remove the current image
		try {
			loop.removeCurrent();
		}
		// If there is nothing to remove, return "no images"
		catch (EmptyLoopException e) {
			return "no images\n";
		}
		// If the loop becomes empty, return "no images"
		if (loop.isEmpty()) {
			return "no images\n";
		}
		// Return current context
		return currentContext();
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, go forward to the
	 * next image in the loop and display the current context.
	 * 
	 * @return If the image loop is empty, returns the string "no images".
	 *         Otherwise, returns the current context.
	 */
	public static String pushForward() {
		// If the loop is empty, return "no images"
		if (loop.isEmpty()) {
			return "no images\n";
		}
		// otherwise, advance to the next image
		loop.next();
		// return context
		return currentContext();
	}

	/**
	 * If the image loop is empty, display "no images". Otherwise, go backwards to
	 * the previous image in the loop and display the current context.
	 * 
	 * @return If the image loop is empty, returns the string "no images".
	 *         Otherwise, returns the current context.
	 */
	public static String pushBack() {
		// If the loop is empty, return "no images"
		if (loop.isEmpty()) {
			return "no images\n";
		}
		// otherwise, move to the previous image
		loop.previous();
		// return context
		return currentContext();
	}

	/**
	 * Helper function to display the current context.
	 * 
	 * @return
	 */
	public static String currentContext() {
		// Initialize variables
		int size = loop.size();
		String outStr = "";

		try {
			// if there is a previous image, print previous image context
			if (size > 2) {
				loop.previous();
				outStr = outStr + ("\t" + imageContextHelper(loop.getCurrent())) + "\n";
				loop.next();
			}

			// Print current image context
			outStr = outStr + ("-->\t" + imageContextHelper(loop.getCurrent()) + " <----\n");

			// if there is a next image, print next image context
			if (size > 1) {
				loop.next();
				outStr = outStr + ("\t" + imageContextHelper(loop.getCurrent()) + "\n");
				loop.previous();
			}
		}

		// If there are no nodes, just don't display anything
		catch (EmptyLoopException e) {
			outStr = "no images";
		}
		return outStr;

	}

	/**
	 * Helper function to generate the image's display string
	 * 
	 * @param img
	 *            Image to generate the string for
	 * @return A string with the Image's information displayed as: Title [FileName,
	 *         Duration]
	 */
	private static String imageContextHelper(Image img) {
		// Initialize variables
		String outStr = "";
		// Don't run on a null object
		if (img != null) {
			// start with the title (stripping quotes if present)
			outStr = outStr + titleValidationHelper(img.getTitle());
			// if we have a title, pad out with a space
			if (outStr.isEmpty() == false) {
				outStr = outStr + " ";
			}
			// add the file name and duration, with proper formatting
			outStr = outStr + "[" + img.getFile() + ", " + img.getDuration() + "]";
		}
		return outStr;
	}

	/**
	 * Display information on available commands.
	 * 
	 * @return Information on available commands
	 */
	static String pushHelp() {
		// You may use this method as implemented here
		String cmds = "";
		cmds += "Available commands:\n" + "\tSave image loop into filename\n" + "\tLoad image loop from filename\n"
				+ "\tAdd Image at filename after current image\n" + "\tInsert Image at filename before current image\n"
				+ "\tFind image matching title\n" + "\tUpdate display time of current image\n"
				+ "\tEdit title of current image\n" + "\tJump count images\n" + "\tDisplay loop\n"
				+ "\tShow current image in a window\n" + "\tTest image loop by showing all images\n"
				+ "\tRemove current image from loop\n" + "\tMove current image forward one step\n"
				+ "\tMove current image back one step\n" + "\tHelp on available commands\n"
				+ "\tQuit and close this GUI\n";
		return cmds;
	}

	/**
	 * Quit execution of the program.
	 * 
	 * @return null string
	 */
	static String pushQuit() {
		// You may use this method as implemented here
		System.exit(0);
		return "";
	}

	/********************************
	 * The following method actually implements our GUI. Major components are
	 * buttons and text fields. The components are defined and placed (in terms of
	 * pixels). You should not change any of this unless you really know what you
	 * are doing. Each button has an "action listener." When you push a button, the
	 * action listener calls a pushXXX method that YOU must define.
	 *********************************/
	public static void runGUI() {

		// f is the JFrame that will be our GUI
		JFrame f = new JFrame("Image Loop");
		// We define the buttons and text areas that will fill the GUI
		// The locations of each component are set, in terms of pixels
		final JTextField tf1 = new JTextField("");
		JTextArea ta = new JTextArea();
		ta.setTabSize(4);
		ta.setBounds(50, 450, 370, 300);
		JButton b1 = new JButton("Save");
		b1.setBounds(50, 25, 110, 30);
		tf1.setBounds(170, 25, 160, 30);
		tf1.setText("filename");
		JButton b2 = new JButton("Load");
		b2.setBounds(50, 60, 110, 30);
		final JTextField tf2 = new JTextField("");
		tf2.setBounds(170, 60, 160, 30);
		tf2.setText("filename");
		JButton b3 = new JButton("Add after");
		b3.setBounds(50, 95, 110, 30);
		final JTextField tf3 = new JTextField("");
		tf3.setBounds(170, 95, 150, 30);
		tf3.setText("filename of image");
		JButton b4 = new JButton("Insert before");
		b4.setBounds(50, 130, 110, 30);
		final JTextField tf4 = new JTextField("");
		tf4.setBounds(170, 130, 150, 30);
		tf4.setText("filename of image");
		JButton b5 = new JButton("Find");
		b5.setBounds(50, 165, 110, 30);
		final JTextField tf5 = new JTextField("");
		tf5.setBounds(170, 165, 150, 30);
		tf5.setText("title");
		JButton b6 = new JButton("Update");
		b6.setBounds(50, 200, 110, 30);
		final JTextField tf6 = new JTextField("");
		tf6.setBounds(170, 200, 150, 30);
		tf6.setText("time");
		JButton b7 = new JButton("Edit");
		b7.setBounds(50, 235, 110, 30);
		final JTextField tf7 = new JTextField("");
		tf7.setBounds(170, 235, 150, 30);
		tf7.setText("title");
		JButton b8 = new JButton("Jump");
		b8.setBounds(50, 270, 110, 30);
		final JTextField tf8 = new JTextField("");
		tf8.setBounds(170, 270, 150, 30);
		tf8.setText("count");
		JButton b9 = new JButton("Display loop");
		b9.setBounds(50, 305, 110, 30);
		JButton b10 = new JButton("Show image");
		b10.setBounds(170, 305, 110, 30);
		JButton b11 = new JButton("Test loop");
		b11.setBounds(50, 340, 110, 30);
		JButton b12 = new JButton("Remove image");
		b12.setBounds(170, 340, 120, 30);
		JButton b13 = new JButton("Move forward");
		b13.setBounds(50, 375, 110, 30);
		JButton b14 = new JButton("Move backward");
		b14.setBounds(170, 375, 110, 30);
		JButton b15 = new JButton("Quit");
		b15.setBounds(50, 410, 110, 30);
		JButton b16 = new JButton("Help");
		b16.setBounds(170, 410, 110, 30);

		// The effect of pushing a GUI button is defined in an ActionListener
		// The actionPerformed method is executed when the associated button is pushed

		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushSave(tf1.getText()));
			}
		});

		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushLoad(tf2.getText()));
			}
		});

		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushAddImage(tf3.getText()));
			}
		});

		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushInsertImage(tf4.getText()));
			}
		});

		b5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushFind(tf5.getText()));
			}
		});

		b6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushUpdate(tf6.getText()));
			}
		});

		b7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushEdit(tf7.getText()));
			}
		});

		b8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushJump(tf8.getText()));
			}
		});

		b9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushDisplay());
			}
		});

		b10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ta.setText(pushShow());
			}
		});

		b11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushTest());
			}
		});

		b12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushRemove());
			}
		});

		b13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushForward());
			}
		});

		b14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushBack());
			}
		});

		b15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushQuit());
			}
		});

		b16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.setText(pushHelp());
			}
		});

		// Buttons and text frames are added to the JFrame to build the GUI

		f.add(b1);
		f.add(tf1);
		f.add(ta);
		f.add(b1);
		f.add(tf1);
		f.add(ta);
		f.add(b1);
		f.add(tf1);
		f.add(ta);
		f.add(b2);
		f.add(tf2);
		f.add(b3);
		f.add(tf3);
		f.add(b4);
		f.add(tf4);
		f.add(b5);
		f.add(tf5);
		f.add(b6);
		f.add(tf6);
		f.add(b7);
		f.add(tf7);
		f.add(b8);
		f.add(tf8);
		f.add(b9);// f.add(tf9);
		f.add(b10);// f.add(tf10);
		f.add(b11);// f.add(tf10);
		f.add(b12);// f.add(tf10);
		f.add(b13);// f.add(tf10);
		f.add(b14);// f.add(tf10);
		f.add(b15);// f.add(tf10);
		f.add(b16);// f.add(tf10);
		f.setBounds(50, 50, 500, 800);
		f.setLayout(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// The GUI is now up and running!

	}

	/**
	 * Main method for running the GUI based editor
	 * 
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {
		runGUI();
	}
}
