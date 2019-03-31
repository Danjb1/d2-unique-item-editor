package editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import panels.ChooseItemPanel;
import panels.CostPanel;
import panels.GeneralPanel;
import panels.GraphicsPanel;
import panels.OverwritePanel;
import panels.PreviewPanel;
import panels.PropertiesPanel;
import panels.SoundPanel;

public class UniqueEditor extends Editor implements ActionListener {

    public static final String FILENAME_UNIQUE_ITEMS = "UniqueItems.txt";
    
    private D2Edit d2Edit;
    private Database database;
    
    // GUI
	private GeneralPanel general;
	private PreviewPanel preview;
	private CostPanel cost;
	private GraphicsPanel graphics;
	private SoundPanel sound;
	private PropertiesPanel properties;

	/**
	 * Constructor for a UniqueEditor.
	 * @param d2Edit
	 * @param database
	 */
	public UniqueEditor(D2Edit d2Edit, Database database) {
	    this.d2Edit = d2Edit;
	    this.database = database;
		createFrame();
	}

	/**
	 * Creates the frame for the program.
	 */
	private void createFrame() {
		// Create Components
		JPanel topPanel = new JPanel();
			general = new GeneralPanel(this);
			JPanel rightPanel = new JPanel();
				preview = new PreviewPanel();
				cost = new CostPanel();
				graphics = new GraphicsPanel(this);
				sound = new SoundPanel(this);
				rightPanel.setPreferredSize(new Dimension(250, 420));
				rightPanel.add(preview);
				rightPanel.add(cost);
				rightPanel.add(graphics);
				rightPanel.add(sound);
			topPanel.add(general);
			topPanel.add(rightPanel);
		JPanel bottomPanel = new JPanel();
			properties = new PropertiesPanel(this);
			bottomPanel.add(properties);
		
		// Build MenuBar
		JMenuBar menuBar = new JMenuBar();
			JMenu menuFile = new JMenu("File");
		 		JMenuItem menuNew = new JMenuItem("New Item");
		 		menuNew.addActionListener(this);
			 	JMenuItem menuSave = new JMenuItem("Save Item");
			 	menuSave.addActionListener(this);
			 	JMenuItem menuLoad = new JMenuItem("Load Item");
			 	menuLoad.addActionListener(this);
			menuFile.add(menuNew);
			menuFile.add(menuSave);
			menuFile.add(menuLoad);
		menuBar.add(menuFile);
		
		// Create Frame
		general.refreshItem();
		frame = new JFrame("D2Edit");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(topPanel);
		frame.add(bottomPanel);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Reacts to clicks on the MenuBar.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Item")){
			newItem();
		} else if (e.getActionCommand().equals("Save Item")){
			saveItem();
		} else if (e.getActionCommand().equals("Load Item")){
			loadItem();
		}
	}

	/**
	 * Creates a new item.
	 */
	private void newItem() {
		general.setItemName("");
		general.setVersion("100");
		general.setEnabled("1");
		general.setLadder("");
		general.setRarity("1");
		general.setNoLimit("");
		general.setLevel("1");
		general.setReqLevel("0");
		general.setItemCode("qui");
		general.setCarryOne("");
		cost.setCostMult("5");
		cost.setCostAdd("5000");
		graphics.setWorldColour("");
		graphics.setInvColour("");
		graphics.setWorldImage("");
		graphics.setInvImage("");
		sound.setSoundName("");
		properties.clear();
	}

	/**
	 * Saves an item.
	 * @throws IOException 
	 */
	private void saveItem() {
		// Get File
		File file = d2Edit.getFile(FILENAME_UNIQUE_ITEMS, true);
		if (file == null) return;
		d2Edit.rememberChosenFile(file);

		// Prepare variables
	    String code = getItemCode();
		
		// Check for overwrite
		ArrayList<String> items = d2Edit.readFile(file);
		for (int i = 0; i < items.size(); i++){
			String item = items.get(i);
			String[] itemDetails = item.split("\t");
			if (itemDetails[0].equals(general.getItemName())){
				createOverwriteWindow(file, items, code, i);
				return;
			}
		}
		
		// Save to File
		saveItem(file, code, true, true);
	}

	/**
	 * Creates a window asking the user if an item should be overwritten.
	 * @param file
	 * @param items 
	 * @param code
	 * @param replaceIndex
	 */
	private void createOverwriteWindow(File file, ArrayList<String> items, String code, int replaceIndex) {
		this.frame.setEnabled(false);
		JFrame frame = new JFrame("Item Exists");
		
		JPanel panel = new JPanel();
		OverwritePanel overwritePanel = new OverwritePanel(this, frame, file, items, code, replaceIndex);
		panel.add(overwritePanel);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setAutoRequestFocus(true);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(this.frame);
		frame.setVisible(true);
	}

	/**
	 * Saves this item to the given File.
	 * @param file "UniqueItems.txt" File
	 * @param code Item code
	 * @param append Should the file contents be overwritten or appended?
	 * @param writeString Should a string be added to the string table?
	 */
	public void saveItem(File file, String code, boolean append, boolean writeString) {
		BufferedWriter out = null;
		try {
			
		    // Write to string table
			if (writeString){
			    String tblFile = d2Edit.getTblFileLocation(file);
			    d2Edit.writeToStringTbl(tblFile, general.getItemName());
			} else if (D2Edit.DEBUG_PRINT_WRITESTRING){
				System.out.println("String exists; skipping write string");
			}
			
			// Write to File
			if (D2Edit.DEBUG_PRINT_SAVE){
				if (append){
					System.out.println("Adding item \"" + general.getItemName() + "\" to file");
				} else {
					System.out.println("Overwriting item \"" + general.getItemName() + "\"");
				}
			}
			if (!D2Edit.DEBUG_SAVE_DISABLED){
				out = new BufferedWriter(new FileWriter(file, append));
				out.write(code);
			}
		    
			JOptionPane.showMessageDialog(frame, "Item saved successfully!");
		} catch (IOException ex){
			JOptionPane.showMessageDialog(frame, "Error saving item!");
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads an item.
	 */
	private void loadItem() {
		// Get File
		File file = d2Edit.getFile(FILENAME_UNIQUE_ITEMS, false);
		if (file == null) return;
		d2Edit.rememberChosenFile(file);
	    
		// Read File
		ArrayList<String> fileContents = d2Edit.readFile(file);
	    
	    // Open "Select Item" window
	    createSelectItemWindow(fileContents);
	}

	/**
	 * Gets the item's code.
	 */
	private String getItemCode() {
		String name = general.getItemName(); // Comment only
		String version = general.getVersion();
		String enabled = general.getEnabled();
		String ladder = general.getLadder();
		String rarity = general.getRarity();
		String noLimit = general.getNoLimit();
		String level = general.getItemLevel();
		String reqLevel = general.getRequiredLevel();
		String itemCode = general.getItemCode();
		String type = general.getType(); // Comment only
		String uber = ""; // Unused
		String carryOne = general.getCarryOne();
		String costMult = cost.getCostMult();
		String costAdd = cost.getCostAdd();
		String worldColour = graphics.getEquippedColour();
		String invColour = graphics.getInventoryColour();
		String worldImage = graphics.getWorldImage();
		String invImage = graphics.getInventoryImage();
		String soundName = sound.getSound();
		String soundDelay = sound.getDelay();
		
		String code = name + '\t' + version + '\t' + enabled + '\t' + ladder + '\t' + rarity + '\t' + noLimit + '\t' + level + '\t' + reqLevel + '\t' + itemCode
				 + '\t' + type + '\t' + uber + '\t' + carryOne + '\t' + costMult + '\t' + costAdd + '\t' + worldColour + '\t' + invColour + '\t' + worldImage
				 + '\t' + invImage + '\t' + soundName + '\t' + soundDelay + '\t' + soundName + '\t';
		
		// Add properties
		for (int i = 0; i < 12; i++){
			code += properties.getPropertyString(i) + '\t';
		}
		
		code += "0" + "\r\n"; // End of line
		return code;
	}
	
	/**
	 * Creates a frame where an item can be chosen.
	 */
	private void createSelectItemWindow(ArrayList<String> items) {
		JDialog dialog = new JDialog(frame, "Choose Item", true);
		ChooseItemPanel choosePanel = new ChooseItemPanel(this, dialog, items);
        dialog.setContentPane(choosePanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
	}

	/**
	 * Loads the item with the given code.
	 * @param code
	 */
	public void loadItem(String code) {
		String[] itemDetails = code.split("\t");
		general.setItemName(itemDetails[0]); // Comment only
		general.setVersion(itemDetails[1]);
		general.setEnabled(itemDetails[2]);
		general.setLadder(itemDetails[3]);
		general.setRarity(itemDetails[4]);
		general.setNoLimit(itemDetails[5]);
		general.setItemCode(itemDetails[8]);
		general.setLevel(itemDetails[6]);		// Must be after setItemCode
		general.setReqLevel(itemDetails[7]);	// Must be after setItemCode
		// itemDetails[9] is the type, retrieved automatically from item code
		// itemDetails[10] is the unused "uber" flag
		general.setCarryOne(itemDetails[11]);
		cost.setCostMult(itemDetails[12]);
		cost.setCostAdd(itemDetails[13]);
		graphics.setWorldColour(itemDetails[14]);
		graphics.setInvColour(itemDetails[15]);
		graphics.setWorldImage(itemDetails[16]);
		graphics.setInvImage(itemDetails[17]);
		sound.setSoundName(itemDetails[18]);
		// itemDetails[19] is the sound delay, retrieved automatically
		// itemDetails[20] is the same as itemDetails[18]

		// Get properties
		int propertiesStart = 21;
		int propertiesLength = 48;
		properties.clear();
		for (int i = propertiesStart; i < propertiesStart + propertiesLength; i += 4){
			String paramCode = itemDetails[i];
			String paramZ = itemDetails[i+1];
			String paramX = itemDetails[i+2];
			String paramY = itemDetails[i+3];
			String paramDescription = "";
			// Retrieve description from paramCode
			String[] propList = database.getListColumn(Database.ITEM_PROPERTIES, 1);
			for (int j = 0; j < propList.length; j++){
				if (propList[j] != null && propList[j].equals(paramCode)){ // Match found
					paramDescription = database.getListItem(Database.ITEM_PROPERTIES, 0, j);
					break;
				}
				paramDescription = "Unrecognised property: " + paramCode;
			}
			if (!paramCode.equals("")){	// Ignore blank properties
				ItemProperty property = new ItemProperty(paramDescription, paramCode, paramX, paramY, paramZ);
				properties.addProperty(property);
			}
		}
	}

    /**
     * Getter for preview panel.
     * @return
     */
    public PreviewPanel getPreviewPanel() {
        return preview;
    }

    /**
     * Getter for general panel.
     * @return
     */
    public GeneralPanel getGeneralPanel() {
        return general;
    }
    
    /**
     * Getter for graphics panel.
     * @return
     */
    public GraphicsPanel getGraphicsPanel(){
        return graphics;
    }

    /**
     * Getter for properties panel.
     */
    public PropertiesPanel getPropertiesPanel() {
        return properties;
    }
    
    public Database getDatabase() {
        return database;
    }

}
