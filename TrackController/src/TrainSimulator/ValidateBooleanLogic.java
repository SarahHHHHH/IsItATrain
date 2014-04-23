package TrainSimulator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will maintain the Track and Train information for a Train on a 
 *          given set of blocks, on a track. 
 */



@SuppressWarnings("unused")
/**
 * This class will maintain the Track and Train information for a Train on a 
 * given set of blocks, on a track. 
 * 
 * @author Derrick Ward
 */
public class ValidateBooleanLogic {
	//Fields logic
	private String logic="";
	
	
        /**
        * This Constructor creates a ValidateBooleanLogc Object (PLC Program) 
        * and loads the logic into the this PLC Program.
        * 
        * @param logicToLoad The Logic this PLC Program will follow
        */
	public ValidateBooleanLogic(String logicToLoad){
		this.loadLogic(logicToLoad);
	}
	
	
        /**
        * This method loads the logic this PLC Program will follow
        * 
        * @param logicToLoad The Logic this PLC Program will follow
        */
	private void loadLogic(String logicToLoad){
		this.logic = logicToLoad;
	}
	
	
        /**
        * This method evaluates the logic points along the track and returns a
        * boolean value.
        * 
        * @param logicPoints The existence of a train at different locations
        * along the track
        * @return Whether Logic Points along with Logic Language evaluate to true
        * or false
        */
	public boolean evaluateLogic(ArrayList<String> logicPoints){
		int lpLength = logicPoints.size();
		int logicLength = this.logic.length();
		int index = 0;
		boolean temp, tempExistence;
		
		Stack<String> leftParen = new Stack<String>();
		Stack<String> existOrNot = new Stack<String>();
		Stack<String> operator = new Stack<String>();
		
		//Traverse logic and store it in the stacks
		while (index < logicLength){
			if (this.logic.substring(index, index+1).equals("(")){
				leftParen.push("(");
			}
			else if (this.logic.substring(index, index+1).equals("!")){
				existOrNot.push("!");
			}
			else if (this.logic.substring(index, index+1).equals("1")){
				existOrNot.push("1");
			}
			else if (this.logic.substring(index, index+1).equals("&")){
				operator.push("&");
			}
			else if (this.logic.substring(index, index+1).equals("|")){
				operator.push("&");
			}
			else if (this.logic.substring(index, index+1).equals(")")){
				operator.push(")");
				break;
			}
			index++; //increment index
		}
		
		//Logic is stored!
		
		//Traverse logic points
		//Look at the first point alone to get temp initialize
		leftParen.pop();
		String tempExistOrNotPop = existOrNot.pop();
		temp = (tempExistOrNotPop.equals("|") && logicPoints.get(0).equals("1")) ?
				true : (tempExistOrNotPop.equals("!") && logicPoints.get(0).equals("0")) ?
				true : false;
		operator.pop();

		
		//Lets look at our stacks
		/*
		System.out.println("Left Paren Stack:");
		System.out.println(Arrays.toString(leftParen.toArray()));
		
		System.out.println("existOrNot Stack:");
		System.out.println(Arrays.toString(existOrNot.toArray()));
		
		System.out.println("Operator Stack:");
		System.out.println(Arrays.toString(operator.toArray()));
		*/
		index = 1;
		//Traverse the rest of the logic points
		while (index < (logicPoints.size())){
			//System.out.println("Done");
			leftParen.pop();
			String tempOperPop = operator.pop();
			tempExistOrNotPop = existOrNot.pop();
			
			if (tempOperPop.equals("&")){
				
				//should the current logic point be true or not
				boolean shouldExistOrNot = (tempExistOrNotPop.equals("!")) ? false : true;
				
				//get the logic evaluation of the current logic point based off whether it should exist
				tempExistence = (shouldExistOrNot && logicPoints.get(index).equals("1")) ? 
						true : (!shouldExistOrNot && logicPoints.get(index).equals("0")) ?
						true : false;
				
				temp = (temp && tempExistence);
			}
			else if (tempOperPop.equals("|")){
				
				//should the current logic point be true or not
				boolean shouldExistOrNot = (tempExistOrNotPop.equals("!")) ? false : true;
				
				//get the logic evaluation of the current logic point based off whether it should exist
				tempExistence = (shouldExistOrNot && logicPoints.get(index).equals("1")) ? 
						true : (!shouldExistOrNot && logicPoints.get(index).equals("0")) ?
						true : false;
				
				temp = (temp || tempExistence);
			}
			index++;
		}
		
		//Return the logic evaluation
		return temp;
	}
}