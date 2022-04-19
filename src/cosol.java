import java.util.*;
@SuppressWarnings("resource")

public class cosol {
	public static List<String> StrStck = new ArrayList<>(); // List for Strings
	public static List<Integer> MathStck = new ArrayList<>(); // List for Performing Math operations (integers)
	public static List<Integer> CtrlStck = new ArrayList<>(); // List for implementing Control-Flow
	public static Integer LastMathPop = 0;
	
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
	
	public static boolean isNum(char num) {
		return num == '0' || num == '1' || num == '2' || num == '3' || num == '4' || num == '5' || num == '6' || num == '7' || num == '8' || num == '9' || num == '0';
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
		char[] instructionChar = instruction.toCharArray();
		String fullStr = ""; // String for collecting Strings
		boolean fillStr = false; // Boolean for marking when to collect Strings
		String fullMath = ""; // String for collecting Integers
		boolean fillMath = false; // String for marking when to collect Integers
		int stckIndex = 0; // Integer for marking which Stack should be addressed
		
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
			// Math Operations
			// Pops the first to Values of the MathStack, 
			// Does the specified operation, e.g: SecondValue - FirstValue
			// The Second Value popped is always the first value in the operation!
			if (!fillStr && !fillMath && MathStck.size() >= 2) {
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
			if (!fillStr && !fillMath) {
				// Conditional jump function
				if (instructionChar[i] == '\'') {
					int num = ctrlPop();
					if (num != 0) {
						i = MathStck.get(MathStck.size() - 1) - 1; // Reset the for Loop to the desired position -> since i would instantly increase, we subtract one right away, so we get the correct value.
						continue;
					}
				}
			}
			// Standard Operations
			if (!fillStr && !fillMath) {
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
					CtrlStck.add(mathPop());
				}
				// Try Shift to Math Function
				if (instructionChar[i] == '¬') {
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
				if (instructionChar[i] == '¦') {
					stckIndex--;
				}
				// Re-put the last thing popped
				if (instructionChar[i] == '°') {
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
				// Goto Function
				if (instructionChar[i] == '!') {
					try {
						i = MathStck.get(MathStck.size() - 1) - 1; // Reset the for Loop to the desired position -> since i would instantly increase, we subtract one right away, so we get the correct value.
						continue;
					}
					catch (Exception ex) {
						System.out.println("!?");
						System.exit(0);
						break;
					}
				}
			}
		}
		System.out.println(StrStck);
		System.out.println(MathStck);
		System.out.println(CtrlStck);
	}
}
