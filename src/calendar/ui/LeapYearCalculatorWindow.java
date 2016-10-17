package calendar.ui;

import javax.swing.*;

import com.kndesign.common.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeapYearCalculatorWindow extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeapYearCalculatorWindow(String titel){
		super(titel);
		setResizable(false);
		setSize(350, 150);
		setLayout(new FlowLayout());
		setVisible(true);
		final JButton buttonGET_LEAPYEARS = new JButton("Schaltjahre berechnen");
		final JTextField fromYearTF = new JTextField();
		final JTextField toYearTF = new JTextField();
		
		final JComboBox<String> leapYearCalculationTA = new JComboBox<String>();
		
		final JLabel fromHeader = new JLabel("Von Jahr: ");
		final JLabel toHeader = new JLabel("Bis Jahr: ");
		final JLabel amountOfYears = new JLabel();
		final JLabel comboboxHeader = new JLabel("Schaltjahre");
		//Minimum Size dimension for the date Textfields
		Dimension tf_dimension = new Dimension(50,20);
		
		
		fromYearTF.setPreferredSize(tf_dimension);
		toYearTF.setPreferredSize(tf_dimension);

		buttonGET_LEAPYEARS.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					amountOfYears.setText("");
					leapYearCalculationTA.removeAllItems();
					if(toYearTF.getText()!=""||fromYearTF.getText() != ""){
						int from = Integer.parseInt(fromYearTF.getText());
						int to = Integer.parseInt(toYearTF.getText());
						int k = 0; //shows the amount of leap years
						
						DateHandler date = new DateHandler();
						
						int[] years = date.getLeapYearsFromTo(from, to);
						System.out.println("Years:  "+years.length);
						String[] str = new String[years.length];
						for (int i = 0;i<years.length; i++) {
							
							str[i] = Integer.toString(years[i]);
							leapYearCalculationTA.addItem(str[i]);
							k = i+1;
						}
						amountOfYears.setForeground(Color.BLUE);
						amountOfYears.setText("Von "+from+" bis "+to+" gibt es "+k+" Schaltjahre.");
						add(amountOfYears);
					}
				}
				
		});
		add(fromHeader);
		add(fromYearTF);
		add(toHeader);
		add(toYearTF);
		add(buttonGET_LEAPYEARS);
		add(comboboxHeader);
		add(leapYearCalculationTA);
	}//Constructors end
}

