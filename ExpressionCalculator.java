//Palmer Brown, Benjamin Tang, Shirley Mendez
//ECE 309
//Homework Lab 11
//November 5, 2016
//palmer test comment

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame; //
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ExpressionCalculator implements ActionListener, KeyListener {

	private int output;
	private static int errorCounter;
	private static int calculateCounter;
	// window
	private JFrame calculatorWindow = new JFrame("PBS Calculator");
	private String newLine = System.lineSeparator();
	// text fields for input and output of data
	private JTextField expressionInput = new JTextField(200);
	private static JTextField variableInput = new JTextField(20);
	private JTextArea outputArea = new JTextArea("Enter Expression");
	private static JTextArea errorOutput = new JTextArea("Calculator's Message: ");

	// labels
	private JLabel variableLabel = new JLabel("x = ");

	// buttons
	private JButton clearButton = new JButton("Clear");

	// panels
	private JScrollPane outScrollPane = new JScrollPane(outputArea);
	private JPanel northPanel 		  = new JPanel();
	private JPanel southPanel 		  = new JPanel();
	private JPanel centerPanel 		  = new JPanel();

	private static int countOfOperators = 0;
	private static int countOfAddition = 0;
	private static int countOfSubtraction = 0;
	private static int countOfDivision=0;
	private static int countOfMultiplication=0;
	private static int countOfExponentatial=0;
	private static int countOfR=0;
	public ExpressionCalculator() {
		// setting panels to respective areas
		northPanel.setLayout(new GridLayout(1,1));
		northPanel.add(errorOutput);
		errorOutput.setBackground(Color.blue);
		errorOutput.setForeground(Color.white);
		errorOutput.setFont(new Font("default", Font.BOLD, 18));
		errorOutput.setBorder(new EmptyBorder(10,10,10,10));
		errorOutput.setEditable(false);
		calculatorWindow.getContentPane().add(northPanel, "North");

		centerPanel.setLayout(new GridLayout(1,1));
		centerPanel.add(outScrollPane);
		outputArea.setForeground(Color.white);
		outputArea.setFont(new Font("default", Font.BOLD, 20));
		outputArea.setBorder( new EmptyBorder(0,10,0,0));
		centerPanel.setBackground(Color.black);
		outputArea.setBackground(Color.black);
		calculatorWindow.getContentPane().add(centerPanel, "Center");

		clearButton.setBackground(Color.cyan);
		clearButton.setFont(new Font("default", Font.BOLD, 18));
		clearButton.setBorder(new EmptyBorder(15,10,15,10));
		expressionInput.setBackground(Color.white);
		expressionInput.setBorder(new EmptyBorder(15,10,15,10));
		expressionInput.setFont(new Font("default", Font.BOLD, 18));
		variableLabel.setBackground(Color.lightGray);
		variableLabel.setFont(new Font("default", Font.BOLD, 18));
		variableLabel.setBorder(new EmptyBorder(15,10,15,10));
		variableInput.setBackground(Color.white);
		variableInput.setFont(new Font("default", Font.BOLD, 18));
		variableInput.setBorder(new EmptyBorder(15,10,15,10));

		southPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); 	// Invisible border for center bar
		southPanel.setLayout(new GridBagLayout()); 			// Initialize grid layout for center bar
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; 			// Format horizontal layout for center bar
		c.gridheight = (northPanel.getHeight()); 
		c.weightx = 0.02; 									// Percent width of the clear button
		c.gridx = 0; 										// Clear button grid x position
		c.gridy = 0; 										// Clear button grid y position
		c.gridwidth = 1; 	
		southPanel.add(clearButton,c);
		c.weightx = 0.6;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		southPanel.add(expressionInput,c);
		c.weightx = 0.1;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		southPanel.add(variableLabel, c);
		c.weightx = 0.15;
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		southPanel.add(variableInput,c);
		calculatorWindow.getContentPane().add(southPanel, "South");

		calculatorWindow.setVisible(true);
		calculatorWindow.setSize(600, 700);
		calculatorWindow.setResizable(false);
		clearButton.addActionListener(this);
		expressionInput.addKeyListener(this);

		calculatorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		System.out.println("ECE 309 Expression Calculator");
		System.out.println("Team members: Benjamin Tang, Palmer Brown, Shirley Mendez");
		new ExpressionCalculator();
	}

	public void actionPerformed(ActionEvent ae) { // actions to be performed by GUI elements (like a button)
		//clear button clears all text areas
		if (ae.getSource() == clearButton) {
			expressionInput.setText("");
			variableInput.setText("");
			outputArea.setText("");
			errorOutput.setText("");
			errorOutput.setBackground(Color.blue);
			errorOutput.setText("Calculator's Message: Enter Expression below");
		}
	}

	public void calculate(String equation) throws IllegalArgumentException { // main calculation method
		//String output = simpleExpression(equation);
		String expression = expressionInput.getText();
		String variable = variableInput.getText();
		double doubleVariable = 0;
		String finalValue;
		String expression_new = null;
		String newerExpression = null; 

		System.out.println("Expression coming in: " + expression);
		//replacing expression if it contains an equal sign
		if(expression.contains("=")){
			newerExpression = expression.substring(0, expression.indexOf('=') );
		}
		//if it doesn't contain an equal sign then it will just update expression onto new string
		else if(!(expression.contains("="))){
			newerExpression = expression;
		}

		System.out.println("answer before calling: " + newerExpression);

		if(!(newerExpression.contains("x") || newerExpression.contains("X"))){
			//if expression has no x & x is blank -SIMPLE EXPRESSION
			if(variable.length() == 0){
				expression_new = newerExpression;
				errorCounter = 0;
				errorOutput.setBackground(Color.blue);
				errorOutput.setForeground(Color.black);
				errorOutput.setText("Calculator's Message: Solution");
				variableInput.setText("");
				System.out.println("before going in if statements " + expression_new );
				if(expression_new.contains("pi") || expression_new.contains("PI") || expression_new.contains("pI")
						|| expression_new.contains("Pi") || expression_new.contains("e") || expression_new.contains("E")){
					expression_new = substituteValueInExpression(expression_new);
					System.out.println("after sub in: " + expression_new);
				}

				//if parentheses then check for parentheses
				if(newerExpression.contains("(")){
					expression_new = parenthesesErrorCheck(expression_new);
				}//if no parentheses then check for simple expression
				if(!(expression_new.contains("("))){
					expression_new = evaluateComplexExpression(expression_new);
				}

				outputArea.append(newerExpression + " = " + expression_new + newLine);
			}	
			//if expression has no x & x has a value - SIMPLE EXPRESSION
			else if(variable.length() != 0){
				errorCounter = 1;
				errorOutput.setBackground(Color.pink);
				errorOutput.setForeground(Color.black);
				errorOutput.setText("X has a value when it is not necessary");
			}			
		}

		if (newerExpression.contains("x") || newerExpression.contains("X")) {
			// If expression has an x but x is blank
			if(variable.length() == 0){
				try {
					errorOutput.setBackground(Color.pink);
					errorOutput.setForeground(Color.black);
					errorOutput.setText("X value is needed");
					errorCounter = 1;
				}
				catch (Exception e) {
					throw new IllegalArgumentException("Error with converting xvalue to a double value");
				}	
			}
			//if an expression has an x and had a value for x
			else if( variable.length() != 0){
				try {
					doubleVariable = Double.parseDouble(variable);
				}
				catch (Exception e) {
					throw new IllegalArgumentException("Error with converting xvalue to a double value");
				}	
				errorCounter = 0;
				errorOutput.setBackground(Color.blue);
				errorOutput.setForeground(Color.black);
				errorOutput.setText("Calculator's Message: Solution");

				expression = substituteValueInExpression(newerExpression);
				System.out.println("answer for sub in value: " + expression);
				if(newerExpression.contains("(")){
					expression = parenthesesErrorCheck(expression);
					expression = evaluateComplexExpression(expression);
				}
				else if(!(newerExpression.contains("("))){
					expression = evaluateComplexExpression(expression);
				}
				System.out.println("answer for parentheses: " + expression);
				outputArea.append(newerExpression + " = " + expression + " for x = " + variable + newLine);

			}
		}
	}
	public static String simpleExpression(String simpleEquation) throws IllegalArgumentException{ //calculates a simple expression
		double result = 0;
		while(true){
			String expression = simpleEquation.toLowerCase(); //changing what was inputed by the user
			//getting rid of space, read whole line and lower variables to lower case 
			int i = 1;
			char operator = '0';
			for(; i < expression.length(); i++){
				if((expression.charAt(i) == 'r') || (expression.charAt(i) == '/') || (expression.charAt(i) == '^')
						|| (expression.charAt(i) == '+') || (expression.charAt(i) == '-') || (expression.charAt(i) == '*')){
					operator = expression.charAt(i);
					break;
				}
			}
			//System.out.println("i = " + i);
			if(operator == '0'){
				System.out.println("Entered expression did not contain an operator.");				
				break;
			}
			if( i == expression.length()-1){
				System.out.println("Operator cannot be at the end of the expression.");
				break;
			}
			String leftOperand = expression.substring(0, i).trim(); //takes care of trimming everything, both blanks before and after
			String rightOperand = expression.substring(i+1).trim();
			double leftNumber;
			double rightNumber;
			try {
				leftNumber = Double.parseDouble(leftOperand);
				rightNumber = Double.parseDouble(rightOperand);
			}
			catch(NumberFormatException nfe){
				System.out.println("Left or right operand is not numeric.");
				break;
			}
			System.out.println("Left operand is " + leftNumber +  ", operation is " + operator 
					+ ", right operand is " + rightNumber);
			//result = Math. maxValue();
			switch (operator){
			case('*'): result = leftNumber * rightNumber;
			break;
			case('/'): result = leftNumber / rightNumber;
			break;          
			case('+'): result = leftNumber + rightNumber;
			break;          
			case('-'): result = leftNumber - rightNumber;
			break;
			case('^'): result = Math.pow(leftNumber, rightNumber);
			break;
			case('r'): result = Math.pow(leftNumber, 1/rightNumber);
			break;
			default : ;
			}
			System.out.println("Answer is:" + result); //expression to see how it's reading it 
			return String.valueOf(result);
		}
		return String.valueOf(result);
	}
	private static void multiplyErrorCheck(String string) {
		//checks for parentheses multiply errors only
		//other method checks for all space multiply errors
		string = string.replaceAll(" ",  "");
		System.out.println("Orignal: " + string);
		while (string.contains("(")) {
			//System.out.println("Edit loop");
			int index = string.indexOf("(");

			if (index > 0) {
				char before = string.charAt(index-1);
				char after = string.charAt(index+1);
				if (((before == '+') || (before == '-') || (before == '*') || (before == '/') || 
						(before == '^') || (before == 'r')) || ((after == '+') || (after == '*') || 
								(after == '/') || (after == '^') || (after == 'r') || (after == '-'))) {
					string = string.replaceFirst("\\(", "");
					System.out.println("Edit: " + string);
				}
				else {
					System.out.println("error at " + index);
					System.out.println("String is: " + string);
					// System.exit(0);
					//return;
					errorCounter = 1;
					calculateCounter = 1;
					errorOutput.setBackground(Color.pink);
					errorOutput.setForeground(Color.black);
					errorOutput.setText("Parentheses without an operator inbetween");
					throw new IllegalArgumentException("Parentheses without an operator inbetween");
				}
			}//end of if loop
			else if (index == 0) {
				string = string.replaceFirst("\\(",  "");
				System.out.println("Edit: " +string);
			}
		}//end of while loop 


	}//end of method
	public void keyPressed(KeyEvent ke) { // Key press listener, specifically for ENTER key
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
			if(output == 0){
				outputArea.setText("");
				output = 1;
			}
			String expression = expressionInput.getText();
			String newerExpression = null;
			//if the expression contains a "=" sign then we take off starting from = to the farest right and save
			//into a new string
			if(expression.contains("=")){
				newerExpression = expressionInput.getText().substring(0, expressionInput.getText().indexOf('=') );
				calculateCounter = 0;
			}
			//if the string doesn't have a equal sign we save onto the new string with no changes made to it
			else {
				newerExpression = expression;
				calculateCounter = 0;
			}
			//testing for any unknown letters that are not x or X
			if(newerExpression.contains("a")||newerExpression.contains("b")||newerExpression.contains("c")||
					newerExpression.contains("d")||newerExpression.contains("f")||newerExpression.contains("g")||
					newerExpression.contains("h")||newerExpression.contains("i")||
					newerExpression.contains("j")||newerExpression.contains("k")|newerExpression.contains("l")||
					newerExpression.contains("m")||newerExpression.contains("n")||newerExpression.contains("o")||
					newerExpression.contains("p")||newerExpression.contains("q")||
					newerExpression.contains("s")||newerExpression.contains("t")||newerExpression.contains("u")||
					newerExpression.contains("v")||newerExpression.contains("w")||newerExpression.contains("y")||
					newerExpression.contains("z")||newerExpression.contains("A")||newerExpression.contains("B")||
					newerExpression.contains("C")||newerExpression.contains("D")||newerExpression.contains("F")||
					newerExpression.contains("G")||newerExpression.contains("H")||newerExpression.contains("I")||
					newerExpression.contains("J")||newerExpression.contains("K")||newerExpression.contains("L")||
					newerExpression.contains("M")||newerExpression.contains("N")||newerExpression.contains("O")||
					newerExpression.contains("P")||newerExpression.contains("Q")||newerExpression.contains("R")||
					newerExpression.contains("S")||newerExpression.contains("T")||newerExpression.contains("U")||
					newerExpression.contains("V")||newerExpression.contains("W")||newerExpression.contains("Y")||
					newerExpression.contains("Z")){
				if(newerExpression.contains("pi") || newerExpression.contains("PI") || newerExpression.contains("pI")
						|| newerExpression.contains("Pi")){
					calculateCounter = 0;
					errorCounter = 0;
				}
				else{
					System.out.println("catched the error");
					errorOutput.setBackground(Color.pink);
					errorOutput.setForeground(Color.black);
					errorOutput.setText("Unknown variable used that's not x");
					calculateCounter = 1;
					errorCounter = 1;
				}
			}
			//checking newerExpression if it contain a unknown operator
			if(newerExpression.contains("%") || newerExpression.contains("$") || newerExpression.contains("#") 
					|| newerExpression.contains("!") || newerExpression.contains("@") || newerExpression.contains("&")
					|| newerExpression.contains("~") || newerExpression.contains("?") || newerExpression.contains("'")
					|| newerExpression.contains("`") || newerExpression.contains("&&") || newerExpression.contains(",")
					|| newerExpression.contains("[") || newerExpression.contains("]") || newerExpression.contains("{")
					|| newerExpression.contains("|") || newerExpression.contains("}") || newerExpression.contains(":")
					|| newerExpression.contains(";")){
				errorOutput.setBackground(Color.pink);
				errorOutput.setForeground(Color.black);
				errorOutput.setText("Unknown operator");
				errorCounter = 1;
				calculateCounter = 1;
			} 

			int left = newerExpression.length() - newerExpression.replace("(", "").length();
			int right = newerExpression.length() - newerExpression.replace(")", "").length();
			//count number of operators 
			if (left != right) {
				System.out.println("Error: Number of left and right parenthesis's do not match");
				//ADD GUI ERROR HERE, has to be put in calculate or before then so it can catch it
				errorOutput.setText("Uneven number of left and right parenthesis");
				errorOutput.setBackground(Color.pink);
				errorOutput.setForeground(Color.black);	
				errorCounter = 1;
				//	throw new IllegalArgumentException("Parenthesis do not match!");
			}
			if(newerExpression.contains(")") || newerExpression.contains("(")){
				multiplyErrorCheck(newerExpression);
			}
			String variable = variableInput.getText();
			if(variable.length() != 0){
				xCheckForError(variable);
			}
			if(newerExpression.contains("1") || newerExpression.contains("2") || newerExpression.contains("3") ||
					newerExpression.contains("4") || newerExpression.contains("5") || newerExpression.contains("6")
					|| newerExpression.contains("7") || newerExpression.contains("9") || newerExpression.contains("0")){
				checkSpaces(newerExpression);
			}
			if(calculateCounter == 0){
				calculate(newerExpression);
			}
			System.out.println("newereexpression @ keypressed: " + newerExpression);
			if(errorCounter == 0){
				expressionInput.setText("");
				variableInput.setText("");
			}
		}
	}

	public void keyReleased(KeyEvent ke) {/*Required to compile*/};
	public void keyTyped(KeyEvent ke) {

	}

	private static String removeBlanks(String input) throws IllegalArgumentException{

		String inputWithoutAnySpaces = input.replaceAll(" ", "");
		return inputWithoutAnySpaces;
	}

	private static String substituteValueInExpression(String input) throws IllegalArgumentException{

		//remove blanks!
		//input = removeBlanks(input);
		//replace pi value
		if(input.contains("pi") || input.contains("PI") || input.contains("pI") || input.contains("Pi")){
			double pieValue = Math.PI;
			String pieStringValue = String.valueOf(pieValue);
			input = input.replaceAll("pi", pieStringValue);
			input = input.replaceAll("PI", pieStringValue);
			input = input.replaceAll("pI", pieStringValue);
			input = input.replaceAll("Pi", pieStringValue);
		}

		//replace for x 
		if(input.contains("x") || input.contains("X")){
			String xValue = variableInput.getText();
			try {
				Double xDoubleValue = Double.parseDouble(xValue);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("error converting String to a double (for the xValue textbox");
			}

			input = input.replaceAll("x", xValue);
			input = input.replaceAll("X", xValue);
		}
		//replace r and e

		if (input.contains("e") || input.contains("E")) {
			double eValue = Math.E;
			String eStringValue = String.valueOf(eValue);
			input = input.replaceAll("e", eStringValue);
			input = input.replaceAll("E", eStringValue);
		}

		//replace r 

		return input;
	}

	private static String parenthesesErrorCheck(String input) throws IllegalArgumentException
	{
		openParenthesesCheck(input);
		//test for errors in parenthesis
		/*String originalInput = input; 
		int left = input.length() - input.replace("(", "").length();
		int right = input.length() - input.replace(")", "").length();
		System.out.println("Left '(': " + left);
		System.out.println("Right ')': " + right);

		if (left != right) {
			System.out.println("Error: Number of left and right parenthesis's do not match");
			throw new IllegalArgumentException("Parenthesis do not match!");
		}*/
		String originalInput = input; 
		int left = input.length() - input.replace("(", "").length();
		int right = input.length() - input.replace(")", "").length();
		//count number of operators 
		if (left != right) {
			System.out.println("Error: Number of left and right parenthesis's do not match");
			//ADD GUI ERROR HERE, has to be put in calculate or before then so it can catch it
			errorOutput.setText("Uneven number of left and right parenthesis");
			errorOutput.setBackground(Color.pink);
			errorOutput.setForeground(Color.black);	
			errorCounter = 1;
			throw new IllegalArgumentException("Parenthesis do not match!");
		}

		String string = originalInput;
		String substring1 = "(";
		String substring2 = ")";
		String replacement = "";
		int index = string.lastIndexOf(substring1);
		int index2 = string.indexOf(substring2);
		String firstPartString = string.substring(0, index2+1);
		String originalEdit;
		String toBeCalculated;
		double valueReturned = 0;
		String valueReturnedString = null;

		while (string.contains("(")) {

			index = string.lastIndexOf(substring1);
			index2 = string.indexOf(substring2);
			firstPartString = string.substring(0, index2+1);
			originalEdit = firstPartString;

			index = firstPartString.lastIndexOf(substring1);
			index2 = firstPartString.indexOf(substring2);
			//System.out.println(index + "     " + index2);
			if (index >= index2) {
				throw new IllegalArgumentException("Error with the parentheses not in the correct order!");
			}
			else {
				//here I am replacing the parentheses but for our calculator we will need 
				// to check for simple or complex expression here.  T:he index is the index value of left parentheses 
				//and the index2 is the index of the right.  So I am thinking creating a substring here between those indexes
				//check it for operators and then based on operators we call simple or complex methods everytime
				//if (countOfOperators > 1) {
				//evaluateComplexExpression(input);
				//}
				//else {
				//evaluateSimpleExpression(input);
				//}


				firstPartString =  firstPartString.substring(0, index) + replacement + firstPartString.substring(index+substring1.length());

				index2 = firstPartString.indexOf(substring2);

				System.out.println("Part to send to be calculated: " + firstPartString.substring(index, index2));
				toBeCalculated = firstPartString.substring(index,index2);

				countOfAddition = toBeCalculated.length() - toBeCalculated.replace("+","").length();
				countOfSubtraction = toBeCalculated.length() - toBeCalculated.replace("-","").length();
				countOfDivision = toBeCalculated.length() - toBeCalculated.replace("/","").length();
				countOfMultiplication = toBeCalculated.length() - toBeCalculated.replace("*","").length();
				countOfExponentatial = toBeCalculated.length() - toBeCalculated.replace("^","").length();
				countOfR = toBeCalculated.length() - toBeCalculated.replace("r","").length();

				countOfOperators = countOfAddition + countOfSubtraction + countOfDivision + countOfMultiplication + countOfExponentatial + countOfR; 
				if (countOfOperators > 1) {
					//complexExpression
					valueReturnedString = evaluateComplexExpression(toBeCalculated);
				}
				else if(countOfOperators == 1) {
					valueReturnedString = simpleExpression(toBeCalculated);
					System.out.println("Calling simple expression method");
				}				
				firstPartString =  firstPartString.substring(0, index2) + replacement + firstPartString.substring(index2+substring2.length());
				firstPartString = firstPartString.replace(toBeCalculated, valueReturnedString);
				//System.out.println(firstPartString);						
			}
			string = string.replace(originalEdit, firstPartString);
			System.out.println("LOOP FINAL: " + string);
		}

		string = removeBlanks(string);
		//	valueReturnedString = simpleExpression(string);
		//re turn valueReturnedString
		return string;	
	}

	public static String evaluateComplexExpression(String complexEquation) throws IllegalArgumentException{
		//Initializing variables
		String ans = null;
		String currentSimpleExpression = null;
		char leadingNegativeCheck;
		
		//variables for 1st priority operators (^ and r)
		int firstOperator_I = 0;
		int secondOperator_I = 0;
		int operatorFocus_I = 0;
		
		//variables for 2nd priority operators (* and /)
		int firstOperator_J = 0;
		int secondOperator_J = 0;
		int operatorFocus_J = 0;
		
		//variables for 3rd priority operators (+ and -)
		int firstOperator_K = 0;
		int secondOperator_K = 0;
		int operatorFocus_K = 0;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
		//1st priority section:
		leadingNegativeCheck = complexEquation.charAt(0);
		if(complexEquation.contains("\n"))
			complexEquation.replace("\n", "");
		if(leadingNegativeCheck == '-')
			complexEquation = complexEquation.replaceFirst(Pattern.quote("-"),Matcher.quoteReplacement("n"));
		for (int i = 0; i < complexEquation.length(); i++) { //for loop continually finds operators that aren't
			if (complexEquation.charAt(i) == '*' || 		 //the high priority operators, incrementing variable
					complexEquation.charAt(i) == '/' ||		 //'firstOperator_I' - this will be the offset 
					complexEquation.charAt(i) == '+' ||		 //for the left-side of the substring
					complexEquation.charAt(i) == '-')
				firstOperator_I = i;
			if (complexEquation.charAt(i) == '^' || complexEquation.charAt(i) == 'r') {
				operatorFocus_I = i;						 									//when it finds a high priority operator, it saves the
				for (i++; i < complexEquation.length(); i++){									//placement of its position in 'operatorFocus_I';
					if(complexEquation.charAt(i) == '-' &&	 							//if the next operator IS right after the high priority
							i - operatorFocus_I == 1 ||			 						//operator and is negative, or has EXACTLY ONE SPACE between them
							complexEquation.charAt(i) == '-' &&  						//and is a negative, we assume that it is a negative unary operator
							i - operatorFocus_I >= 2 && 								//and continue to loop for the actual second operator
							complexEquation.charAt(i-1) == ' ')			 							
						i++;
					else if(complexEquation.charAt(i) == '*' && i - operatorFocus_I > 1 || 		
					complexEquation.charAt(i) == '/' && i - operatorFocus_I > 1 ||		//when it finds a high priority operator, it saves the
					complexEquation.charAt(i) == '+' && i - operatorFocus_I > 1 ||		//placement of its position in 'operatorFocus_J';
					complexEquation.charAt(i) == '^' && i - operatorFocus_I > 1 ||		//another for loop is put in, continuing from where the
					complexEquation.charAt(i) == 'r' && i - operatorFocus_I > 1 ||		//last for loop was, finding the next operator
					complexEquation.charAt(i) == '-' && i - operatorFocus_I > 1) {		//if the next operator is NOT right after the high priority
					secondOperator_I = i;					 							//operator, then record the placement of the second operator
					break;									 							//(our right-side of the substring) and then break from the loop
				}										 
				else if(complexEquation.charAt(i) == '+' &&
						i - operatorFocus_I == 1)			 //if the next operator IS right after the high priority operator, and it's a positive, throw an error
/*possible error*/	throw new IllegalArgumentException("Cannot have a '+' unary operator!");
				else if(complexEquation.charAt(i) == '*' && i - operatorFocus_I == 1 ||  //if the next operator IS right after the high priority operator
					complexEquation.charAt(i) == '/' && i - operatorFocus_I == 1 ||		 //and is NOT a negative or positive operator, then throw a different error
					complexEquation.charAt(i) == '^' && i - operatorFocus_I == 1 ||
					complexEquation.charAt(i) == 'r' && i - operatorFocus_I == 1)
/*possible error*/	throw new IllegalArgumentException("Operator syntax error: " + complexEquation.charAt(i) + complexEquation.charAt(operatorFocus_I));
				}
				//the next few if statements creates the substrings depending on the values of the firstOperator and secondOperator variables
				if(secondOperator_I == 0 && firstOperator_I != 0) 							//indicates that the substring is at the END of a longer string
					currentSimpleExpression = complexEquation.substring(firstOperator_I+1); 
				else if(secondOperator_I != 0 && firstOperator_I == 0)						//indicates that the substring is at the BEGINNING of a longer string
					currentSimpleExpression = complexEquation.substring(0, secondOperator_I);
				else if(secondOperator_I == 0 && firstOperator_I == 0)						//indicates that a substring isn't needed
						currentSimpleExpression = complexEquation;	
				else																		//line below indicates that substring is in the MIDDLE of a longer string
					currentSimpleExpression = complexEquation.substring(firstOperator_I+1, secondOperator_I);
				if(currentSimpleExpression.charAt(0) == 'n') //just changed, works for all except BEGINNING OF LONGER STRING
					currentSimpleExpression = currentSimpleExpression.replaceFirst(Pattern.quote("n"),Matcher.quoteReplacement("-"));
					firstOperator_I = 0;													//firstOperator and secondOperator reset
					secondOperator_I = 0;	
					operatorFocus_I = 0;
					divideByZero(currentSimpleExpression);
					ans = simpleExpression(currentSimpleExpression);						//solve the simple expression of the substring
					if(currentSimpleExpression.charAt(0) == '-')
						currentSimpleExpression = currentSimpleExpression.replaceFirst(Pattern.quote("-"),Matcher.quoteReplacement("n"));
					complexEquation = complexEquation.replaceFirst(Pattern.quote(currentSimpleExpression),Matcher.quoteReplacement(ans)); //replace the substring with the calculated value
					i = 0;																	//because the string length has changed, we have to go through the loop from the beginning;
				} 
			}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		//2nd priority section:
		leadingNegativeCheck = complexEquation.charAt(0);
		if(leadingNegativeCheck == '-')
			complexEquation = complexEquation.replaceFirst(Pattern.quote("-"),Matcher.quoteReplacement("n"));
		for (int j = 0; j < complexEquation.length(); j++) { //for loop continually finds operators that aren't
			if (complexEquation.charAt(j) == '^' || 		 //the medium priority operators, incrementing variable
					complexEquation.charAt(j) == 'r' ||		 //'firstOperator_J' - this will be the offset 
					complexEquation.charAt(j) == '+' ||		 //for the left-side of the substring
					complexEquation.charAt(j) == '-')
				firstOperator_J = j;
			if (complexEquation.charAt(j) == '*' || complexEquation.charAt(j) == '/') {
				operatorFocus_J = j;						 							
				for (j++; j < complexEquation.length(); j++){							
					if(complexEquation.charAt(j) == '-' &&	 							//if the next operator IS right after the medium priority
							j - operatorFocus_J == 1 ||			 						//operator and is negative, or has EXACTLY ONE SPACE between them
							complexEquation.charAt(j) == '-' &&  						//and is a negative, we assume that it is a negative unary operator
							j - operatorFocus_J >= 2 && 								//and continue to loop for the actual second operator
							complexEquation.charAt(j-1) == ' ')			 							
						j++;
					else if(complexEquation.charAt(j) == '*' && j - operatorFocus_J > 1 || 		
					complexEquation.charAt(j) == '/' && j - operatorFocus_J > 1 ||		//when it finds a medium priority operator, it saves the
					complexEquation.charAt(j) == '+' && j - operatorFocus_J > 1 ||		//placement of its position in 'operatorFocus_J';
					complexEquation.charAt(j) == '^' && j - operatorFocus_J > 1 ||		//another for loop is put in, continuing from where the
					complexEquation.charAt(j) == 'r' && j - operatorFocus_J > 1 ||		//last for loop was, finding the next operator.
					complexEquation.charAt(j) == '-' && j - operatorFocus_J > 1) {		//if the next operator is NOT right after the high priority
					secondOperator_J = j;					 							//operator, then record the placement of the second operator
					break;									 							//(our right-side of the substring) and then break from the loop
				}							 
				else if(complexEquation.charAt(j) == '+' &&
						j - operatorFocus_J == 1)			 //if the next operator IS right after the medium priority operator, and it's a positive, throw an error
/*possible error*/	throw new IllegalArgumentException("Cannot have a '+' unary operator!");
				else if(complexEquation.charAt(j) == '*' && j - operatorFocus_J == 1 ||  //if the next operator IS right after the medium priority operator
					complexEquation.charAt(j) == '/' && j - operatorFocus_J == 1 ||		 //and is NOT a negative or positive operator, then throw a different error
					complexEquation.charAt(j) == '^' && j - operatorFocus_J == 1 ||
					complexEquation.charAt(j) == 'r' && j - operatorFocus_J == 1)
/*possible error*/	throw new IllegalArgumentException("Operator syntax error: " + complexEquation.charAt(j) + complexEquation.charAt(operatorFocus_J));
				}
				//the next few if statements creates the substrings depending on the values of the firstOperator and secondOperator variables
				if(secondOperator_J == 0 && firstOperator_J != 0) 							//indicates that the substring is at the END of a longer string
					currentSimpleExpression = complexEquation.substring(firstOperator_J+1); 
				else if(secondOperator_J != 0 && firstOperator_J == 0)						//indicates that the substring is at the BEGINNING of a longer string
					currentSimpleExpression = complexEquation.substring(0, secondOperator_J);
				else if(secondOperator_J == 0 && firstOperator_J == 0)						//indicates that a substring isn't needed
						currentSimpleExpression = complexEquation;	
				else																		//line below indicates that substring is in the MIDDLE of a longer string
					currentSimpleExpression = complexEquation.substring(firstOperator_J+1, secondOperator_J);
				if(currentSimpleExpression.charAt(0) == 'n') //just changed, works for all except BEGINNING OF LONGER STRING
					currentSimpleExpression = currentSimpleExpression.replaceFirst(Pattern.quote("n"),Matcher.quoteReplacement("-"));
					firstOperator_J = 0;													//firstOperator and secondOperator reset
					secondOperator_J = 0;	
					operatorFocus_J = 0;
					divideByZero(currentSimpleExpression);
					ans = simpleExpression(currentSimpleExpression);						//solve the simple expression of the substring
					if(currentSimpleExpression.charAt(0) == '-')
						currentSimpleExpression = currentSimpleExpression.replaceFirst(Pattern.quote("-"),Matcher.quoteReplacement("n"));
					complexEquation = complexEquation.replaceFirst(Pattern.quote(currentSimpleExpression),Matcher.quoteReplacement(ans)); //replace the substring with the calculated value
					j = 0;																	//because the string length has changed, we have to go through the loop from the beginning
				} 
			}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
		//third priority section:
		leadingNegativeCheck = complexEquation.charAt(0);
		if(leadingNegativeCheck == '-')
			complexEquation = complexEquation.replaceFirst(Pattern.quote("-"),Matcher.quoteReplacement("n"));
		for (int k = 0; k < complexEquation.length(); k++) { //for loop continually finds operators that aren't
			if (complexEquation.charAt(k) == '*' || 		 //the low priority operators, incrementing variable
					complexEquation.charAt(k) == '/' ||		 //'firstOperator_K' - this will be the offset 
					complexEquation.charAt(k) == '^' ||		 //for the left-side of the substring
					complexEquation.charAt(k) == 'r')
				firstOperator_K = k;
			if (complexEquation.charAt(k) == '+' || complexEquation.charAt(k) == '-') {
				operatorFocus_K = k;						 								//when it finds a low priority operator, it saves the
				for (k++; k < complexEquation.length(); k++){								//placement of its position in 'operatorFocus_K';
					if(complexEquation.charAt(k) == '-' &&	 							//if the next operator IS right after the low priority
							k - operatorFocus_J == 1 ||			 						//operator and is negative, or has EXACTLY ONE SPACE between them
							complexEquation.charAt(k) == '-' &&  						//and is a negative, we assume that it is a negative unary operator
							k - operatorFocus_J >= 2 && 								//and continue to loop for the actual second operator
							complexEquation.charAt(k-1) == ' ')			 							
						k++;
					else if(complexEquation.charAt(k) == '*' && k - operatorFocus_K > 1 || 		
					complexEquation.charAt(k) == '/' && k - operatorFocus_K > 1 ||		//when it finds a low priority operator, it saves the
					complexEquation.charAt(k) == '+' && k - operatorFocus_K > 1 ||		//placement of its position in 'operatorFocus_J';
					complexEquation.charAt(k) == '^' && k - operatorFocus_K > 1 ||		//another for loop is put in, continuing from where the
					complexEquation.charAt(k) == 'r' && k - operatorFocus_K > 1 ||		//last for loop was, finding the next operator.
					complexEquation.charAt(k) == '-' && k - operatorFocus_K > 1) {		//if the next operator is NOT right after the low priority
					secondOperator_K = k;					 							//operator, then record the placement of the second operator
					break;									 							//(our right-side of the substring) and then break from the loop
				}	
				else if(complexEquation.charAt(k) == '+' &&
						k - operatorFocus_K == 1)			 									//if the next operator IS right after the low priority operator,
/*possible error*/	throw new IllegalArgumentException("Cannot have a '+' unary operator!");	//and it's a positive, throw an error
				else if(complexEquation.charAt(k) == '*' && k - operatorFocus_K == 1 ||  		//if the next operator IS right after the low priority operator
					complexEquation.charAt(k) == '/' && k - operatorFocus_K == 1 ||		 		//and is NOT a negative or positive operator, then throw a different error
					complexEquation.charAt(k) == '^' && k - operatorFocus_K == 1 ||
					complexEquation.charAt(k) == 'r' && k - operatorFocus_K == 1)
/*possible error*/	throw new IllegalArgumentException("Operator syntax error: " + complexEquation.charAt(k) + complexEquation.charAt(operatorFocus_K));
				}
				//the next few if statements creates the substrings depending on the values of the firstOperator and secondOperator variables
				if(secondOperator_K == 0 && firstOperator_K != 0) 							//indicates that the substring is at the END of a longer string
					currentSimpleExpression = complexEquation.substring(firstOperator_K+1); 
				else if(secondOperator_K != 0 && firstOperator_K == 0)						//indicates that the substring is at the BEGINNING of a longer string
					currentSimpleExpression = complexEquation.substring(0, secondOperator_K);
				else if(secondOperator_K == 0 && firstOperator_K == 0)						//indicates that a substring isn't needed
						currentSimpleExpression = complexEquation;			
				else																		//line below indicates that substring is in the MIDDLE of a longer string
					currentSimpleExpression = complexEquation.substring(firstOperator_K+1, secondOperator_K);
				if(currentSimpleExpression.charAt(0) == 'n') //just changed, works for all except BEGINNING OF LONGER STRING
					currentSimpleExpression = currentSimpleExpression.replaceFirst(Pattern.quote("n"),Matcher.quoteReplacement("-"));
					firstOperator_K = 0;													//firstOperator and secondOperator reset
					secondOperator_K = 0;	
					operatorFocus_K = 0;
					divideByZero(currentSimpleExpression);
					ans = simpleExpression(currentSimpleExpression);						//solve the simple expression of the substring
					if(currentSimpleExpression.charAt(0) == '-')
						currentSimpleExpression = currentSimpleExpression.replaceFirst(Pattern.quote("-"),Matcher.quoteReplacement("n"));
					complexEquation = complexEquation.replaceFirst(Pattern.quote(currentSimpleExpression),Matcher.quoteReplacement(ans)); //replace the substring with the calculated value
					k = 0;																	//because the string length has changed, we have to go through the loop from the beginning
				} 
			}
		if(complexEquation.charAt(0) == 'n')
			return complexEquation	= complexEquation.replaceFirst(Pattern.quote("n"),Matcher.quoteReplacement("-"));
		else
			return complexEquation;
	}
	
	private static void divideByZero(String string) { //added into code
	       int index = -1;
	       while (string.contains("0")) {
	           index = string.indexOf("0");
	           if (index == 0) {
	               string = string.replaceFirst("0", "");
	           }
	           else {
	               if (string.charAt(index-1) == '/') {
	                   System.out.println("Error can't divide by zero");
		   				errorCounter = 1;
		   				calculateCounter = 1;
						errorOutput.setBackground(Color.pink);
						errorOutput.setForeground(Color.black);
						errorOutput.setText("Error: can't divide by zero");
	                   throw new IllegalArgumentException("Error: can't divide by zero");
	               }//end of if inside of else 
	               else {
	                   string = string.replaceFirst("0", "");
	               }
	           }
	       }
	   }
	
	private static String xCheckForError (String string) {
	       string = variableInput.getText();
	       double value = 0;
	      try {
	      value = Double.parseDouble(string);
	      }
	      catch (Exception e) {
 				errorCounter = 1;
 				calculateCounter = 1;
				errorOutput.setBackground(Color.pink);
				errorOutput.setForeground(Color.black);
				errorOutput.setText("X should not be having two values");
	          throw new IllegalArgumentException("X should not be having two values");
	      }
	      return string;
	   }
	private static void checkSpaces(String string) {
        while (string.contains(" ")) {
            int index = string.indexOf(" ");
            char before = string.charAt(index-1);
            char after = string.charAt(index+1);
            if (after == ' ') {
                string = string.replaceFirst(" ", "");
                System.out.println("First if at: " + string);
            }//end of if 
            else if (((before == '+') || (before == '-') || (before == '*') || (before == '/') || (before == '^') || (before == 'r')) || ((after == '+') || (after == '*') || (after == '/') || (after == '^') || (after == 'r') || (after == '-'))) {
                string = string.replaceFirst(" ", "");
                string = string.substring(0, index) + string.substring(index);
                System.out.println("Valid first space so string is:  " + string);
            }//end of else if
            else {
                System.out.println("error at " + index);
                System.out.println("String is: " + string);
                errorCounter = 1;
                calculateCounter = 1;
                errorOutput.setBackground(Color.pink);
                errorOutput.setForeground(Color.black);
                errorOutput.setText("No detected operator inbetween numbers");
                throw new IllegalArgumentException("No detected operator inbetween numbers");
         //       System.exit(0);
            }//end of else
        }//end of while
        //return string;
        return; //can switch to return string if you want it to remove spaces for you
    }// end of checkSpaces
	private static void openParenthesesCheck(String string){
		int unbalancedToggle = 0;
		for(int v = 0; v < string.length(); v++){
			if(string.charAt(v) == '(')
				unbalancedToggle++;
			if(string.charAt(v) == ')')
				unbalancedToggle--;
			if(unbalancedToggle < 0){
				errorCounter = 1;
				calculateCounter = 1;
			errorOutput.setBackground(Color.pink);
			errorOutput.setForeground(Color.black);
			errorOutput.setText("Error: open parentheses");
			throw new IllegalArgumentException("Error: open parentheses");
			}
		}
	}
}
