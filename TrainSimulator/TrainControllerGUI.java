package TrainSimulator;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;

public class TrainControllerGUI 
{
	private JFrame trainFrame;
	private JPanel trainPanel;
	private JButton trainBrake, emergencyBrake, turnLight, turnDoor, setSpeed, setAuthority, setSpeedLimit,setTemp, setAnnoucement;
	private JTextField currentSpeed, currentSpeedLimit, currentAcceleration, currentLight, currentDoor, currentTemp,currentID, currentAuthority,currentTrackSpeedLimit;
	private JTextField setSpeedField, setAuthorityField, setSpeedLimitField, setTempField, setAnnoucementField;
	private JLabel currentIDLabel, currentSpeedLabel, currentSpeedLimitLabel, currentAccelerationLabel, currentLightLabel, currentDoorLabel, currentTempLabel, currentAuthorityLabel,
	currentTrackSpeedLimitLabel, setSpeedLabel, setAuthorityLabel, setSpeedLimitLabel, errorReportLabel, setTempLabel, setAnnoucementLabel;
	public JTextArea errorReport, annoucement;
	public int trainID=1;
	private int doorStatus=0; 
	private int lightStatus=0;
	public double currentSpeedValue, currentSpeedLimitValue, currentAccelerationValue, currentTempValue, currentAuthorityValue;
	public String currentDoorStats="Closed";
	public String currentLightStats="On";
	public String annoucementString;
	public double setSpeedNumber, setSpeedLimitNumber, setAccelerationNumber, setTempNumber;
	public int setAuthorityNumber,setAuthorityChange;
	int setDoorStats, setLightStats;
	int brakeSignal=0; 
	int emergencyBrakeSignal=0;
	String tempStr;
	double tempDouble;
	int tempInteger;
	DecimalFormat df = new DecimalFormat("#.###");
	DecimalFormat authorityFormat = new DecimalFormat("#");

	
	public TrainControllerGUI()
	{
		trainFrame=new JFrame("Prototype GUI");
		trainPanel=new JPanel();
		trainPanel.setLayout(null);
		TrainControllerGUI.ButtonListener bListener = new TrainControllerGUI.ButtonListener();
		setSpeedNumber=0;
		setAuthorityNumber=0;
		setSpeedLimitNumber=70;
		setTempNumber=52;
		setAuthorityChange=1;
		
		trainBrake=new JButton("Brake");
		emergencyBrake=new JButton("Emergency Brake");
		turnLight=new JButton("Switch Light");
		turnDoor=new JButton("Switch Door");
		setSpeed=new JButton("Set Speed");
		setSpeedLimit=new JButton("Set Speed Limit");
		setAuthority=new JButton("Set Authority");
		setTemp=new JButton("Set Temperature");
		setAnnoucement=new JButton("Set Annoucement");
		trainPanel.add(trainBrake);
		trainPanel.add(emergencyBrake);
		trainPanel.add(turnLight);
		trainPanel.add(turnDoor);
		trainPanel.add(setSpeed);
		trainPanel.add(setSpeedLimit);
		trainPanel.add(setAuthority);
		trainPanel.add(setTemp);
		trainPanel.add(setAnnoucement);
		
		trainBrake.addActionListener(bListener);
		emergencyBrake.addActionListener(bListener);
		turnLight.addActionListener(bListener);
		turnDoor.addActionListener(bListener);
		setSpeed.addActionListener(bListener);
		setSpeedLimit.addActionListener(bListener);
		setAuthority.addActionListener(bListener);
		setTemp.addActionListener(bListener);
		setAnnoucement.addActionListener(bListener);
		
		currentIDLabel=new JLabel("Train ID");
		currentSpeedLabel=new JLabel("Current Speed");
		currentSpeedLimitLabel=new JLabel("Current Speed Limit");
		currentAccelerationLabel=new JLabel("Current Acceleration");
		currentLightLabel=new JLabel("Current Light Stat");
		currentDoorLabel=new JLabel("Current Door Stat");
		currentTempLabel=new JLabel("Current Temperature");
		currentAuthorityLabel=new JLabel("Current Authority");
		setSpeedLabel=new JLabel("Set Speed");
		setAuthorityLabel=new JLabel("Set Authority");
		setSpeedLimitLabel=new JLabel("Set Speed Limit");
		setTempLabel=new JLabel("Set Temperature");
		setAnnoucementLabel=new JLabel("Annoucement");
		errorReportLabel=new JLabel("Error Report");
		currentTrackSpeedLimitLabel=new JLabel("Track Speed Limit");
		currentID=new JTextField(trainID);
		currentSpeed=new JTextField();
		currentSpeedLimit=new JTextField();
		currentAcceleration=new JTextField();
		currentLight=new JTextField();
		currentDoor=new JTextField();
		currentTemp=new JTextField();
		currentAuthority=new JTextField();
		currentTrackSpeedLimit=new JTextField();
		setSpeedField=new JTextField("0");
		setAuthorityField=new JTextField("0");
		setSpeedLimitField=new JTextField("70");
		setTempField=new JTextField("52");
		setAnnoucementField=new JTextField();
		errorReport=new JTextArea();
		annoucement=new JTextArea();
		trainPanel.add(setAnnoucementField);
		trainPanel.add(currentIDLabel);
		trainPanel.add(currentSpeedLabel);
		trainPanel.add(currentSpeedLimitLabel);
		trainPanel.add(currentAccelerationLabel);
		trainPanel.add(currentLightLabel);
		trainPanel.add(currentDoorLabel);
		trainPanel.add(currentTempLabel);
		trainPanel.add(currentAuthorityLabel);
		trainPanel.add(setSpeedLabel);
		trainPanel.add(setAuthorityLabel);
		trainPanel.add(setSpeedLimitLabel);
		trainPanel.add(setTempLabel);
		trainPanel.add(currentID);
		trainPanel.add(currentSpeed);
		trainPanel.add(currentSpeedLimit);
		trainPanel.add(currentAcceleration);
		trainPanel.add(currentLight);
		trainPanel.add(currentDoor);
		trainPanel.add(currentTemp);
		trainPanel.add(currentAuthority);
		trainPanel.add(setSpeedField);		
		trainPanel.add(setAuthorityField);
		trainPanel.add(setSpeedLimitField);
		trainPanel.add(errorReport);
		trainPanel.add(errorReportLabel);
		trainPanel.add(setTempField);
		trainPanel.add(annoucement);
		trainPanel.add(setAnnoucementLabel);
		trainPanel.add(currentTrackSpeedLimitLabel);
		trainPanel.add(currentTrackSpeedLimit);
		
		currentIDLabel.setBounds(100,440,200,20);
		currentID.setBounds(100,470,200,20);
		currentSpeedLabel.setBounds(100,20, 200, 20);
		currentSpeed.setBounds(100, 50, 200, 20);
		currentSpeedLimitLabel.setBounds(100, 80, 200, 20);
        currentSpeedLimit.setBounds(100, 110, 200, 20);
        currentAccelerationLabel.setBounds(100, 140, 200, 20);
        currentAcceleration.setBounds(100, 170, 200, 20);
        currentLightLabel.setBounds(100, 210, 200, 20);
        currentLight.setBounds(100, 230, 200, 20);
        currentDoorLabel.setBounds(100, 260, 200, 20);
        currentDoor.setBounds(100, 290, 200, 20);
        currentTempLabel.setBounds(100, 320, 200, 20);
        currentTemp.setBounds(100, 350, 200, 20);
        currentAuthorityLabel.setBounds(100, 380, 200, 20);
        currentAuthority.setBounds(100, 410, 200, 20);
        currentTrackSpeedLimitLabel.setBounds(100,500,200,20);
        currentTrackSpeedLimit.setBounds(100,530,200,20);
        setSpeedLabel.setBounds(380, 20, 100, 20);
        setSpeedField.setBounds(480, 20, 150, 20);
        setSpeed.setBounds(650, 20, 150, 20);
        setAuthorityLabel.setBounds(380, 50, 100, 20);
        setAuthorityField.setBounds(480, 50, 150, 20);
        setAuthority.setBounds(650, 50, 150, 20);    
        setSpeedLimitLabel.setBounds(380, 80, 100, 20);
        setSpeedLimitField.setBounds(480, 80, 150, 20);
        setSpeedLimit.setBounds(650, 80, 150, 20);
        setTempLabel.setBounds(380, 110, 100, 20);
        setTempField.setBounds(480, 110, 150, 20);
        setTemp.setBounds(650, 110, 150, 20);
        setAnnoucementLabel.setBounds(380,140,100,20);
        setAnnoucementField.setBounds(480,140,150,20);
        setAnnoucement.setBounds(650,140,150,20);
        trainBrake.setBounds(480, 170, 150, 20);
		emergencyBrake.setBounds(480, 200, 150, 20);
		turnLight.setBounds(480, 230, 150, 20);
		turnDoor.setBounds(480, 260, 150, 20);
		errorReport.setBounds(480,320,350,400);
		errorReportLabel.setBounds(480,320,150,20);
		annoucement.setBounds(100,560,200,300);
		
        trainFrame.add(trainPanel);
        
        trainFrame.setVisible(true);
        trainFrame.setDefaultCloseOperation(2);
        trainFrame.setSize(900, 900);
	}
	
	void updateDisplay()
	{
		currentSpeed.setText(df.format(currentSpeedValue));
		currentSpeedLimit.setText(Double.toString(currentSpeedLimitValue));
		currentAcceleration.setText(df.format(currentAccelerationValue));
		currentLight.setText(currentLightStats);
		currentDoor.setText(currentDoorStats);
		currentTemp.setText(df.format(currentTempValue));
		currentID.setText(Integer.toString(trainID));
		currentAuthority.setText(authorityFormat.format(currentAuthorityValue));
		
	}
	
	void updateSetDisplay()
	{
		setSpeedField.setText(Double.toString(setSpeedNumber));
		setSpeedLimitField.setText(Double.toString(setSpeedLimitNumber));
		setAuthorityField.setText(Double.toString(setAuthorityNumber));
		setTempField.setText(Double.toString(setTempNumber));	
	}
	
	int getGUILightSwitch()
	{
		return lightStatus;
	}
	
	int getGUIDoorSwitch()
	{
		return doorStatus;
	}
	
	int[] getGUIAuthority()
	{
		int returnValue[]={setAuthorityNumber,setAuthorityChange};
		return returnValue;
	}
	
	double getGUISpeed()
	{
		return setSpeedNumber;
	}
	
	double getGUISpeedLimit()
	{
		return setSpeedLimitNumber;
	}
	
	double getGUITemp()
	{
		return setTempNumber;
	}
	
	int getBrake()
	{
		return brakeSignal;
	}
	
	int getEmergencyBrake()
	{
		return emergencyBrakeSignal;
	}
	
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==setAnnoucement)
			{
				String tempString=setAnnoucementField.getText();
				annoucement.append(tempString+"\n");
			}
			if(e.getSource()==trainBrake)
			{
				brakeSignal=1;
				setSpeedNumber=0;
				
				if(currentSpeedValue<=0)
				{
					brakeSignal=0;
				}
			}
			else if(e.getSource()==emergencyBrake)
			{
				emergencyBrakeSignal=1;
				setSpeedNumber=0;
				if(currentSpeedValue<=0)
				{
					emergencyBrakeSignal=0;
				}
			}
			else if(e.getSource()==turnLight)
			{
				if(currentLightStats=="On")
				{
					lightStatus=0;
				}
				else
				{
					lightStatus=1;
				}
			}
			else if(e.getSource()==turnDoor)
			{
				if(currentDoorStats=="Closed")
				{
					doorStatus=1;
				}
				else
				{
					doorStatus=0;
				}
			}
			else if(e.getSource()==setSpeed)
			{
				emergencyBrakeSignal=0;
				brakeSignal=0;
				tempStr = setSpeedField.getText();
				try
				{
		            tempDouble=Double.valueOf(tempStr);
		            setSpeedNumber=tempDouble;
		        }
				catch(Exception ex)
				{
					errorReport.append("Please enter a valid double\n");
		        }
			}
			else if(e.getSource()==setSpeedLimit)
			{
				tempStr = setSpeedLimitField.getText();
				try
				{
		            tempDouble=Double.valueOf(tempStr);
		            setSpeedLimitNumber=tempDouble;
		        }
				catch(Exception ex)
				{
					errorReport.append("Please enter a valid double\n");
		        }
			}
			else if(e.getSource()==setAuthority)
			{
				tempStr = setAuthorityField.getText();
				try
				{
					tempInteger=Integer.parseInt(tempStr);
		            setAuthorityNumber=tempInteger;
		            setAuthorityChange=1;
		        }
				catch(Exception ex)
				{
					errorReport.append("Please enter a valid Interger\n");
		        }
			}
			else if(e.getSource()==setTemp)
			{
				tempStr = setTempField.getText();
				try
				{
		            tempDouble=Double.valueOf(tempStr);
		            setTempNumber=tempDouble;
		
		        }
				catch(Exception ex)
				{
					errorReport.append("Please enter a valid double\n");
		        }
			}
		}
	}

	
	/*public static void main(String[] args)
	{
	   new TrainControllerGUI();
	}*/	
}

