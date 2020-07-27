package panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import editor.UniqueEditor;

public class ChooseItemPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private UniqueEditor editor;
	private JDialog dialog;
	private JList<String> list;
	private ArrayList<String> items;

	/**
	 * Creates a ChooseItemPanel
	 * @param editor
	 * @param dialog Containing JFrame
	 * @param property Property being edited
	 */
	public ChooseItemPanel(UniqueEditor editor, JDialog dialog, ArrayList<String> items) {
		this.editor = editor;
		this.dialog = dialog;
		this.items = items;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Create Components
		list = new JList<String>();
		list.setModel(new DefaultListModel<String>());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2){
					// Double click on item
					accept();
					close();
				}
			}
		});
		JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroller.setPreferredSize(new Dimension(200, 300));

		// Add items to list
		DefaultListModel<String> listModel = (DefaultListModel<String>) list.getModel();
		Collections.sort(items);
		ArrayList<String> itemsToRemove = new ArrayList<>();
		for (String item : items){
			String[] itemDetails = item.split("\t");
			if (itemDetails[1].isEmpty()){
				// Item has no specified version; this line is probably a comment
				itemsToRemove.add(item);
				continue;
			}
			listModel.addElement(itemDetails[0]);
		}
		items.removeAll(itemsToRemove);

		JPanel buttonPanel = new JPanel();
			JButton buttonSave = new JButton("Accept");
			buttonSave.addActionListener(this);
			JButton buttonCancel = new JButton("Cancel");
			buttonCancel.addActionListener(this);

			buttonPanel.add(buttonSave);
			buttonPanel.add(buttonCancel);

		// Add Components
		add(listScroller);
		add(buttonPanel);
	}

	/**
	 * Reacts to button presses
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Accept")){
			accept();
		}
		close();
	}

	/**
	 * Accepts the currently-selected item
	 */
	private void accept() {
		int index = list.getSelectedIndex();
		if (index >= 0){
			editor.loadItem(items.get(index));
		}
	}

	public void close() {
		dialog.dispose();
	}

}