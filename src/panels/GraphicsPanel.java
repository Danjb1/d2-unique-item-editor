package panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import editor.D2Edit;
import editor.Database;
import editor.UniqueEditor;

public class GraphicsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private UniqueEditor editor;
	private Database database;
	private String imageName = null;
	private JComboBox<String> selColourInv, selColourWorld, selImageInv, selImageWorld;

	/**
	 * Creates a GraphicsPanel
	 * @param editor
	 */
	public GraphicsPanel(UniqueEditor editor) {
		this.editor = editor;
		this.database = editor.getDatabase();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Create components
		
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(
                loweredEtched, " Graphics ");
		title.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		this.setBorder(title);
		
		JPanel panelGraphics = new JPanel();
		panelGraphics.setPreferredSize(new Dimension(240, 145));
		
			JLabel labelColourInv = new JLabel("Colour (Inventory):");
			JLabel labelColourWorld = new JLabel("  Colour (Equipped):");
			JLabel labelImageInv = new JLabel("Override Inventory Image:");
			JLabel labelImageWorld = new JLabel("Override World Image:");
			
			selColourInv = new JComboBox<String>(database.getListColumn(Database.COLOURS, 0));
			selColourInv.setMaximumRowCount(15);
			selColourWorld = new JComboBox<String>(database.getListColumn(Database.COLOURS, 0));
			selColourWorld.setMaximumRowCount(15);
			selImageInv = new JComboBox<String>(database.getListColumn(Database.IMAGES_INVENTORY, 0));
			selImageInv.setMaximumRowCount(15);
			selImageInv.addActionListener(this);
			selImageWorld = new JComboBox<String>(database.getListColumn(Database.IMAGES_WORLD, 0));
			selImageWorld.setMaximumRowCount(15);

			panelGraphics.add(labelColourInv);
			panelGraphics.add(selColourInv);
			panelGraphics.add(labelColourWorld);
			panelGraphics.add(selColourWorld);
			panelGraphics.add(labelImageInv);
			panelGraphics.add(selImageInv);
			panelGraphics.add(labelImageWorld);
			panelGraphics.add(selImageWorld);
			
		// Add components
		add(panelGraphics);
		
	}

	/**
	 * Reacts to an image being selected
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		imageName = database.getListItem(Database.IMAGES_INVENTORY, 1, selImageInv.getSelectedIndex());
		if (imageName == null){
			editor.getGeneralPanel().refreshItem();
		} else {
			editor.getPreviewPanel().setImage(D2Edit.loadImage(imageName));
		}
	}
	
	/**
	 * Returns true if an image has been selected to override the standard image
	 * @return
	 */
	public boolean imageOverride(){
		return imageName != null;
	}

	/**
	 * Getter for equipped colour code
	 * @return
	 */
	public String getEquippedColour() {
		int index = selColourWorld.getSelectedIndex();
		if (index > 0){
			return database.getListItem(Database.COLOURS, 1, index);
		} else {
			return "";
		}
	}

	/**
	 * Getter for inventory colour code
	 * @return
	 */
	public String getInventoryColour() {
		int index = selColourInv.getSelectedIndex();
		if (index > 0){
			return database.getListItem(Database.COLOURS, 1, index);
		} else {
			return "";
		}
	}

	/**
	 * Getter for world image code
	 * @return
	 */
	public String getWorldImage() {
		int index = selImageWorld.getSelectedIndex();
		if (index > 0){
			return database.getListItem(Database.IMAGES_WORLD, 1, index);
		} else {
			return "";
		}
	}

	/**
	 * Getter for inventory image code
	 * @return
	 */
	public String getInventoryImage() {
		int index = selImageInv.getSelectedIndex();
		if (index > 0){ // Image is being overriden
			return "inv" + database.getListItem(Database.IMAGES_INVENTORY, 1, index);
		} else {
			return editor.getGeneralPanel().getInventoryImage();
		}
	}

	/**
	 * Setter for world colour
	 * @param col
	 */
	public void setWorldColour(String col) {
		if (col.equals("")){
			selColourWorld.setSelectedIndex(0);
		} else {
			String[] colours = database.getListColumn(Database.COLOURS, 1);
			for (int i = 1; i < colours.length; i++){ // Skip "Default" row
				if (colours[i].equals(col)){
					selColourWorld.setSelectedIndex(i);
					return;
				}
			}
		}
	}

	/**
	 * Setter for inventory colour
	 * @param col
	 */
	public void setInvColour(String col) {
		if (col.equals("")){
			selColourInv.setSelectedIndex(0);
		} else {
			String[] colours = database.getListColumn(Database.COLOURS, 1);
			for (int i = 1; i < colours.length; i++){ // Skip "Default" row
				if (colours[i].equals(col)){
					selColourInv.setSelectedIndex(i);
					return;
				}
			}
		}
	}

	/**
	 * Setter for world image
	 * @param img
	 */
	public void setWorldImage(String img) {
		if (img.equals("")){
			selImageWorld.setSelectedIndex(0);
		} else {
			String[] images = database.getListColumn(Database.IMAGES_WORLD, 1);
			for (int i = 1; i < images.length; i++){ // Skip "Default" row
				if (images[i] != null && images[i].equals(img)){
					selImageWorld.setSelectedIndex(i);
					return;
				}
			}
		}
	}

	/**
	 * Setter for inventory image
	 * @param img
	 */
	public void setInvImage(String img) {
		if (!img.equals("")){
			img = img.substring(3);	// Trim "inv" part
			String[] images = database.getListColumn(Database.IMAGES_INVENTORY, 1);
			for (int i = 1; i < images.length; i++){ // Skip "Default" row
				if (images[i] != null && images[i].equals(img)){
					selImageInv.setSelectedIndex(i);
					editor.getPreviewPanel().setImage(D2Edit.loadImage(imageName));
					return;
				}
			}
		}
		// Image not being overriden by the GraphicsPanel
		selImageInv.setSelectedIndex(0);
		editor.getGeneralPanel().setImage(img);
	}
	
}
