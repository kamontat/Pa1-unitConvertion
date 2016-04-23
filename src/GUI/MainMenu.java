package GUI;

import Code.LengthUnit;
import Code.UnitConverter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by kamontat on 21/4/59.
 */
public class MainMenu extends JFrame {
	// constant variable
	private final int TEXTFIELD1 = 1;
	private final int TEXTFIELD2 = 2;
	// UI in Jframe
	private JTextField textField1;
	private JPanel panel1;
	private JComboBox<String> comboBox1;
	private JLabel message;
	private JTextField textField2;
	private JComboBox<String> comboBox2;
	private JButton convertButton;
	private JButton clearButton;
	// special variable
	private double amount;
	private LengthUnit from;
	private LengthUnit to;
	// last textField that user press on
	private int switchField;

	// constructor
	public MainMenu() {
		UnitConverter uc = new UnitConverter();
		setContentPane(panel1);

		// make convert unable in first run program
		convertButton.setEnabled(false);

		addUnit(comboBox1);
		addUnit(comboBox2);

		// run in every alphabet that user insert or remove from text field
		textField1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				changeAmount(TEXTFIELD1);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changeAmount(TEXTFIELD1);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changeAmount(TEXTFIELD1);
			}
		});
		textField2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				changeAmount(TEXTFIELD2);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changeAmount(TEXTFIELD2);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changeAmount(TEXTFIELD2);
			}
		});

		// run in every changing that user change in combo box
		comboBox1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				changeAmount(TEXTFIELD1);
			}
		});
		comboBox2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				changeAmount(TEXTFIELD2);
			}
		});

		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				from = LengthUnit.values()[comboBox1.getSelectedIndex()];
				to = LengthUnit.values()[comboBox2.getSelectedIndex()];

				if (switchField == TEXTFIELD1) {
					textField2.setText(String.format("%.2g", uc.convert(amount, from, to)));
				} else {
					textField1.setText(String.format("%.2g", uc.convert(amount, to, from)));
				}
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				textField1.setText("");
				textField2.setText("");
				comboBox1.setSelectedIndex(0);
				comboBox2.setSelectedIndex(0);
			}
		});
	}

	/**
	 * Change amount and Switch field that user input in last time. <br>
	 * <p>
	 * <i>if <b>lock</b> is true, program won't do this method</i>
	 *
	 * @param textField
	 * 		which textField that user input in last time
	 */
	private void changeAmount(int textField) {
		if (textField == TEXTFIELD1) {
			amount = warningIn(textField1);
			switchField = TEXTFIELD1;
		} else if (textField == TEXTFIELD2) {
			amount = warningIn(textField2);
			switchField = TEXTFIELD2;
		}
	}

	/**
	 * warning if user input alphabet that can't be convert to number
	 * if user input number correctly it will make <i>Convert button</i> enabled and return that number; <br>otherwise, <i>Convert button</i> unable and return 0
	 *
	 * @param field
	 * 		check warning in <code>field</code>
	 * @return the number(type: <b>float</b>) in field
	 */
	private double warningIn(JTextField field) {
		// check String must be number.
		if (isAllNumberIn(field.getText())) {
			convertButton.setEnabled(true);

			// case user enter dot in first time
			if (field.getText().equals(".")) return Float.parseFloat(field.getText() + 0);
			return Float.parseFloat(field.getText());
		} else {
			convertButton.setEnabled(false);
		}
		return 0;
	}

	/**
	 * add Length Unit in combo box
	 *
	 * @param comboBox
	 * 		display length unit
	 */
	private void addUnit(JComboBox<String> comboBox) {
		for (int i = 0; i < LengthUnit.values().length; i++) {
			comboBox.addItem(LengthUnit.values()[i].getName());
		}

	}

	/**
	 * check that all input String is number or can parse it to number.
	 *
	 * @param input
	 * 		String want to check
	 * @return true if it all the number; otherwise, return false
	 */
	private Boolean isAllNumberIn(String input) {
		// if input is emply String
		if (input.length() == 0) return false;

		Boolean checkDot = false;

		Character[] except = new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};

		for (int i = 0; i < input.length(); i++) {
			char aChar = input.charAt(i);
			Boolean checkNumber = false;

			if (aChar == '.' && !checkDot) checkDot = true;
			else if (aChar == '.') return false;

			for (Character exceptChar : except) {
				if (aChar == exceptChar) {
					checkNumber = true;
				}
			}

			if (!checkNumber) return false;
		}
		return true;
	}

	/**
	 * to run this GUI
	 */
	public void run() {
		pack();
		setSize(750, 85);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		MainMenu dialog = new MainMenu();
		dialog.run();

	}
}
