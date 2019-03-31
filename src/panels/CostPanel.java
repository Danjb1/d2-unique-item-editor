package panels;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CostPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField textCostBaseMult, textCostMod;

	/**
	 * Creates a CostPanel
	 */
	public CostPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Create components
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(
                loweredEtched, " Cost ");
		title.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		this.setBorder(title);
		
		JPanel panelCost = new JPanel();
		panelCost.setPreferredSize(new Dimension(240, 35));
		
			JLabel labelBaseCostTimes = new JLabel("( Base Cost x ");
			JLabel labelPlus = new JLabel(") +");
			
			textCostBaseMult = new JTextField(5);
			textCostBaseMult.setText("5");
			textCostMod = new JTextField(5);
			textCostMod.setText("5000");

			panelCost.add(labelBaseCostTimes);
			panelCost.add(textCostBaseMult);
			panelCost.add(labelPlus);
			panelCost.add(textCostMod);
			
		// Add components
		add(panelCost);
	}

	/**
	 * Getter for cost multiplier
	 * @return
	 */
	public String getCostMult() {
		return textCostBaseMult.getText();
	}

	/**
	 * Getter for amount added to cost
	 * @return
	 */
	public String getCostAdd() {
		return textCostMod.getText();
	}

	/**
	 * Setter for cost multiplier
	 * @param mult
	 */
	public void setCostMult(String mult) {
		textCostBaseMult.setText(mult);
	}

	/**
	 * Setter for amount added to cost
	 * @param add
	 */
	public void setCostAdd(String add) {
		textCostMod.setText(add);
	}
	
}
