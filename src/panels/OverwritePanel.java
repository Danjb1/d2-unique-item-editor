package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import editor.UniqueEditor;

public class OverwritePanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private UniqueEditor editor;
	private JFrame frame;
	private File file;
	private ArrayList<String> items;
	private String code;
	private int replaceIndex;

	/**
	 * Creates an OverwritePanel
	 * @param d2Edit
	 * @param frame
	 * @param file
	 * @param items 
	 * @param code
	 * @param replaceIndex
	 */
	public OverwritePanel(UniqueEditor editor, JFrame frame, File file, ArrayList<String> items, String code, int replaceIndex) {
		this.editor = editor;
		this.frame = frame;
		this.file = file;
		this.items = items;
		this.code = code;
		this.replaceIndex = replaceIndex;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Create Components
		JPanel labelPanel = new JPanel();
			JLabel msg1 = new JLabel("An item with this name already exists in the selected file.");
			JLabel msg2 = new JLabel("Do you wish to overwrite it?");
			labelPanel.add(msg1);
			labelPanel.add(msg2);
		JPanel buttonPanel = new JPanel();
			JButton buttonSave = new JButton("Yes");
			buttonSave.addActionListener(this);
			JButton buttonCancel = new JButton("No");
			buttonCancel.addActionListener(this);
			buttonPanel.add(buttonSave);
			buttonPanel.add(buttonCancel);
		
		// Add Components
		add(labelPanel);
		add(buttonPanel);
	}

	/**
	 * Reacts to button presses
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.dispose();
		editor.getFrame().setEnabled(true);
		editor.getFrame().toFront();
		if (e.getActionCommand().equals("Yes")){
			editor.saveItem(file, makeFileContents(), false, false);
		} else {
			editor.saveItem(file, code, true, false);	// Append
		}
	}

	/**
	 * Recreates the "UniqueItems.txt" file with the desired item replaced
	 * @return
	 */
	private String makeFileContents() {
		code = code.substring(0, code.lastIndexOf('0') + 1);	// Cut off line break (it will be added in the loop below)
		items.set(replaceIndex, code);	// Overwrite item
		// Combine items in one giant String
		String fileContents = "index\tversion\tenabled\tladder\trarity\tnolimit\tlvl\tlvl req\tcode\t*type\t" +
				"*uber\tcarry1\tcost mult\tcost add\tchrtransform\tinvtransform\tflippyfile\tinvfile\tdropsound\t" +
				"dropsfxframe\tusesound\tprop1\tpar1\tmin1\tmax1\tprop2\tpar2\tmin2\tmax2\tprop3\tpar3\tmin3\t" +
				"max3\tprop4\tpar4\tmin4\tmax4\tprop5\tpar5\tmin5\tmax5\tprop6\tpar6\tmin6\tmax6\tprop7\tpar7\t" +
				"min7\tmax7\tprop8\tpar8\tmin8\tmax8\tprop9\tpar9\tmin9\tmax9\tprop10\tpar10\tmin10\tmax10\t" +
				"prop11\tpar11\tmin11\tmax11\tprop12\tpar12\tmin12\tmax12\t*eol\r\n";	// Header row
		for (String item : items){
			fileContents += item + "\r\n";
		}
		return fileContents;
	}
	
}
