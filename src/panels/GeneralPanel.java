package panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import editor.D2Edit;
import editor.Database;
import editor.UniqueEditor;

public class GeneralPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private UniqueEditor editor;
	private Database database;
	
	private JComboBox<String> selCategory, selClass, selType;
	private int itemTypes = Database.TYPE_A_ARMOUR;
	private String imageCode, altImage1, altImage2;
	private JRadioButton checkNormal, checkExceptional, checkElite,
			checkImageStandard, checkImageAlt1, checkImageAlt2;
	private int itemTier;
	private JTextField textName, textItemLevel, textReqLevel, textRarity;
	private JCheckBox checkEnabled, checkExpansion, checkLadder, 
			checkMultispawn, checkLimitOne;
	private int currentType = -1;
	
	/**
	 * Creates a GeneralPanel
	 * @param editor
	 */
	public GeneralPanel(UniqueEditor editor) {
		this.editor = editor;
		this.database = editor.getDatabase();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 420));
		
		// Create components
		
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(
                loweredEtched, " General ");
		title.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		this.setBorder(title);
		
		JPanel panelName = new JPanel();
		
			JLabel labelName = new JLabel("Name:");
			
			textName = new JTextField(22);
	
			panelName.add(labelName);
			panelName.add(textName);
		
		JPanel panelCategory = new JPanel();
		
			JLabel labelCategory = new JLabel("Category:");
			
			selCategory = new JComboBox<String>(database.getListColumn(Database.CATEGORIES, 0));
			selCategory.setPreferredSize(new Dimension(160, 20));
			selCategory.addActionListener(this);

			panelCategory.add(labelCategory);
			panelCategory.add(selCategory);
			
		JPanel panelClass = new JPanel();
			
			JLabel labelClass = new JLabel("      Class:");
			
			selClass = new JComboBox<String>(database.getListColumn(Database.CLASS_ARMOUR, 0));
			selClass.setPreferredSize(new Dimension(160, 20));
			selClass.addActionListener(this);
			selClass.setMaximumRowCount(15);

			panelClass.add(labelClass);
			panelClass.add(selClass);
			
		JPanel panelType = new JPanel();

			JLabel labelType = new JLabel("       Type:");
			
			selType = new JComboBox<String>(database.getListColumn(Database.TYPE_A_ARMOUR, itemTier));
			selType.setPreferredSize(new Dimension(160, 20));
			selType.addActionListener(this);
			selType.setMaximumRowCount(15);
	
			panelType.add(labelType);
			panelType.add(selType);
			
		JPanel panelTier = new JPanel();
		panelTier.setLayout(new BoxLayout(panelTier, BoxLayout.Y_AXIS));
		
			JLabel labelTier = new JLabel("Item Tier:");
			
			checkNormal = new JRadioButton("Normal");
			checkNormal.setSelected(true);
			checkNormal.addActionListener(this);
			
			checkExceptional = new JRadioButton("Exceptional");
			checkExceptional.addActionListener(this);
	
			checkElite = new JRadioButton("Elite");
			checkElite.addActionListener(this);
	
			ButtonGroup tierGroup = new ButtonGroup();
			tierGroup.add(checkNormal);
			tierGroup.add(checkExceptional);
			tierGroup.add(checkElite);

			panelTier.add(labelTier);
			panelTier.add(checkNormal);
			panelTier.add(checkExceptional);
			panelTier.add(checkElite);

		JPanel panelImage = new JPanel();
		panelImage.setLayout(new BoxLayout(panelImage, BoxLayout.Y_AXIS));
			
			JLabel labelImage = new JLabel("Image:");
		
			checkImageStandard = new JRadioButton("Standard Image");
			checkImageStandard.setSelected(true);
			checkImageStandard.addActionListener(this);
			
			checkImageAlt1 = new JRadioButton("Alt Image 1");
			checkImageAlt1.addActionListener(this);
			checkImageAlt1.setEnabled(false);
	
			checkImageAlt2 = new JRadioButton("Alt Image 2");
			checkImageAlt2.addActionListener(this);
			checkImageAlt2.setEnabled(false);
	
			ButtonGroup imageGroup = new ButtonGroup();
			imageGroup.add(checkImageStandard);
			imageGroup.add(checkImageAlt1);
			imageGroup.add(checkImageAlt2);
			
			panelImage.add(labelImage);
			panelImage.add(checkImageStandard);
			panelImage.add(checkImageAlt1);
			panelImage.add(checkImageAlt2);

		JPanel panelRadioBoxes = new JPanel();
		
			panelRadioBoxes.add(panelTier);
			panelRadioBoxes.add(Box.createRigidArea(new Dimension(10, 0)));
			panelRadioBoxes.add(panelImage);


		JPanel panelItemLevel = new JPanel();
			
			JLabel labelItemLevel = new JLabel("       Item Level:");
			
			textItemLevel = new JTextField("1");
			textItemLevel.setPreferredSize(new Dimension(50, 20));

			panelItemLevel.add(labelItemLevel);
			panelItemLevel.add(textItemLevel);

		JPanel panelReqLevel = new JPanel();
			
			JLabel labelReqLevel = new JLabel("Required Level:");
			
			textReqLevel = new JTextField("1");
			textReqLevel.setPreferredSize(new Dimension(50, 20));

			panelReqLevel.add(labelReqLevel);
			panelReqLevel.add(textReqLevel);

		JPanel panelRarity = new JPanel();
				
			JLabel labelRarity = new JLabel("              Rarity:");
			
			textRarity = new JTextField("1");
			textRarity.setPreferredSize(new Dimension(50, 20));

			panelRarity.add(labelRarity);
			panelRarity.add(textRarity);

		JPanel panelCheckBoxes = new JPanel();
			panelCheckBoxes.setPreferredSize(new Dimension(180, 60));
			
			checkEnabled = new JCheckBox("Enabled");
			checkEnabled.setSelected(true);
			checkExpansion = new JCheckBox("Expansion");
			checkExpansion.setSelected(true);
			checkLadder = new JCheckBox("Ladder");
			checkMultispawn = new JCheckBox("Multispawn");
			checkLimitOne = new JCheckBox("Limit 1");

			panelCheckBoxes.add(checkEnabled);
			panelCheckBoxes.add(checkExpansion);
			panelCheckBoxes.add(checkLadder);
			panelCheckBoxes.add(checkMultispawn);
			panelCheckBoxes.add(checkLimitOne);
			
		// Add components
		add(panelName);
		add(panelCategory);
		add(panelClass);
		add(panelType);
		add(panelRadioBoxes);
		add(panelItemLevel);
		add(panelReqLevel);
		add(panelRarity);
		add(panelCheckBoxes);
		
	}

	/**
	 * Reacts to clicks on the various Components
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		 if (e.getSource().equals(selCategory)){
			 refreshClass();
		 } else if (e.getSource().equals(selClass)){
			 refreshType();
		 } else if (e.getSource().equals(selType)){
			 refreshItem();
		 } else if (e.getSource().equals(checkNormal)){
			 if (checkNormal.isSelected()){
				 itemTier = 0;
				 int i = selType.getSelectedIndex();
				 refreshType();
				 selType.setSelectedIndex(i);
			 }
		 } else if (e.getSource().equals(checkExceptional)){
			 if (checkExceptional.isSelected()){
				 itemTier = 1;
				 int i = selType.getSelectedIndex();
				 refreshType();
				 selType.setSelectedIndex(i);
			 }
		 } else if (e.getSource().equals(checkElite)){
			 if (checkElite.isSelected()){
				 itemTier = 2;
				 int i = selType.getSelectedIndex();
				 refreshType();
				 selType.setSelectedIndex(i);
			 }
		 } else if (e.getSource().equals(checkImageStandard)){
			 refreshItem();
		 } else if (e.getSource().equals(checkImageAlt1)){
			 refreshItem();
		 } else if (e.getSource().equals(checkImageAlt2)){
			 refreshItem();
		 }
	}

	/**
	 * Refreshes the "Class" dropdown list based on the chosen category
	 */
	private void refreshClass() {
		if (selCategory.getSelectedItem().equals("Armour")){
			 selClass.setModel(new DefaultComboBoxModel<String>(database.getListColumn(Database.CLASS_ARMOUR, 0)));
		 } else if (selCategory.getSelectedItem().equals("Jewellery")){
			 selClass.setModel(new DefaultComboBoxModel<String>(database.getListColumn(Database.CLASS_JEWELLERY, 0)));
		 } else if (selCategory.getSelectedItem().equals("Weapons")){
			 selClass.setModel(new DefaultComboBoxModel<String>(database.getListColumn(Database.CLASS_WEAPONS, 0)));
		 }
		refreshType();
	}
	
	/**
	 * Refreshes the "Type" dropdown list based on the chosen type
	 */
	private void refreshType() {
		if (selCategory.getSelectedIndex() == 0){
			itemTypes = selClass.getSelectedIndex() + Database.TYPE_A_ARMOUR;
		} else if (selCategory.getSelectedIndex() == 1){
			itemTypes = selClass.getSelectedIndex() + Database.TYPE_J_AMULETS;
		} else {
			itemTypes = selClass.getSelectedIndex() + Database.TYPE_W_AMAZON;
		}
		String[] itemNames = database.getListColumn(itemTypes, itemTier);
		selType.setModel(new DefaultComboBoxModel<String>(itemNames));
		currentType = -1;
		refreshItem();
	}

	/**
	 * Refreshes the graphics options and preview when a new item is chosen
	 */
	public void refreshItem() {
		refreshImageCodes();
		// Get image code
		if (!editor.getGraphicsPanel().imageOverride()){
			if (checkImageStandard.isSelected()){
				editor.getPreviewPanel().setImage(D2Edit.loadImage(imageCode));
			} else if (checkImageAlt1.isSelected()){
				editor.getPreviewPanel().setImage(D2Edit.loadImage(altImage1));
			} else if (checkImageAlt2.isSelected()){
				editor.getPreviewPanel().setImage(D2Edit.loadImage(altImage2));
			}
		}
		// Disable item tier boxes if item type is the same for normal and exceptional columns
		if (database.getListItem(itemTypes, 4, selType.getSelectedIndex()).equals(
		        database.getListItem(itemTypes, 5, selType.getSelectedIndex()))){
			checkNormal.setSelected(true);
			checkExceptional.setEnabled(false);
			checkElite.setEnabled(false);
			itemTier = 0;
		} else {
			checkExceptional.setEnabled(true);
			checkElite.setEnabled(true);
		}
		// Get item level and required level - only when item is changed
		if (selType.getSelectedIndex() != currentType){
			textItemLevel.setText(database.getListItem(itemTypes, 7 + itemTier, selType.getSelectedIndex()));
			textReqLevel.setText(database.getListItem(itemTypes, 10 + itemTier, selType.getSelectedIndex()));
			currentType = selType.getSelectedIndex();
		}
	}

	/**
	 * Refreshes the image codes and enables / disables the "Alt Image" options
	 */
	private void refreshImageCodes() {
		// Get image code
		String[] imageCodeList;
		imageCodeList = database.getListColumn(itemTypes, 3);
		imageCode = imageCodeList[selType.getSelectedIndex()];
		// Look for alternate images
		String[] altImageList = database.getListColumn(Database.IMAGE_REPLACEMENTS, 0);
		for (int i = 0; i < altImageList.length; i++){
			if (imageCode.equals(altImageList[i])){
				checkImageAlt1.setEnabled(true);
				altImage1 = database.getListItem(Database.IMAGE_REPLACEMENTS, 1, i);
				altImage2 = database.getListItem(Database.IMAGE_REPLACEMENTS, 2, i);
				if (altImage2 != null){
					checkImageAlt2.setEnabled(true);
				} else {
					checkImageAlt2.setEnabled(false);
					if (checkImageAlt2.isSelected()) checkImageAlt1.setSelected(true);
				}
				return;
			}
		}
		checkImageStandard.setSelected(true);
		checkImageAlt1.setEnabled(false);
		checkImageAlt2.setEnabled(false);
	}

	/**
	 * Getter for item name
	 * @return
	 */
	public String getItemName() {
		return textName.getText();
	}

	/**
	 * Getter for version
	 * @return
	 */
	public String getVersion() {
		if (checkExpansion.isSelected()){
			return "100";
		} else {
			return "0";
		}
	}

	/**
	 * Getter for "enabled"
	 * @return
	 */
	public String getEnabled() {
		if (checkEnabled.isSelected()){
			return "1";
		} else {
			return "";
		}
	}

	/**
	 * Getter for "ladder" flag
	 * @return
	 */
	public String getLadder() {
		if (checkLadder.isSelected()){
			return "1";
		} else {
			return "";
		}
	}

	/**
	 * Getter for rarity
	 * @return
	 */
	public String getRarity() {
		return textRarity.getText();
	}

	/**
	 * Getter for "no limit" flag
	 * @return
	 */
	public String getNoLimit() {
		if (checkMultispawn.isSelected()){
			return "1";
		} else {
			return "";
		}
	}

	/**
	 * Getter for item level
	 * @return
	 */
	public String getItemLevel() {
		return textItemLevel.getText();
	}

	/**
	 * Getter for required level
	 * @return
	 */
	public String getRequiredLevel() {
		return textReqLevel.getText();
	}

	/**
	 * Getter for item code
	 * @return
	 */
	public String getItemCode() {
		int column = 4 + itemTier;
		return database.getListItem(itemTypes, column, selType.getSelectedIndex());
	}

	/**
	 * Getter for type
	 * @return
	 */
	public String getType() {
		return selType.getItemAt(selType.getSelectedIndex());
	}

	/**
	 * Getter for "carry one" flag
	 * @return
	 */
	public String getCarryOne() {
		if (checkLimitOne.isSelected()){
			return "1";
		} else {
			return "";
		}
	}
	
	/**
	 * Getter for inventory image code
	 * @return
	 */
	public String getInventoryImage() {
		if (checkImageAlt1.isSelected()){
			return "inv" + altImage1;
		} else if (checkImageAlt2.isSelected()){
			return "inv" + altImage2;
		} else {
		    // The user has opted for the standard graphic, so we return it
		    // explicitly. If we leave this blank, some items will automatically
		    // apply a unique image.
			return "inv" + imageCode;
		}
	}

	/**
	 * Setter for name
	 * @param name
	 */
	public void setItemName(String name) {
		textName.setText(name);
	}

	/**
	 * Setter for version
	 * @param version
	 */
	public void setVersion(String version) {
		if (version.equals("100")){
			checkExpansion.setSelected(true);
		} else {
			checkExpansion.setSelected(false);
		}
	}

	/**
	 * Setter for "enabled" flag
	 * @param enabled
	 */
	public void setEnabled(String enabled) {
		if (enabled.equals("1")){
			checkEnabled.setSelected(true);
		} else {
			checkEnabled.setSelected(false);
		}
	}

	/**
	 * Setter for "ladder" flag
	 * @param ladder
	 */
	public void setLadder(String ladder) {
		if (ladder.equals("1")){
			checkLadder.setSelected(true);
		} else {
			checkLadder.setSelected(false);
		}
	}

	/**
	 * Setter for rarity
	 * @param rarity
	 */
	public void setRarity(String rarity) {
		textRarity.setText(rarity);
	}

	/**
	 * Setter for "no limit" flag
	 * @param noLimit
	 */
	public void setNoLimit(String noLimit) {
		if (noLimit.equals("1")){
			checkMultispawn.setSelected(true);
		} else {
			checkMultispawn.setSelected(false);
		}
	}

	/**
	 * Setter for item level
	 * @param level
	 */
	public void setLevel(String level) {
		textItemLevel.setText(level);
	}

	/**
	 * Setter for required level
	 * @param reqLevel
	 */
	public void setReqLevel(String reqLevel) {
		textReqLevel.setText(reqLevel);
	}

	/**
	 * Setter for item code
	 * @param itemCode
	 */
	public void setItemCode(String itemCode) {
		// For every item type...
		for (int fileId = Database.TYPE_A_ARMOUR; fileId <= Database.TYPE_W_WANDS; fileId++){
			
			// For every item tier...
			int normalColumn = 4;
			int eliteColumn = 6;
			for (int tierColumn = normalColumn; tierColumn <= eliteColumn; tierColumn++){

				// Check every item of the specified type and tier
				String[] items = database.getListColumn(fileId, tierColumn);
				for (int i = 0; i < items.length; i++){
					if (items[i].equals(itemCode)){	// Match found
						
						// Set item tier
						itemTier = tierColumn - normalColumn;
						if (itemTier == 0){
							checkNormal.setSelected(true);
						} else if (itemTier == 1){
							checkExceptional.setSelected(true);
						} else {
							checkElite.setSelected(true);
						}
						
						// Set category and class
						if (fileId <= Database.TYPE_A_SHIELDS){
							selCategory.setSelectedIndex(0);
							refreshClass();
							selClass.setSelectedIndex(fileId - Database.TYPE_A_ARMOUR);
						} else if (fileId <= Database.TYPE_J_RINGS){
							selCategory.setSelectedIndex(1);
							refreshClass();
							selClass.setSelectedIndex(fileId - Database.TYPE_J_AMULETS);
						} else {
							selCategory.setSelectedIndex(2);
							refreshClass();
							selClass.setSelectedIndex(fileId - Database.TYPE_W_AMAZON);
						}
						
						// Set type
						refreshType();
						selType.setSelectedIndex(i);
						return;
						
					}
				}
			}
		}
	}

	/**
	 * Setter for "carry one" flag
	 * @param carryOne
	 */
	public void setCarryOne(String carryOne) {
		if (carryOne.equals("1")){
			checkLimitOne.setSelected(true);
		} else {
			checkLimitOne.setSelected(false);
		}
	}

	/**
	 * Setter for image code
	 * @param img
	 */
	public void setImage(String img) {
		refreshImageCodes();
		if (altImage1 != null && altImage1.equals(img)){
			checkImageAlt1.setSelected(true);
			editor.getPreviewPanel().setImage(D2Edit.loadImage(altImage1));
		} else if (altImage2 != null && altImage2.equals(img)){
			checkImageAlt2.setSelected(true);
			editor.getPreviewPanel().setImage(D2Edit.loadImage(altImage2));
		} else {
			checkImageStandard.setSelected(true);
			editor.getPreviewPanel().setImage(D2Edit.loadImage(imageCode));
		}
	}
	
}
