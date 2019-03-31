package panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import editor.ItemProperty;
import editor.UniqueEditor;

public class PropertiesPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private UniqueEditor editor;
	private JList<ItemProperty> list;

	private boolean insertAtStart;

	/**
	 * Creates a PropertiesPanel
	 * @param editor
	 */
	public PropertiesPanel(UniqueEditor editor) {
		this.editor = editor;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(500, 255));
		
		// Create components
		
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(
                loweredEtched, " Properties ");
		title.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		this.setBorder(title);
		
		list = new JList<ItemProperty>();
		list.setModel(new DefaultListModel<ItemProperty>());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(12);
		JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroller.setPreferredSize(new Dimension(480, 205));
		
		JPanel buttonPanel = new JPanel();
		
			JButton addProperty = new JButton("Add Property");
			addProperty.addActionListener(this);
			JButton editProperty = new JButton("Edit Property");
			editProperty.addActionListener(this);
			JButton removeProperty = new JButton("Remove Property");
			removeProperty.addActionListener(this);
			JButton moveUp = new JButton("/\\");
			moveUp.addActionListener(this);
			JButton moveDown = new JButton("\\/");
			moveDown.addActionListener(this);
			
			buttonPanel.add(addProperty);
			buttonPanel.add(editProperty);
			buttonPanel.add(removeProperty);
			buttonPanel.add(moveUp);
			buttonPanel.add(moveDown);
		
		// Add components
		add(listScroller);
		add(buttonPanel);
		
	}

	/**
	 * Reacts to a button press
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add Property")){
			// Ensure we haven't reached the limit
			DefaultListModel<ItemProperty> listModel = (DefaultListModel<ItemProperty>) list.getModel();
			if (listModel.getSize() < list.getVisibleRowCount()){
				createPropertyWindow(null);
			}
		} else if (e.getActionCommand().equals("Edit Property")){
			editProperty();
		} else if (e.getActionCommand().equals("Remove Property")){
			removeProperty();
		} else if (e.getActionCommand().equals("/\\")){
			moveUp();
		} else if (e.getActionCommand().equals("\\/")){
			moveDown();
		}
	}
	
	/**
	 * Creates a frame where a property can be edited
	 * @param property Property to edit (null to create one from scratch)
	 */
	private void createPropertyWindow(ItemProperty property) {
        final JDialog dialog = new JDialog(editor.getFrame(), "Edit Property", true);
        final EditPropertiesPanel propertyPanel = new EditPropertiesPanel(editor, dialog, property);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                propertyPanel.dialogClosed();
            }
        });
        dialog.setContentPane(propertyPanel);
        dialog.setResizable(false);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.pack();
        dialog.setLocationRelativeTo(editor.getFrame());
        // Shift up a little (as dropdown box extends downwards)
        dialog.setLocation(dialog.getLocation().x, dialog.getLocation().y - 200);
        dialog.setVisible(true);
	}

    /**
	 * Adds a property to the list
	 * @param property
	 */
	public void addProperty(ItemProperty property){
		if (property == null) return;
		DefaultListModel<ItemProperty> listModel = (DefaultListModel<ItemProperty>) list.getModel();
		// Make sure it's inserted in the right place!
		int index;
		if (insertAtStart){
			index = 0;
			insertAtStart = false;
		} else {
			index = list.getSelectedIndex();
		    if (index == -1) {
		        index = 0;
		    } else {
		        index++;
		    }
		}
	    listModel.insertElementAt(property, index);
	    //Select the new item and make it visible.
	    list.setSelectedIndex(index);
	    list.ensureIndexIsVisible(index);
	}

	/**
	 * Removes the currently-selected property from the list
	 */
	private void removeProperty() {
		DefaultListModel<ItemProperty> listModel = (DefaultListModel<ItemProperty>) list.getModel();
		int index = list.getSelectedIndex();
		if (index >= 0){
			listModel.remove(index);
	        if (index == listModel.getSize()) {
	            //removed item in last position
	            index--;
	        }
	        list.setSelectedIndex(index);
	        list.ensureIndexIsVisible(index);
		}
	}

	/**
	 * Edits the currently-selected property
	 * (Technically removes the property and opens it in the "Edit Property" window which re-adds it)
	 */
	private void editProperty() {
		ItemProperty property = list.getSelectedValue();
		if (property == null) return;
		int index = list.getSelectedIndex();
		removeProperty();
		// Work out where element should be re-inserted after editing
		if (index == 0){
			insertAtStart = true;
		} else {
			index--;
		}
		list.setSelectedIndex(index);
		createPropertyWindow(property);
	}

	/**
	 * Moves the selected property up
	 */
	private void moveUp() {
		int index = list.getSelectedIndex();
		int destination = index - 1;
		if (destination >= 0){
			swap(index, destination);
			list.setSelectedIndex(destination);
		}
	}
	
	/**
	 * Moves the selected property down
	 */
	private void moveDown() {
		int index = list.getSelectedIndex();
		int destination = index + 1;
		if (destination < getNumProperties()){
			swap(index, destination);
			list.setSelectedIndex(destination);
		}
	}

	/**
	 * Swaps 2 elements in the properties list
	 * @param i
	 * @param j
	 */
	private void swap(int i, int j) {
		DefaultListModel<ItemProperty> listModel = (DefaultListModel<ItemProperty>) list.getModel();
		ItemProperty temp = listModel.getElementAt(i);
		listModel.setElementAt(listModel.getElementAt(j), i);
		listModel.setElementAt(temp, j);
	}

	/**
	 * Getter for number of properties that have been added
	 * @return
	 */
	public int getNumProperties() {
		DefaultListModel<ItemProperty> listModel = (DefaultListModel<ItemProperty>) list.getModel();
		return listModel.getSize();
	}

	/**
	 * Getter for the code String of a given property
	 * @param i
	 * @return
	 */
	public String getPropertyString(int i) {
		if (i < getNumProperties()){
			DefaultListModel<ItemProperty> listModel = (DefaultListModel<ItemProperty>) list.getModel();
			ItemProperty property = listModel.getElementAt(i);
			return property.getParamCode() + '\t' + property.getParamZ() + '\t' + property.getParamX() + '\t' + property.getParamY();
		} else {
			return "" + '\t' + "" + '\t' + "" + '\t' + "";
		}
	}

	/**
	 * Removes all properties
	 */
	public void clear() {
		while (list.getSelectedIndex() >= 0){
			removeProperty();
		}	
	}

}
