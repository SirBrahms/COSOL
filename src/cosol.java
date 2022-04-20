import java.util.*;
@SuppressWarnings("resource")

public class cosol {
	public static List<String> StrStck = new ArrayList<>(); // List for Strings
	public static List<Integer> MathStck = new ArrayList<>(); // List for Performing Math operations (integers)
	public static List<Integer> CtrlStck = new ArrayList<>(); // List for implementing Control-Flow
	public static Hashtable<String, String> _Labels = new Hashtable<>(); // HashTable for storing Labels with their names
	public static String _NextLabelname = ""; // String for storing the Name of the Next Label
	public static Integer _LoopIndex = 0; // Integer for storing the Loop-Index of a given loop
	public static Integer LastMathPop = 0; // Integer for storing the last value that was popped from the Math Stack
	
	public static void main(String[] args) {
		try {
			while (true) {
				Scanner sc = new Scanner(System.in);
				System.out.print("> ");
				String instruction = sc.nextLine();
				interpret(instruction);
			}
		}
		catch (Exception ex){
			System.out.println(">?");
			System.exit(0);
		}
	}
	
	public static Integer mathPop() {
		int num = MathStck.get(MathStck.size() - 1);
		LastMathPop = num;
		MathStck.remove(MathStck.size() - 1);
		return num;
	}
	
	public static Integer ctrlPop() {
		int num = CtrlStck.get(CtrlStck.size() - 1);
		CtrlStck.remove(CtrlStck.size() - 1);
		return num;
	}
	
	public static String strPop() {
		try {
			String str = StrStck.get(StrStck.size() - 1);
			StrStck.remove(StrStck.size() - 1);
			return str;
		}
		catch (Exception ex) {
			System.out.println("'?'");
			System.exit(0);
		}
		return null;
	}
	
	
	public static void interpret(String instruction) {
		// defining Local Variables
		char[] instructionChar = instruction.toCharArray();
		String fullStr = ""; // String for collecting Strings
		boolean fillStr = false; // Boolean for marking when to collect Strings
		String fullMath = ""; // String for collecting Integers
		boolean fillMath = false; // Boolean for marking when to collect Integers
		String fullCond = ""; // String for collecting Conditional Expressions
		boolean fillCond = false; // Boolean for marking when to collect Conditional Expressions
		String fullLbl = ""; // String for Collecting Labels
		boolean fillLbl = false; // Boolean for marking when to collect Labels
		String fullLoop = ""; // String for Collecting Loops
		boolean fillLoop = false; // Boolean for marking when to collect Loops
		int stckIndex = 0; // Integer for marking which Stack should be addressed
		
		
		// Actually interpreting the instruction
		for (int i = 0; i < instructionChar.length; i++) {
			// Ignore Whitespace
			if (instructionChar[i] == ' ') {
				continue;
			}
			
			// String Collection
			if (fillStr && instructionChar[i] != '"') {
				fullStr += instructionChar[i];
			}
			if (instructionChar[i] == '"' && !fillStr) {
				fillStr = true;
			}
			else if (instructionChar[i] == '"' && fillStr) {
				fillStr = false;
				StrStck.add(fullStr);
				fullStr = "";
			}
			// Integer Collection
			if (fillMath && instructionChar[i] != '#') {
				fullMath += instructionChar[i];
			}
			if (instructionChar[i] == '#' && !fillMath) {
				fillMath = true;
			}
			else if (instructionChar[i] == '#' && fillMath) {
				try {
					fillMath = false;
					MathStck.add(Integer.parseInt(fullMath));
					fullMath = "";
				}
				catch (Exception ex){
					System.out.println("#?#");
					System.exit(0);
					break;
				}
			}
			// Conditional Collection
			if (fillCond && instructionChar[i] != '(' && instructionChar[i] != ')') {
				fullCond += instructionChar[i];
			}
			if (instructionChar[i] == '(' && !fillCond) {
				fillCond = true;
			}
			else if (instructionChar[i] == ')' && fillCond) {
				fillCond = false;
				fullCond = "";
			}
			// Label Collection
			if (fillLbl && instructionChar[i] != '{' && instructionChar[i] != '}') {
				fullLbl += instructionChar[i];
			}
			if (instructionChar[i] == '{' && !fillLbl) {
				fillLbl = true;
			}
			else if (instructionChar[i] == '}' && fillLbl) {
				fillLbl = false;
				if (_NextLabelname != "") {
					_Labels.put(_NextLabelname, fullLbl);
					fullLbl = "";
					_NextLabelname = "";
				}
				else {
					System.out.println("\"?\":{}");
					System.exit(0);
					break;
				}
				
			}
			
			// Loop Collection and Execution
			if (fillLoop && instructionChar[i] != '[' && instructionChar[i] != ']') {
				fullLoop += instructionChar[i];
			}
			if (instructionChar[i] == '[' && !fillLoop) {
				fillLoop = true;
			}
			else if (instructionChar[i] == ']' && fillLoop) {
				fillLoop = false;
				// Loop Execution
				for (int j = 0; j < _LoopIndex; j++) {
					interpret(fullLoop);
				}
				fullLoop = "";
			}
			
			
			// Math Operations
			// Pops the first to Values of the MathStack, 
			// Does the specified operation, e.g: SecondValue - FirstValue
			// The Second Value popped is always the first value in the operation!
			if (!fillStr && !fillMath && MathStck.size() >= 2 && !fillLbl) {
				if (instructionChar[i] == '+') {
					int y = mathPop();
					int x = mathPop();
					MathStck.add(x + y);
				}
				else if (instructionChar[i] == '-') {
					int y = mathPop();
					int x = mathPop();
					MathStck.add(x - y);
				}
				else if (instructionChar[i] == '*') {
					int y = mathPop();
					int x = mathPop();
					MathStck.add(x * y);
				}
				else if (instructionChar[i] == '/') {
					int y = mathPop();
					int x = mathPop();
					MathStck.add(x / y);
				}
			}
			
			
			// Comparisons
			// (for filling the Control Stack: Branching will be in the standard operations)
			if (!fillStr && !fillMath && fillCond && !fillLbl) {
				if (instructionChar[i] == '=') {
					int x = mathPop();
					int y = mathPop();
					
					if (x == y) {
						CtrlStck.add(1);
					}
					else {
						CtrlStck.add(0);
					}
				}
				if (instructionChar[i] == '<') {
					int x = mathPop();
					int y = mathPop();
					
					if (x <= y) {
						CtrlStck.add(1);
					}
					else {
						CtrlStck.add(0);
					}
				}
				if (instructionChar[i] == '>') {
					int x = mathPop();
					int y = mathPop();
					
					if (x >= y) {
						CtrlStck.add(1);
					}
					else {
						CtrlStck.add(0);
					}
				}
				if (instructionChar[i] == '!') {
					int x = mathPop();
					int y = mathPop();
					
					if (x != y) {
						CtrlStck.add(1);
					}
					else {
						CtrlStck.add(0);
					}
				}
			}
			
			
			// Standard Operations
			if (!fillStr && !fillMath && !fillCond && !fillLbl) {
				// Print Function
				if (instructionChar[i] == '.') {
					System.out.println(strPop());
				}
				// Shift to String Function
				if (instructionChar[i] == '<') {
					StrStck.add(mathPop().toString());
				}
				// Shift to Control Function
				if (instructionChar[i] == '>') {
					int num = mathPop();
					if (num == 0 || num == 1) {
						CtrlStck.add(num);
					}
					else {
						System.out.println(">>?");
						System.exit(0);
						break;
					}
				}
				// Try Shift to Math Function
				if (instructionChar[i] == '�') {
					try {
						MathStck.add(Integer.parseInt(strPop()));
					}
					catch (Exception ex) {
						System.out.println("#?#");
						System.exit(0);
						break;
					}
				}
				// Input Function
				if (instructionChar[i] == '_') {
					try {
						Scanner sc = new Scanner(System.in);
						String str = sc.next();
						StrStck.add(str);
					}
					catch (Exception ex) {
						System.out.println(">?");
						System.exit(0);
						break;
					}
				}
				// Stack Clear Function
				if (instructionChar[i] == '?') {
					if (stckIndex == 0) {
						StrStck.clear();
					}
					else if (stckIndex == 1) {
						MathStck.clear();
					}
					else if (stckIndex == 2) {
						CtrlStck.clear();
					}
					else {
						System.out.println("|?");
					}
				}
				// Stack Index increment Function
				if (instructionChar[i] == '|') {
					stckIndex++;
				}
				// Stack Index decrement Function
				if (instructionChar[i] == '�') {
					stckIndex--;
				}
				// Re-put the last thing popped
				if (instructionChar[i] == '�') {
					MathStck.add(LastMathPop);
				}
				// Program Quit Function
				if (instructionChar[i] == '*') {
					System.exit(mathPop());
				}
				// Swap Function: Swaps the last two values on the stack (uses stack index)
				if (instructionChar[i] == '~') {
					if (stckIndex == 0) {
						String str1 = strPop();
						String str2 = strPop();
						StrStck.add(str2);
						StrStck.add(str1);
					}
					else if (stckIndex == 1) {
						Integer num1 = mathPop();
						Integer num2 = mathPop();
						MathStck.add(num1);
						MathStck.add(num2);
					}
					else {
						System.out.println("|?");
					}
				}
				// Set Loop-Index
				if (instructionChar[i] == ';') {
					_LoopIndex = mathPop() - 1;
				}
				// Set next Label-Name
				if (instructionChar[i] == ':') {
					_NextLabelname = strPop();
				}
				// Conditional Jump to Label Function
				if (instructionChar[i] == '\'') {
					if (CtrlStck.get(CtrlStck.size() - 1) == 1) {
						try {
							interpret(_Labels.get(strPop()));
						}
						catch (Exception ex) {
							System.out.println("{?}");
							System.exit(0);
							break;
						}
					}
					else {
						continue;
					}
				}
				// Jump to Label Function
				if (instructionChar[i] == '^') {
					try {
						interpret(_Labels.get(strPop()));
					}
					catch (Exception ex) {
						System.out.println("{?}");
						System.exit(0);
						break;
					}
				}
				// Conditional Goto Function
				if (instructionChar[i] == '!') {
					if (CtrlStck.get(CtrlStck.size() - 1) == 1) {
						try {
							i = mathPop() - 1; // Reset the for Loop to the desired position -> since i would instantly increase, we subtract one right away, so we get the correct value.
							continue;
						}
						catch (Exception ex) {
							System.out.println("!?");
							System.exit(0);
							break;
						}
					}
					else {
						continue;
					}
				}
				// Goto Function
				if (instructionChar[i] == '�') {
					try {
						i = mathPop() - 1; // Reset the for Loop to the desired position -> since i would instantly increase, we subtract one right away, so we get the correct value.
						continue;
					}
					catch (Exception ex) {
						System.out.println("�?");
						System.exit(0);
						break;
					}
				}
			}
		}
		//System.out.println(StrStck);
		//System.out.println(MathStck);
		//System.out.println(CtrlStck);
		//System.out.println(_Labels);
	}
}