import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Data {
	public static List<String> StrStck = new ArrayList<>(); // List for Strings
	public static List<Integer> MathStck = new ArrayList<>(); // List for Performing Math operations (integers)
	public static List<Integer> CtrlStck = new ArrayList<>(); // List for implementing Control-Flow
	public static List<String> ArgStck = new ArrayList<>(); // List for implementing the Passing of Arguments (String Only)
	public static Hashtable<String, String> _Labels = new Hashtable<>(); // HashTable for storing Labels with their names
	public static Hashtable<String, Integer> _ObjectPtrs = new Hashtable<>(); // HashTable for storing Objects with their Pointers
	public static String _NextLabelname = ""; // String for storing the Name of the Next Label
	public static Integer _LoopIndex = 0; // Integer for storing the Loop-Index of a given loop
	public static Integer LastMathPop = 0; // Integer for storing the last value that was popped from the Math Stack
	public static Integer StckIndex = 0; // Integer for marking which Stack should be addressed: 0 = strStck, 1 = mathStck
	public static Integer AssignPtr = 0; // Integer for indicating pointers
	public static boolean _StdlibActive = false; // Boolean for marking when the standard library has been implemented
	
	
	
	// Method for Popping values off the Math Stack
	public static Integer mathPop() {
		try {
			int num = Data.MathStck.get(Data.MathStck.size() - 1);
			Data.LastMathPop = num;
			Data.MathStck.remove(Data.MathStck.size() - 1);
			return num;
		}
		catch (Exception ex) {
			System.out.println("#?#");
			System.exit(0);
		}
		return null;
	}
	
	// Method for Popping values off the Control Stack
	public static Integer ctrlPop() {
		try {
			int num = Data.CtrlStck.get(Data.CtrlStck.size() - 1);
			Data.CtrlStck.remove(Data.CtrlStck.size() - 1);
			return num;
		}
		catch (Exception ex) {
			System.out.println("ctrl?");
			System.exit(0);
		}
		return null;
	}
	
	// Method for Popping values off the String Stack
	public static String strPop() {
		try {
			String str = Data.StrStck.get(Data.StrStck.size() - 1);
			Data.StrStck.remove(Data.StrStck.size() - 1);
			return str;
		}
		catch (Exception ex) {
			System.out.println("'?'");
			System.exit(0);
		}
		return null;
	}
	
	// Method for Popping values off the Argument Stack
	public static String argPop() {
		try {
			String str = Data.StrStck.get(Data.StrStck.size() - 1);
			Data.StrStck.remove(Data.StrStck.size() - 1);
			return str;
		}
		catch (Exception ex) {
			System.out.println("'?'");
			System.exit(0);
		}
		return null;
	}
	

}
