package  calendar.ui;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI.CellEditorHandler;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView.TableCell;

import com.kndesign.common.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgeCalcWindow extends JFrame{
	
	public AgeCalcWindow(String titel){
		super(titel);
		setResizable(false);
		setSize(300, 250);
		setLayout(new FlowLayout());
		JButton getAgeButton = new JButton("Alter berechnen");
		final JTextField setDayTF = new JTextField();
		final JTextField setMonthTF = new JTextField();
		final JTextField setYearTF = new JTextField();
		final JTextArea ageCalculationTA = new JTextArea();
		JLabel dayHeader = new JLabel("Tag: ");
		JLabel monthHeader = new JLabel("   Monat: ");
		JLabel yearHeader = new JLabel("   Jahr: ");
		//Minimum Size dimension for the date Textfields
		Dimension dMIN_day_month = new Dimension(20,20);
		Dimension dMIN_year = new Dimension(40,20);
		Dimension dateHeaderSize = new Dimension(300,20);
		
		
		setDayTF.setPreferredSize(dMIN_day_month);
		setMonthTF.setPreferredSize(dMIN_day_month);
		setYearTF.setPreferredSize(dMIN_year);

		getAgeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ageCalculationTA.setText("");
				if(setDayTF.getText()!=null&&setMonthTF.getText()!=null&&setYearTF.getText()!=null){
					if(setDayTF.getText()!="DD"&&setMonthTF.getText()!="MM"&&setYearTF.getText()!="YYYY"){
						int day = Integer.parseInt(setDayTF.getText());
						int month = Integer.parseInt(setMonthTF.getText());
						int year = Integer.parseInt(setYearTF.getText());
						
						DateHandler date = new DateHandler();
						String ageAssumptionText = "Alter in...\n"+
									"Jahren:..........."+date.getAgeInYears(day, month, year)+"\n"+
									"Tagen:............"+date.getAgeInDays(day, month, year)+"\n"+
									"Stunden:.........."+date.getAgeInHours(day, month, year)+"\n"+
									"Minuten:.........."+date.getAgeInMinutes(day, month, year)+"\n"+
									"Sekunden:........."+date.getAgeInSeconds(day, month, year)+"\n"+
									"Millisekunden:...."+date.getAgeInMilliseconds(day, month, year);
						
						ageCalculationTA.setText(ageAssumptionText);
						ageCalculationTA.setForeground(Color.BLACK);
					}else{
						ageCalculationTA.setText("Mindestens eines der erforderlichen Felder ist leer!\nBitte �berpr�fen Sie die Eingabe!");
						ageCalculationTA.setForeground(Color.RED);
					}
				}
				
			}
		});
		add(dayHeader);
		add(setDayTF);
		add(monthHeader);
		add(setMonthTF);
		add(yearHeader);
		add(setYearTF);
		add(getAgeButton);
		add(ageCalculationTA);
	}//Constructors end
}
