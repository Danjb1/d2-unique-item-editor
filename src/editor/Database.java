package editor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Database {

    public static final String FILENAME_DATABASE = "database.dat";
    
    /*
     * Constants representing the index of each section in the database.
     */
    public static final int CATEGORIES = 0;
    public static final int COLOURS = 1;
    public static final int CLASS_ARMOUR = 2;
    public static final int CLASS_JEWELLERY = 3;
    public static final int CLASS_WEAPONS = 4;
    public static final int IMAGE_REPLACEMENTS = 5;
    public static final int IMAGES_INVENTORY = 6;
    public static final int IMAGES_WORLD = 7;
    public static final int SOUNDS = 8;
    public static final int TYPE_A_ARMOUR = 9;
    public static final int TYPE_A_BARBHELMS = 10;
    public static final int TYPE_A_BELTS = 11;
    public static final int TYPE_A_BOOTS = 12;
    public static final int TYPE_A_CIRCLETS = 13;
    public static final int TYPE_A_DRUIDPELTS = 14;
    public static final int TYPE_A_GLOVES = 15;
    public static final int TYPE_A_HELMETS = 16;
    public static final int TYPE_A_NECROHEADS = 17;
    public static final int TYPE_A_PALSHIELDS = 18;
    public static final int TYPE_A_SHIELDS = 19;
    public static final int TYPE_J_AMULETS = 20;
    public static final int TYPE_J_CHARMS = 21;
    public static final int TYPE_J_JEWELS = 22;
    public static final int TYPE_J_RINGS = 23;
    public static final int TYPE_W_AMAZON = 24;
    public static final int TYPE_W_AXES = 25;
    public static final int TYPE_W_BOWS = 26;
    public static final int TYPE_W_CROSSBOWS = 27;
    public static final int TYPE_W_DAGGERS = 28;
    public static final int TYPE_W_JAVELINS = 29;
    public static final int TYPE_W_KATARS = 30;
    public static final int TYPE_W_MACES = 31;
    public static final int TYPE_W_ORBS = 32;
    public static final int TYPE_W_POLEARMS = 33;
    public static final int TYPE_W_SCEPTERS = 34;
    public static final int TYPE_W_SPEARS = 35;
    public static final int TYPE_W_STAVES = 36;
    public static final int TYPE_W_SWORDS = 37;
    public static final int TYPE_W_THROWING = 38;
    public static final int TYPE_W_WANDS = 39;
    public static final int ITEM_PROPERTIES = 40;

    /**
     * Number of sections in the database file.
     */
    private static final int NUM_DATABASE_SECTIONS = 41;
    
    /**
     * The loaded database.
     * An array of sections, where each section is an array of Strings.
     */
    private String[][] database = new String[NUM_DATABASE_SECTIONS][0];

    /**
     * Initialises the database.
     */
    public Database() {
        BufferedReader in = null;
        try {
            // Start at "category -1", i.e. before even the first category
            int currentIndex = -1;
            ArrayList<String> currentFile = new ArrayList<String>();
            in = new BufferedReader(new FileReader(FILENAME_DATABASE));
            String nextLine = "";
            // Read to the end of the file
            while ((nextLine = in.readLine()) != null) {
                // Category header
                if (nextLine.matches("\\{\\-\\-\\w+\\-\\-\\}")){
                    // Store all read lines and reset currentFile
                    if (currentIndex >= 0){
                        database[currentIndex] = new String[currentFile.size()];
                        database[currentIndex] = currentFile.toArray(database[currentIndex]);
                        currentFile.clear();
                    }
                    // Proceed to read next file
                    currentIndex++;
                // Pseudo-line-break; add a blank element to currentFile
                } else if (nextLine.equals("\\n")){
                    currentFile.add("");
                // Standard lines (ignoring blank lines)
                } else if (!nextLine.equals("")){
                    currentFile.add(nextLine);
                }
            }
            // Store the last category read after reaching the end of the file
            database[currentIndex] = new String[currentFile.size()];
            database[currentIndex] = currentFile.toArray(database[currentIndex]);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Gets a file from the "loaded files" array.
     * @param fileIndex File type constant from D2Edit
     * @return
     */
    public String[] getFile(int fileIndex){
        return database[fileIndex];
    }
    
    /**
     * Gets a column from a given data file.
     * Fills "empty" spaces with null.
     * @param fileIndex File type constant from D2Edit
     * @param column Column number
     * @return
     */
    public String[] getListColumn(int fileIndex, int column) {
        String [] list = database[fileIndex];
        String [] returnArray = new String[list.length];
        for (int i = 0; i < returnArray.length; i++){
            String[] item = list[i].split(" : ");
            try {
                returnArray[i] = item[column];
            } catch (ArrayIndexOutOfBoundsException ex){
                returnArray[i] = null;
            }
        }
        return returnArray;
    }
    
    /**
     * Gets an item from a given data file.
     * If the item doesn't exist in the file, this will return null.
     * @param fileIndex File type constant from D2Edit
     * @param column Column number
     * @param row Row number
     * @return
     */
    public String getListItem(int fileIndex, int column, int row){
        return getListColumn(fileIndex, column)[row];
    }
    
}
