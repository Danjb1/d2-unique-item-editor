package panels;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import editor.Database;
import editor.UniqueEditor;

public class SoundPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Database database;
	private JComboBox<String> selSound;

	/**
	 * Creates a SoundPanel
	 * @param editor
	 */
	public SoundPanel(UniqueEditor editor) {
		this.database = editor.getDatabase();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(250, 60));

		// Create Components

		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(
				loweredEtched, " Sound ");
		title.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		this.setBorder(title);

		JPanel panelSound = new JPanel();

			JLabel labelSound = new JLabel("Sound:");
			selSound = new JComboBox<String>(database.getListColumn(Database.SOUNDS, 0));
			selSound.setMaximumRowCount(15);

			panelSound.add(labelSound);
			panelSound.add(selSound);

		// Add Components
		add(panelSound);
	}

	/**
	 * Getter for sound
	 * @return
	 */
	public String getSound() {
		int index = selSound.getSelectedIndex();
		if (index > 0){
			String sound = database.getListItem(Database.SOUNDS, 1, index);
			if (sound != null){
				return sound;
			}
		}
		return "";
	}

	/**
	 * Getter for delay
	 * @return
	 */
	public String getDelay() {
		int index = selSound.getSelectedIndex();
		if (index > 0){
			return database.getListItem(Database.SOUNDS, 2, index);
		} else {
			return "";
		}
	}

	/**
	 * Setter for sound name
	 * @param sound
	 */
	public void setSoundName(String sound) {
		if (sound.equals("")){
			selSound.setSelectedIndex(0);
		} else {
			String[] sounds = database.getListColumn(Database.SOUNDS, 1);
			for (int i = 1; i < sounds.length; i++){ // Skip "Default" row
				if (sounds[i] != null && sounds[i].equals(sound)){
					selSound.setSelectedIndex(i);
					return;
				}
			}
		}
	}

}
