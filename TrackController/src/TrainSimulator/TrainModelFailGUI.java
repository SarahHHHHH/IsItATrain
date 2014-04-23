package TrainSimulator;
import java.awt.event.*;

import javax.swing.*;

public class TrainModelFailGUI 
{
	private JFrame failFrame;
	private JPanel failPanel;
	private JButton engineFailure, signalFailure, brakeFailure, fix;
	
	public int engineFailValue, signalFailValue, brakeFailValue;
	
	public TrainModelFailGUI()
	{
		failFrame=new JFrame("Train Model Failure GUI");
		failPanel=new JPanel();
		failPanel.setLayout(null);
		TrainModelFailGUI.ButtonListener bListener = new TrainModelFailGUI.ButtonListener();
		engineFailValue=0;
		signalFailValue=0;
		brakeFailValue=0;
		engineFailure=new JButton("Engine Failure");
		signalFailure=new JButton("Signal Failure");
		brakeFailure=new JButton("Brake Failure");
		fix=new JButton("Fix");
		
		failPanel.add(engineFailure);
		failPanel.add(signalFailure);
		failPanel.add(brakeFailure);
		failPanel.add(fix);
		
		engineFailure.addActionListener(bListener);
		signalFailure.addActionListener(bListener);
		brakeFailure.addActionListener(bListener);
		fix.addActionListener(bListener);
		
		engineFailure.setBounds(20,20,100,20);
		signalFailure.setBounds(20,50,100,20);
		brakeFailure.setBounds(20,80,100,20);
		fix.setBounds(20,110,100,20);
		
		failFrame.add(failPanel);
        
		failFrame.setVisible(true);
		failFrame.setDefaultCloseOperation(2);
		failFrame.setSize(150, 200);
		
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==engineFailure)
			{
				engineFailValue=1;
			}
			if(e.getSource()==signalFailure)
			{
				signalFailValue=1;
			}
			if(e.getSource()==brakeFailure)
			{
				brakeFailValue=1;
			}
			if(e.getSource()==fix)
			{
				brakeFailValue=0;
				engineFailValue=0;
				signalFailValue=0;
			}
		}
	}
	
	int[] getFailure()
	{
		int[] fail=new int[3];
		fail[0]=engineFailValue;
		fail[1]=signalFailValue;
		fail[2]=brakeFailValue;
		return fail;
	}
	
	/**
	 *The following function minimilize the current GUI
	 */
	
	void minimalize()
	{
		failFrame.setState (JFrame.ICONIFIED );
	}
	
	/**
	 *The following function normalize the current GUI
	 */
	
	void normalize()
	{
		failFrame.setState(JFrame.NORMAL);
	}

}
