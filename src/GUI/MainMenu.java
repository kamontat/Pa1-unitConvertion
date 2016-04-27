package GUI;

import Code.*;
import com.sun.java.swing.action.ExitAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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

	// current unit
	private Unit[] units;
	// special variable
	private double amount;
	// last textField that user press on
	private int switchField;

	// constructor
	public MainMenu() {
		UnitConverter uc = new UnitConverter();
		setContentPane(panel1);

		createMenuBar();

		// make convert unable in first run program
		convertButton.setEnabled(false);
		// set default unit is Length
		addUnit("length");

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

				Unit from = units[comboBox1.getSelectedIndex()];
				Unit to = units[comboBox2.getSelectedIndex()];

				if (switchField == TEXTFIELD1) {
					textField2.setText(String.format("%.5g", uc.convert(amount, from, to)));
				} else {
					textField1.setText(String.format("%.5g", uc.convert(amount, to, from)));
				}
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
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
			if (field.getText().equals(".")) return .0;

			return Float.parseFloat(field.getText());
		} else {
			convertButton.setEnabled(false);
		}
		return 0;
	}

	/**
	 * add <code>unit</code> Unit in all comboBox
	 *
	 * @param unit
	 * 		which unit that want to add
	 */
	private void addUnit(String unit) {
		comboBox1.removeAllItems();
		comboBox2.removeAllItems();

		if (unit.equalsIgnoreCase("length")) {
			units = LengthUnit.values();
		} else if (unit.equalsIgnoreCase("weight")) {
			units = WeightUnit.values();
		} else if (unit.equalsIgnoreCase("pressure")) {
			units = PressureUnit.values();
		}

		for (Unit aUnit : units) {
			comboBox1.addItem(aUnit.getName());
			comboBox2.addItem(aUnit.getName());
		}
	}

	/**
	 * if item be choose, program will change coombo box to that unit
	 *
	 * @param item
	 * 		check item be choice or not
	 */
	private void chooseUnit(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addUnit(item.getText());
				clear();
			}
		});
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
			// check dot must have only one dot
			if (aChar == '.' && !checkDot) checkDot = true;
			else if (aChar == '.') return false;

			for (Character exceptChar : except) {
				if (aChar == exceptChar) {
					checkNumber = true;
				}
			}
			// break loop even input have only one alphabet
			if (!checkNumber) return false;
		}
		return true;
	}

	/**
	 * clear all of textField and set comboBox to the first element
	 */
	private void clear() {
		textField1.setText("");
		textField2.setText("");
		comboBox1.setSelectedIndex(0);
		comboBox2.setSelectedIndex(0);
	}

	/**
	 * create new menu bar with unit item inside it.
	 */
	private void createMenuBar() {
		JMenuBar menu = new JMenuBar();
		JMenu unit = new JMenu("Unit");

		// add unit choice
		JMenuItem length = new JMenuItem("Length");
		JMenuItem weight = new JMenuItem("Weight");
		JMenuItem pressure = new JMenuItem("Pressure");

		// exit action
		ExitAction exit = new ExitAction();
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// if user choose some unit
		chooseUnit(length);
		chooseUnit(weight);
		chooseUnit(pressure);

		unit.add(length);
		unit.add(weight);
		unit.add(pressure);
		/* exit action */
		unit.add(exit);

		menu.add(unit);
		setJMenuBar(menu);
	}

	/**
	 * to run this GUI
	 */
	public void run() {
		pack();
		setSize(750, 100);
		setVisible(true);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		MainMenu dialog = new MainMenu();
		dialog.run();

	}
}
