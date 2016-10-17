package calendar.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import calendar.graphics.Dimensions;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Test objects
	JButton buttonDAYELEMENTTEST;
	// Test objects end
	JButton buttonAGE_CALC;
	JButton buttonCALENDAR;
	JButton buttonLEAPYEAR_CALC;
	JButton buttonCALENDAR_STATUS;
	JButton buttonCREATE_REGEX;
	
	final JLabel headerAVAILABLE;
	final JLabel headerDEVELOPMENT;
	final JLabel headerPLANNED;
	final JLabel headerTESTPHASE;
	
	Border bevelborder;
	PrintWriter writer;
	Dimensions dims;
	/*At the end of the files are all threads defined*/
	// Definitions for the graphical elements
	public Window(String titel) {
		super(titel);
		
		
		setSize(250, 300);
		setResizable(false);
		setVisible(true);
		setLayout(new FlowLayout());

		dims = new Dimensions();
		// test
		buttonLEAPYEAR_CALC = new JButton("Schaltjahre berechnen");
		buttonLEAPYEAR_CALC.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LeapYearCalculatorWindow w = new LeapYearCalculatorWindow("Schaltjahre berechnen");
				
			}
		});
		buttonDAYELEMENTTEST = new JButton("'Tag' Element Test");
		buttonDAYELEMENTTEST.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Tages Element Test");
				
				frame.setPreferredSize(dims.DIM_MAIN_WINDOW_LABEL);
				frame.setVisible(true);
			
				
			}
		});
		// test end
		buttonAGE_CALC = new JButton("Alters Rechner");
		buttonCALENDAR = new JButton("Kalender " + 20 + "%");
		buttonCALENDAR.setBackground(Color.YELLOW);
//		buttonCALENDAR.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Calendar c = new Calendar("Kalender");
//			}
//		});
		buttonLEAPYEAR_CALC.setBackground(Color.GREEN);
		buttonAGE_CALC.setBackground(Color.GREEN);
		buttonCREATE_REGEX = new JButton("Regex Creator");
		// App Status
		buttonCALENDAR_STATUS = new JButton("weitere Infos");
		buttonCALENDAR_STATUS.addActionListener(new ActionListener() {
			String infoText;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String infoText = getInfoText("calendar_logfile.txt");
				
		
				final JTextArea textFileLabel = new JTextArea(infoText);
				JFrame info = new JFrame();
				JButton save = new JButton("Speichern");
				final JTextField newEntry = new JTextField();

				Dimension d2 = new Dimension(470,40);
				Dimension d = new Dimension(470,250);
				newEntry.setPreferredSize(d2);
				newEntry.setBackground(Color.LIGHT_GRAY);
				newEntry.setVisible(true);
				newEntry.setBorder(BorderFactory.createTitledBorder(bevelborder,"Neuen Eintrag erstellen"));
				info.setSize(500, 400);
				textFileLabel.setPreferredSize(d);
				info.add(newEntry);
				info.setAlwaysOnTop(false);
				info.add(textFileLabel);
				textFileLabel.setVisible(true);
				textFileLabel.setEditable(false);
				info.setLayout(new FlowLayout());
				info.setVisible(true);
				save.setVisible(true);
				save.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							writer = new PrintWriter(new BufferedWriter(new FileWriter("calendar_logfile.txt")),true);
							String oldText = textFileLabel.getText();
							
							textFileLabel.setText("");
							
							textFileLabel.setText(oldText+"\n"+newEntry.getText());
							writer.print(textFileLabel.getText());
							writer.write(textFileLabel.getText());
							writer.flush();
							writer.close();
//							save.setText("Gespeichert");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				info.add(save);
			}
		});
		//
		headerAVAILABLE = new JLabel("Verf�gbare Anwendungen");
		headerDEVELOPMENT = new JLabel("Anwendungen im Entwicklungsstatus");
		headerPLANNED = new JLabel("Geplante Anwendungen");
		headerTESTPHASE = new JLabel("Test Implementation");
		headerAVAILABLE.setSize(250, 20);
		headerDEVELOPMENT.setSize(250, 20);
		headerPLANNED.setSize(250, 20);
		headerTESTPHASE.setPreferredSize(dims.DIM_NEW_APPOINTMENT_HEADER_SIZE);
//		headerTESTPHASE.setSize(getMaximumSize().width,20);
//		headerTESTPHASE.setSize(250, 20);
		/*
		 * TODO Writing a separate JLabel class to save and define the settings
		 * directly
		 */
		JLabel noEntry = new JLabel("-- noch kein Eintrag --");
		noEntry.setForeground(Color.RED);
		JLabel noEntry2 = new JLabel("-- noch kein Eintrag --");
		noEntry2.setForeground(Color.RED);
		JLabel noEntry3 = new JLabel("-- noch kein Eintrag --");
		noEntry3.setForeground(Color.RED);
		JLabel noEntry4 = new JLabel("-- noch kein Eintrag --");
		noEntry4.setForeground(Color.RED);

		buttonAGE_CALC.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				AgeCalcWindow acw = new AgeCalcWindow("Alters Rechner");
				acw.setVisible(true);
			}
		});
		// Available
		add(headerAVAILABLE);
		// add(noEntry);//comment if an entry is there
		add(buttonAGE_CALC);
		add(buttonLEAPYEAR_CALC);
		// in development status
		add(headerDEVELOPMENT);
		add(buttonCALENDAR);
		add(buttonCALENDAR_STATUS);
		// add(noEntry2);//comment if an entry is there
		// planned status
		add(headerPLANNED);
		add(buttonCREATE_REGEX);
//		add(noEntry3);// comment if an entry is there
		// in test status
		add(headerTESTPHASE);
		// add(noEntry4);
		add(buttonDAYELEMENTTEST);
		//print absolute Project Path
		//setVisible(false);
		setVisible(true);
	}
	private String getInfoText(String aboutElementName) {
		String defaultString = "Keine Daten vorhanden";
		String contentString = "";
		boolean fileWasChecked =false;
		File textfile = new File("src/"+aboutElementName);
		
		if (!fileWasChecked) {
			try {
				FileReader reader = new FileReader(textfile);
				StringBuilder text = new StringBuilder();
				
				int read; // readline check
				boolean isEndOfFile = false;
				 
				
				while (!isEndOfFile) {
					read = reader.read();
					if (read == -1) {
						isEndOfFile = true;
						contentString = text.toString();
						
					} else {
					
						text.append((char) read);
					}

				}
				reader.close();
				fileWasChecked  = true;
				return contentString;
			} catch (IOException e) {
				// Prints the line number of the error line
				System.err.println(e);
				return defaultString;
			}
		}else{
			return defaultString;
		}
		
	}


}

