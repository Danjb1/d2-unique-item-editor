package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import editor.Database;
import editor.ItemProperty;
import editor.UniqueEditor;

public class EditPropertiesPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private UniqueEditor editor;
	private Database database;
	private JDialog dialog;
	private JComboBox<String> selProperty;
	private JTextField paramX, paramY, paramZ;
	private ItemProperty initialProperty;

	/**
	 * Creates an EditPropertiesPanel
	 * @param editor
	 * @param dialog Containing JFrame
	 * @param property Property being edited
	 */
	public EditPropertiesPanel(UniqueEditor editor, JDialog dialog, ItemProperty initialProperty) {
		this.editor = editor;
		this.database = editor.getDatabase();
		this.dialog = dialog;
		this.initialProperty = initialProperty;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Create Components
		selProperty = new JComboBox<String>(database.getListColumn(Database.ITEM_PROPERTIES, 0));
		selProperty.setMaximumRowCount(30);

		JPanel paramPanel = new JPanel();
			JLabel labelX = new JLabel("X: ");
			paramX = new JTextField(10);
			JLabel labelY = new JLabel("Y: ");
			paramY = new JTextField(10);
			JLabel labelZ = new JLabel("Z: ");
			paramZ = new JTextField(10);
			JButton buttonSave = new JButton("Save");
			buttonSave.addActionListener(this);
			JButton buttonCancel = new JButton("Cancel");
			buttonCancel.addActionListener(this);

			paramPanel.add(labelX);
			paramPanel.add(paramX);
			paramPanel.add(labelY);
			paramPanel.add(paramY);
			paramPanel.add(labelZ);
			paramPanel.add(paramZ);
			paramPanel.add(buttonSave);
			paramPanel.add(buttonCancel);

		if (initialProperty != null){
			selProperty.setSelectedItem(initialProperty.getDescription());
			paramX.setText(initialProperty.getParamX());
			paramY.setText(initialProperty.getParamY());
			paramZ.setText(initialProperty.getParamZ());
		}

		// Add Components
		add(selProperty);
		add(paramPanel);
	}

	/**
	 * Reacts to button presses
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Save")){
			int index = selProperty.getSelectedIndex();
			String paramCode = database.getListItem(Database.ITEM_PROPERTIES, 1, index);
			if (paramCode != null){
				ItemProperty property = new ItemProperty(
						selProperty.getItemAt(index),
						paramCode,
						paramX.getText(),
						paramY.getText(),
						paramZ.getText());
				editor.getPropertiesPanel().addProperty(property);
			} else {
				return;
			}
		} else if (e.getActionCommand().equals("Cancel")){
			editor.getPropertiesPanel().addProperty(initialProperty);
		}
		dialog.dispose();
	}

	public void dialogClosed() {
		editor.getPropertiesPanel().addProperty(initialProperty);
	}

}
