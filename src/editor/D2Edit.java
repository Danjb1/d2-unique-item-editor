package editor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ca.mb.javajeff.pcx.PCXImageReader;
import ca.mb.javajeff.pcx.PCXImageReaderSpi;

/**
 * Main class used to hold global program data.
 *
 * @author Dan Bryce
 */
public class D2Edit {

	public static final boolean DEBUG_SAVE_DISABLED = false;
	public static final boolean DEBUG_WRITESTRING_DISABLED = false;
	public static final boolean DEBUG_PRINT_SAVE = true;
	public static final boolean DEBUG_PRINT_WRITESTRING = true;

	public static final String FILENAME_CONFIG = "preferences.cfg";
	public static final String FILENAME_DTBL_EXE =
			System.getProperty("user.dir") + "/dtbl/dtbl.exe";

	/**
	 * The loaded database of game data.
	 */
	private Database database;

	/**
	 * The currently-open editor.
	 */
	private Editor editor;

	/**
	 * Main method to launch the program.
	 * @param args
	 */
	public static void main(String[] args) {
		// Set System L&F
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new D2Edit();
	}

	/**
	 * Constructor for D2Edit that initialises the program.
	 */
	public D2Edit() {
		database = new Database();
		editor = new UniqueEditor(this, database);
	}

	/**
	 * Loads a pcx image with the given filename.
	 * @param filename Filename, ignoring "inv" prefix and ".pcx" extension.
	 * @return BufferedImage containing the loaded image
	 */
	public static BufferedImage loadImage(String filename) {
		String filePath = "gfx/inv" + filename + ".pcx";
		BufferedImage loadedImage = null;
		try {
			PCXImageReader reader = new PCXImageReader(new PCXImageReaderSpi());
			reader.setInput(ImageIO.createImageInputStream(new File(filePath)));
			loadedImage = reader.read(0);
		} catch (Exception e) {
			System.out.println("Unable to load image: " + filePath);
		}
		return loadedImage;
	}

	/**
	 * Reads the given File.
	 * @param file
	 * @return ArrayList of Strings, where each element is a line of the File
	 */
	public ArrayList<String> readFile(File file) {
		BufferedReader in = null;
		ArrayList<String> fileContents = new ArrayList<String>();
		try {
			String nextLine = "";
			in = new BufferedReader(new FileReader(file));
			in.readLine();	// Skip header row
			while ((nextLine = in.readLine()) != null){
				fileContents.add(nextLine);
			}
		} catch (IOException ex){
			JOptionPane.showMessageDialog(editor.getFrame(), "Error reading file!");
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileContents;
	}

	/**
	 * Loads the default directory for the FileChooser.
	 * @return
	 */
	public String getDefaultDirectory() {
		BufferedReader in = null;
		String dir = null;
		try {
			in = new BufferedReader(new FileReader(FILENAME_CONFIG));
			dir = in.readLine();
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
		return dir;
	}

	/**
	 * Gets the user to select the mod's "UniqueItems.txt" file to save to / load from.
	 * @param filename
	 * @param save Is this a Save dialog?
	 * @return
	 */
	public File getFile(String filename, boolean save) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(getDefaultDirectory()));
		fc.setFileFilter(new FileNameFilter(filename));
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = 0;
		if (save){
			fc.setDialogTitle("Select a " + filename + " to save to");
			returnVal = fc.showSaveDialog(editor.getFrame());
		} else {
			fc.setDialogTitle("Select a " + filename + " to load from");
			returnVal = fc.showOpenDialog(editor.getFrame());
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) return null;
		return fc.getSelectedFile();
	}

	/**
	 * Saves the chosen File's directory to make it the default.
	 * @param file
	 */
	public void rememberChosenFile(File file) {
		BufferedWriter out = null;
		try {
			String dir = file.getParent();
			out = new BufferedWriter(new FileWriter(FILENAME_CONFIG));
			out.write(dir);
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Returns the location of the "Strings.tbl" file from the mod folder.
	 * @param modFolder Filename of any File within the mod's "Excel" folder
	 * @return
	 */
	public String getTblFileLocation(File modFolder) {
		// Folder: Data\Global\Excel\[File]
		String dataFolder = modFolder.getParent();
		// Folder: Data\Global\Excel
		dataFolder = dataFolder.substring(0, dataFolder.lastIndexOf(File.separatorChar));
		// Folder: Data\Global
		dataFolder = dataFolder.substring(0, dataFolder.lastIndexOf(File.separatorChar));
		// Folder: Data
		dataFolder += "/Local/Lng/Eng/patchstring.tbl";
		return dataFolder;
	}

	/**
	 * Writes the item name to the mod's string table.
	 * @throws IOException
	 */
	public void writeToStringTbl(String tblFile, String string) throws IOException {
		if (DEBUG_PRINT_WRITESTRING){
			System.out.println("Adding string \"" + string + "\" to patchstring.tbl");
		}
		if (DEBUG_WRITESTRING_DISABLED){
			return;
		}
		ProcessBuilder pb = new ProcessBuilder(FILENAME_DTBL_EXE, "add", "--duplicate=fail", string, string, enquote(tblFile));
		pb.start();
	}

	private String enquote(String string) {
		return "\"" + string + "\"";
	}

	public Database getDatabase() {
		return database;
	}

}
