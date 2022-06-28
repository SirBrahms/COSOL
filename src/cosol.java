import java.util.*;
import java.io.File;
@SuppressWarnings("resource")

public class cosol {
	
	public static void main(String[] args) {
		if (args.length == 0) {
			try {
				Scanner sc = new Scanner(System.in);
				while (true) {
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
		else {
			// Read file supplied in args[1]
			String fullString = "";
			try {
				File fileToRead = new File(args[0]);
				Scanner reader = new Scanner(fileToRead);
				while (reader.hasNextLine()) {
					fullString += reader.nextLine();
				}
				fullString.replace("/n", "");
			} catch (Exception ex) {
				System.out.println("File not found!");
				System.exit(1);
			}
			interpret(fullString);
		}
	}
	
	public static void loadHeaderFile(String location) {
		String fullString = "";
		try {
			File fileToOpen = new File(location);
			Scanner reader = new Scanner(fileToOpen);
			while (reader.hasNextLine()) {
				fullString += reader.nextLine();
			}
			fullString.replace("\n", "");
		}
		catch (Exception ex) {
			System.out.println("Header File not found");
			System.exit(1);
		}
		finally {
			// Collect labels from Header File
			String fullLbl = ""; // String for Collecting Labels
			String fullStr = ""; // String for collecting Strings
			boolean fillStr = false; // Boolean for marking when to collect Strings
			boolean fillLbl = false; // Boolean for marking when to collect Labels
			String prefix = ""; // String for Prefixing the labels that are added
			List<String> _InternalStrStck = new ArrayList<>(); // Internal Stack
			char[] instructionChars = fullString.toCharArray(); // char Array for instructions
			
			for (int i = 0; i < instructionChars.length; i++) {
				
				// String Collection
				if (fillStr && instructionChars[i] != '"') {
					fullStr += instructionChars[i];
				}
				if (instructionChars[i] == '"' && !fillStr) {
					fillStr = true;
				}
				else if (instructionChars[i] == '"' && fillStr) {
					fillStr = false;
					_InternalStrStck.add(fullStr);
					fullStr = "";
				}
				
				// Collect Labels
				if (fillLbl && instructionChars[i] != '{' && instructionChars[i] != '}') {
					fullLbl += instructionChars[i];
				}
				if (instructionChars[i] == '{' && !fillLbl) {
					fillLbl = true;
				}
				else if (instructionChars[i] == '}' && fillLbl) {
					fillLbl = false;
					if (Data._NextLabelname != "" && prefix != "") {
						Data._Labels.put(prefix + "." + Data._NextLabelname, fullLbl);
						fullLbl = "";
						Data._NextLabelname = "";
					}
					else {
						System.out.println("\"?\":{}");
						System.exit(0);
						break;
					}
					
				}
				
				if (!fillLbl && !fillStr) {
					// Assign Name Function
					if (instructionChars[i] == ':') {
						try {
							Data._NextLabelname = _InternalStrStck.get(_InternalStrStck.size() - 1);
							_InternalStrStck.remove(_InternalStrStck.size() - 1);
						}
						catch (Exception ex) {
							System.out.println("'?'");
							System.exit(0);
							break;
						}
					}
					// Assign Prefix Function
					if (instructionChars[i] == '$') {
						try {
							prefix = _InternalStrStck.get(_InternalStrStck.size() - 1);
							_InternalStrStck.remove(_InternalStrStck.size() - 1);
						}
						catch (Exception ex) {
							System.out.println("$?");
							System.exit(0);
							break;
						}
					}
					/* Implement Header file function (for recursive header file implementation) (WIP)
					if (instructionChars[i] == '@') {
						String headerFile = Data.strPop();
						
						// Check if the HeaderFile is the standard library
						if (headerFile.equals("stdlib")) {
							Data._StdlibActive = true;
						}
						else {
							loadHeaderFile(headerFile);
						}
					}
					*/
				}
			}
		}
		
	}
	
	@SuppressWarnings("unused")
	public static void interpret(String instruction) {
		// defining Local Variables
		char[] instructionChar = instruction.toCharArray();
		String fullStr = ""; // String for collecting Strings
		boolean fillStr = false; // Boolean for marking when to collect Strings
		String fullMath = ""; // String for collecting Integers
		boolean fillMath = false; // Boolean for marking when to collect Integers
		String fullArg = ""; // String for collecting Arguments
		boolean fillArg = false; // Boolean for marking when to collect arguments
		String fullCond = ""; // String for collecting Conditional Expressions
		boolean fillCond = false; // Boolean for marking when to collect Conditional Expressions
		String fullLbl = ""; // String for Collecting Labels
		boolean fillLbl = false; // Boolean for marking when to collect Labels
		String fullLoop = ""; // String for Collecting Loops
		boolean fillLoop = false; // Boolean for marking when to collect Loops
		
		boolean strCond = false; // Boolean for marking when to compare strings
		
		
		// Actually interpreting the instruction
		for (int i = 0; i < instructionChar.length; i++) {
			
			// String Collection
			if (fillStr && instructionChar[i] != '"') {
				fullStr += instructionChar[i];
			}
			if (instructionChar[i] == '"' && !fillStr) {
				fillStr = true;
				if (fillCond) {
					strCond = true;
				}
			}
			else if (instructionChar[i] == '"' && fillStr) {
				fillStr = false;
				Data.StrStck.add(fullStr);
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
					Data.MathStck.add(Integer.parseInt(fullMath));
					fullMath = "";
				}
				catch (Exception ex){
					System.out.println("#?#");
					System.exit(0);
					break;
				}
			}
			// Argument Collection
			if (fillArg && instructionChar[i] != '$') {
				fullArg += instructionChar[i];
			}
			if (instructionChar[i] == '$' && !fillArg) {
				fillArg = true;
			}
			else if (instructionChar[i] == '$' && fillArg) {
				Data.ArgStck.add(fullArg);
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
				if (Data._NextLabelname != "") {
					Data._Labels.put(Data._NextLabelname, fullLbl);
					fullLbl = "";
					Data._NextLabelname = "";
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
			if (instructionChar[i] == '[' && !fillLoop && !fillLbl) {
				fillLoop = true;
			}
			else if (instructionChar[i] == ']' && fillLoop) {
				fillLoop = false;
				// Loop Execution
				for (int j = 0; j < Data._LoopIndex; j++) {
					interpret(fullLoop);
				}
				fullLoop = "";
			}
			
			// Ignore Whitespace
			if (instructionChar[i] == ' ') {
				continue;
			}
			
			
			// Math Operations
			// Pops the first to Values of the MathStack, 
			// Does the specified operation, e.g: SecondValue - FirstValue
			// The Second Value popped is always the first value in the operation!
			if (!fillStr && !fillMath && !fillArg && Data.MathStck.size() >= 2 && !fillLbl) {
				if (instructionChar[i] == '+') {
					int y = Data.mathPop();
					int x = Data.mathPop();
					Data.MathStck.add(x + y);
				}
				else if (instructionChar[i] == '-') {
					int y = Data.mathPop();
					int x = Data.mathPop();
					Data.MathStck.add(x - y);
				}
				else if (instructionChar[i] == '*') {
					int y = Data.mathPop();
					int x = Data.mathPop();
					Data.MathStck.add(x * y);
				}
				else if (instructionChar[i] == '/') {
					int y = Data.mathPop();
					int x = Data.mathPop();
					Data.MathStck.add(x / y);
				}
			}
			
			
			// Comparisons
			// (for filling the Control Stack: Branching will be in the standard operations)
			if (!fillStr && !fillMath && !fillArg && fillCond && !fillLbl) {
				if (instructionChar[i] == '=') {
					if (!strCond) {
						int x = Data.mathPop();
						int y = Data.mathPop();
						
						if (x == y) {
							Data.CtrlStck.add(1);
						}
						else {
							Data.CtrlStck.add(0);
						}
					}
					else {
						String str1 = Data.strPop();
						String str2 = Data.strPop();
						
						if (str1.equals(str2)) {
							Data.CtrlStck.add(1);
						}
						else {
							Data.CtrlStck.add(0);
						}
					}
				}
				if (instructionChar[i] == '<') {
					int x = Data.mathPop();
					int y = Data.mathPop();
					
					if (x <= y) {
						Data.CtrlStck.add(1);
					}
					else {
						Data.CtrlStck.add(0);
					}
				}
				if (instructionChar[i] == '>') {
					int x = Data.mathPop();
					int y = Data.mathPop();
					
					if (x >= y) {
						Data.CtrlStck.add(1);
					}
					else {
						Data.CtrlStck.add(0);
					}
				}
				if (instructionChar[i] == '!') {
					if (!strCond) {
						int x = Data.mathPop();
						int y = Data.mathPop();
						
						if (x != y) {
							Data.CtrlStck.add(1);
						}
						else {
							Data.CtrlStck.add(0);
						}
					}
					else {
						String str1 = Data.strPop();
						String str2 = Data.strPop();
						
						if (!str1.equals(str2)) {
							Data.CtrlStck.add(1);
						}
						else {
							Data.CtrlStck.add(0);
						}
						strCond = false;
					}
				}
			}
			
			
			// Standard Operations
			if (!fillStr && !fillMath && !fillArg && !fillCond && !fillLbl) {
				// Print Function
				if (instructionChar[i] == '.') {
					System.out.println(Data.strPop());
				}
				// Shift to the Stack being pointed at by the Stack Index
				if (instructionChar[i] == '<') {
					if (Data.StckIndex == 0) {
						Data.StrStck.add(Data.mathPop().toString());
					}
					else if (Data.StckIndex == 1) {}
					else if (Data.StckIndex == 2) {
						Data.ArgStck.add(Data.strPop());
					}
					else if (Data.StckIndex == 3) {
						Data.CtrlStck.add(Data.mathPop());
					}
				}
				// Try Shift to Math Function
				if (instructionChar[i] == '>') {
					try {
						Data.MathStck.add(Integer.parseInt(Data.strPop()));
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
						String str = sc.nextLine();
						Data.StrStck.add(str);
					}
					catch (Exception ex) {
						System.out.println(">?");
						System.exit(0);
						break;
					}
				}
				// Stack Clear Function
				if (instructionChar[i] == '?') {
					if (Data.StckIndex == 0) {
						Data.StrStck.clear();
					}
					else if (Data.StckIndex == 1) {
						Data.MathStck.clear();
					}
					else if (Data.StckIndex == 2) {
						Data.CtrlStck.clear();
					}
					else {
						System.out.println("|?");
						System.exit(0);
					}
				}
				// Stack Index set Function
				if (instructionChar[i] == '|') {
					Data.StckIndex = Data.mathPop();
				}
				// Program Quit Function
				if (instructionChar[i] == '\\') {
					System.exit(Data.mathPop());
				}
				// Duplicate the top Value of the Stack referenced by the Stack index
				if (instructionChar[i] == '/') {
					if (Data.StckIndex == 0) {
						String duplicate = Data.strPop();
						Data.StrStck.add(duplicate);
						Data.StrStck.add(duplicate);
					}
					else if (Data.StckIndex == 1) {
						int duplicate = Data.mathPop();
						Data.MathStck.add(duplicate);
						Data.MathStck.add(duplicate);
					}
					else if (Data.StckIndex == 2) {
						String duplicate = Data.argPop();
						Data.ArgStck.add(duplicate);
						Data.ArgStck.add(duplicate);
					}
					else if (Data.StckIndex == 3) {
						int duplicate = Data.ctrlPop();
						Data.CtrlStck.add(duplicate);
						Data.CtrlStck.add(duplicate);
					}
					else {
						System.out.println("|?");
						System.exit(0);
					}
				}
				// Swap Function: Swaps the last two values on the stack (uses stack index)
				if (instructionChar[i] == '~') {
					if (Data.StckIndex == 0) {
						String str1 = Data.strPop();
						String str2 = Data.strPop();
						Data.StrStck.add(str2);
						Data.StrStck.add(str1);
					}
					else if (Data.StckIndex == 1) {
						Integer num1 = Data.mathPop();
						Integer num2 = Data.mathPop();
						Data.MathStck.add(num1);
						Data.MathStck.add(num2);
					}
					else {
						System.out.println("|?");
					}
				}
				// Implement Header file function
				if (instructionChar[i] == '@') {
					String headerFile = Data.strPop();
					
					// Check if the HeaderFile is the standard library
					if (headerFile.equals("stdlib")) {
						Data._StdlibActive = true;
					}
					else {
						loadHeaderFile(headerFile);
					}
				}
				// Set Loop-Index
				if (instructionChar[i] == ';') {
					Data._LoopIndex = Data.mathPop() - 1;
				}
				// Set next Label-Name
				if (instructionChar[i] == ':') {
					Data._NextLabelname = Data.strPop();
					Data._ObjectPtrs.put(Data._NextLabelname, Data.AssignPtr);
					Data.AssignPtr += 8;
				}
				// Conditional Jump to Label Function
				if (instructionChar[i] == '\'') {
					if (Data.ctrlPop() == 1) {
						String Label = Data.strPop();
						try {
							interpret(Data._Labels.get(Label));
						}
						catch (Exception ex) {
							if (Data._StdlibActive) {
								stdlib.tryCallLabel(Label);
							}
							else {
								System.out.println("{?}");
								System.exit(0);
								break;
							}
						}
					}
					
				}
				// Jump to Label Function
				if (instructionChar[i] == '^') {
					String Label = Data.strPop();
					try {
						interpret(Data._Labels.get(Label));
					}
					catch (Exception ex) {
						if (Data._StdlibActive) {
							stdlib.tryCallLabel(Label);
						}
						else {
							System.out.println("{?}");
							System.exit(0);
							break;
						}
					}
				}
				// Conditional Goto Function
				if (instructionChar[i] == '!') {
					if (Data.ctrlPop() == 1) {
						try {
							i = Data.mathPop() - 1; // Reset the for Loop to the desired position -> since i would instantly increase, we subtract one right away, so we get the correct value.
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
				// Pointer reference function
				if (instructionChar[i] == '&') {
					try {
						int address = Data.mathPop();
						// Iterating trough the HashMap to find the right key that is being referenced
						Data._ObjectPtrs.forEach((key, value) -> {
						    if (value.equals(address)) {
						    	Data.StrStck.add(key);
						    }
						});
					}
					catch (Exception ex) {
						System.out.println("&?");
						System.exit(0);
						break;
					}
				}
			}
		}
		//System.out.println(Data.StrStck);
		//System.out.println(Data.MathStck);
		//System.out.println(Data.CtrlStck);
		//System.out.println(Data.ArgStck);
		//System.out.println(Data._Labels);
		//System.out.println(Data._ObjectPtrs);
		//System.out.println(Data.StckIndex);
	}
}
