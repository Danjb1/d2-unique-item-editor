package editor;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * FileFilter that only accepts a file with the given filename.
 */
public class FileNameFilter extends FileFilter {

	private String filename;

	public FileNameFilter(String filename) {
		this.filename = filename;
	}

	/**
	 * Accepts folders and "UniqueItems.txt"
	 */
	@Override
	public boolean accept(File f) {
		return !f.isFile() || f.getName().equals(filename);
	}

	/**
	 * Returns a description of this FileFilter
	 */
	@Override
	public String getDescription() {
		return filename;
	}

}
