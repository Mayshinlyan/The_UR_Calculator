import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class URCalculator {

	private static HashMap<Character, Integer> operator = new HashMap<>();
	private static HashMap<String, Double> variables = new HashMap<>();
	private static Stack stack = new Stack(); ; //stack to store operators according to their precedence
	private static Stack calStack = new Stack(); //stack to store the postfix character;
	private static boolean catchException = false;

	public static void main(String[] args) {

		//hashmap table storing the operation symbols according to their precedence.
		operator.put('{', 0);
		operator.put('}', 0);
		operator.put('[', 1);
		operator.put(']', 1);
		operator.put('(', 2);
		operator.put(')', 2);
		operator.put('+', 3);
		operator.put('-', 4);
		operator.put('/', 5);
		operator.put('*', 6);
		operator.put('^', 7);


		boolean exit = false;


		System.out.println("***********************************Welcome to the May's Calculator!*********************************");

		while (exit==false){
			System.out.println("\n\nPlease enter the mathematical expression you want to compute. You may enter 'clear all' to clear the memory \n "
					+"'clear VarX' to clear a particular varX and 'exit' to exit the calculator.");
			Scanner scan = new Scanner(System.in);
			String input = scan.nextLine();	 //string that user has put in


			double value = 0;


			if(input.equals("clear all")){ //clearing the stored data and calculation.
				variables.clear();
				stack.clear();
				calStack.clear();
			}else if(input.contains("clear ")){
				String[] string = input.split(" ");
				variables.remove(string[1]);
				System.out.println(string[1] + " is removed!");
				variables.get("a");
			}else if(input.equals("exit")){
				exit = true;
			}else {
				input = input.replaceAll("\\s", ""); //removing the spaces in the string


				if(isParenthesisMatch(input)){ // checking if the expression is well formed or not.

					//array storing the variable names
					String[] varList = input.split("=");

					//string to be converted to postfix
					String cal = varList[varList.length-1];

					if(cal.length()<=2){
						value = Double.parseDouble(cal);
					}
					else{
						String[] stringList = cal.split("(?<=[\\-\\+\\*\\/\\(\\)\\{\\}\\[\\]])|(?=[\\-\\+\\*\\/\\(\\)\\{\\}\\[\\]])");

						//converting the String[] into arraylist
						ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(stringList));

						//removing extra operators
						extraOp(arrayList);


						//calculation part
						try{
							System.out.println("Postfix expression: "+ infixToPostfix(arrayList));
							value = Double.parseDouble(calculate(infixToPostfix(arrayList)));
							System.out.println("The value is: "+ value);
						}catch(Exception NumberFormatException){
							System.err.println("The mathematical expression is not valid.");
						}

					}
					//printing the value and the key
					for(int i=0; i<varList.length-1;i++){

						String key = varList[i];
						variables.put(key, value);
						System.out.println("The value of the key: " + key + " is "+ variables.get(key));
					}
				}
			}
		}


		System.out.println("**************************************Thanks for using May Calculator!************************************************************");
	}

	public static ArrayList<String> extraOp(ArrayList<String> list){
		int length = list.size();


		//method to deal with +++++a
		for(int i=length-2; i>=0 ; i--){

			if(list.get(i).equals("+") || list.get(i).equals("-") || list.get(i).equals("/") || list.get(i).equals("*")){

				if(list.get(i).equals(list.get(i+1))){
					list.remove(i+1);

				}

				else if (list.get(0).equals("+") || list.get(0).equals("-") || list.get(0).equals("/") || list.get(0).equals("*")){
					list.remove(0);

				}
			}
		}

		return list;
	}

	//method to check whether the parenthesis matches or not
	public static boolean isParenthesisMatch(String str) {
		if (str.charAt(0) == '{')
			return false;

		Stack<Character> stack = new Stack<Character>();

		char c;
		for(int i=0; i < str.length(); i++) {
			c = str.charAt(i);

			if(c == '(')
				stack.push(c);
			else if(c == '{')
				stack.push(c);
			else if (c == '[')
				stack.push(c);
			else if(c == ')')
				if(stack.empty()){
					System.err.println("The expression is not well formed");
					return false;}
				else if(stack.peek() == '(')
					stack.pop();
				else
				{System.err.println("The expression is not well formed");
				return false;}
			else if(c == ']')
				if(stack.empty()){
					System.err.println("The expression is not well formed");
					return false;}
				else if(stack.peek() == '[')
					stack.pop();
				else{System.err.println("The expression is not well formed");
				return false;}
			else if(c == '}')
				if(stack.empty()){
					System.err.println("The expression is not well formed");
					return false;}
				else if(stack.peek() == '{')
					stack.pop();
				else{System.err.println("The expression is not well formed");
				return false;}
		}

		if(!stack.empty()){
			System.err.println("The expression is not well formed");
		}
		return stack.empty();
	}


	//method to convert infix expression to postfix expression
	public static String infixToPostfix(ArrayList<String> list){

		String outputStr = "";

		for(int i=0; i<list.size(); i++){ //looping through the string

			if(isOperator((list.get(i)).charAt(0))){ //checking whether the character is an operator or not

				char curChar = list.get(i).charAt(0);


				if(stack.isEmpty()){ //checking the stack is empty or not
					stack.push(curChar); //add the operator to the stack
				}
				else if(curChar == ')' || curChar == ']' || curChar == '}'){//popping out all the operators between the two brackets
					char top = (char) stack.peek();
					//if(top)
					while((top != '(') && (top !='[') && (top !='{')){

						if(!stack.isEmpty()){
							outputStr += " "+stack.pop();
							top = (char) stack.peek();
						}
					}
					stack.pop(); //popping the top operator.
				}else if(curChar == '-' && list.get(i-1).charAt(0)== '('){
					stack.push(""+"0"+curChar);
				}
				else if(operator.get(stack.peek())<operator.get(curChar) || curChar == '(' || curChar == '[' || curChar == '{'){ //checking the precedence of the operator
					stack.push(curChar);//add the operator to the stack
				}else {
					outputStr +=" " + stack.pop();//remove the top operator from the stack
					stack.push(curChar);//add the operator to the stack
				}

			}else {outputStr +=" "+ list.get(i);} //add the character to the output string
		}

		while(!stack.isEmpty()){ //if the stack is not empty
			outputStr +=" "+ stack.pop(); // add it to the output String
		}
		return outputStr;
	}


	//method to check whether the character is an operator or not.
	public static boolean isOperator(char op){
		boolean pass = false;
		for(Map.Entry<Character, Integer> entry : operator.entrySet()){
			if(op == entry.getKey()){
				pass = true;
				break;
			}else pass = false;
		}
		return pass;
	}


	//method to calculate the postfix expression
	public static String calculate(String s){
		String outputStr = ""; //output string
		String key = ""; //key to put variables into HashMap
		String[] list;
		//System.out.println("length is " + s.length()+ " and the s is:" + s);
		if(s.length()!=2){
			list = s.split(" ");
		}else{
			list = new String[1];
			list[0]=s;
		}

		for(int i=0; i< list.length; i++){ //looping through the string

			double x=0,y=0,r=0;


			if(list[i].equals(" ")){

			}else if(list[i].equals("+")){ //operation for addition
				x =  (double) calStack.pop();
				y =  (double) calStack.pop();
				r = x+y;
				calStack.push(r);
			}else if(list[i].equals("-")){ //operation for subtraction
				x = (double) calStack.pop();
				y = (double) calStack.pop();
				r = y-x;
				calStack.push(r);
			}else if(list[i].equals("/")){ //operation for division
				x = (double) calStack.pop();
				y = (double) calStack.pop();

				if(x==0){
					System.err.println("Division by 0 is not allowed!");
					return null;
				}else {
					r = y/x;
					calStack.push(r);
				}

			}else if(list[i].equals("*")){ //operation for multiplication
				x = (double) calStack.pop();
				y = (double) calStack.pop();
				r = x*y;
				calStack.push(r);
			}else if(isNumeric(list[i])) {
				//if the character is a digit add it to the stack
				double d = Double.parseDouble(list[i]);
				calStack.push(d);


			}else if(isAlpha(list[i])){

				try{
					double t = variables.get(list[i]);

					calStack.push(t);
				}catch(Exception NullPointerException){
					System.err.println("The variable: '"+ list[i] + "' is not defined.");

				}
			}
		}

		if(!calStack.isEmpty()){
			double r = (double) calStack.pop(); //assigning the final result to r.
			outputStr += r;
		}
		return outputStr; //printing out the output string.
	}

	//method to check whether the string has letter or not.
	public static boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}

	//method to check whether a string a number or not.
	public static boolean isNumeric(String str)
	{
		try
		{
			double d = Double.parseDouble(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

}
