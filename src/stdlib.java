import java.io.File;
import java.io.FileWriter;
import java.util.*;

@SuppressWarnings("resource")
// Standard Library for COSOL
public class stdlib {
	// Functions for matching the Label-Calls
	public static void tryCallLabel(String labelname) {
		switch (labelname) {
			// File IO
			case "WriteFile":
				writeToFile();
				break;
			case "ReadFile":
				readFromFile();
				break;
			case "DeleteFile":
				deleteFile();
				break;
			// Threading
			case "RunThread":
				startThread();
				break;
			// Pointers
			case "pointerof":
				pointerof();
				break;
			case "rptr":
				callpointer();
				break;
			case "AssignPointer":
				assignPointer();
				break;
			default:
				System.out.println("{?}");
				System.exit(0);
				break;
		}
	}
	
	/* File IO-Section:
	 * - writeToFile() -> writes text to a file
	 * - readFromFile() -> reads text from a file
	 * - deleteFile() -> deletes a file
	 */
	
	private static void writeToFile() {
		try {
			FileWriter writer = new FileWriter(Data.strPop());
			writer.write(Data.strPop());
			writer.close();
		}
		catch (Exception ex) {
			System.out.println("stdlib: fileWriterFault");
			System.exit(0);
		}
		return;
	}
	
	private static void readFromFile() {
		try {
			File fileToRead = new File(Data.strPop());
			Scanner reader = new Scanner(fileToRead);
			while (reader.hasNextLine()) {
				Data.StrStck.add(reader.nextLine());
			}
		}
		catch (Exception ex) {
			System.out.println("stdlib: fileReaderFault");
			System.exit(0);
		}
	}
	
	private static void deleteFile() {
		File fileToDelete = new File(Data.strPop());
		if (!fileToDelete.delete()) {
			System.out.println("stdlib: fileDeletionFault");
			System.exit(0);
		}
	}
	
	/* Threading Section
	 * - createThread() -> creates a new thread with the specified label
	 * WIP!!
	 */
	
	private static void startThread() {
		new Thread(() -> {
		    cosol.interpret(Data._Labels.get(Data.strPop()));
		}).start();
	}
	
	/* Pointer Section
	 * - pointerof() -> gets the pointer of the top value on the String Stack and pushes it onto the Math Stack (Only String Arguments)
	 * - callpointer() -> calls a label based on the top value on the Math Stack (Interpreted as a pointer)
	 * - assignPointer() -> Assigns a pointer to any value that is on top of the Stack referenced by the Stack index (depends on Stack Index)
	 */
	
	private static void pointerof() {
		try {
			Integer ptr = Data._ObjectPtrs.get(Data.strPop());
			Data.MathStck.add(ptr);
		}
		catch (Exception ex) {
			System.out.println("{?}");
			System.exit(0);
		}
	}
	
	private static void callpointer() {
		try {
			Integer val = Data.mathPop();
			// Iterating trough the HashMap to find the right key that is being referenced
			Data._ObjectPtrs.forEach((key, value) -> {
			    if (value.equals(val)) {
			        cosol.interpret(Data._Labels.get(key));
			    }
			});
		}
		catch (Exception ex) {
			System.out.println("{?}");
			System.exit(0);
		}
	}
	
	private static void assignPointer() {
		switch (Data.StckIndex) {
			case 0:
				String val = Data.strPop();
				Data._ObjectPtrs.put(val, Data.AssignPtr);
				Data.AssignPtr++;
				break;
			case 1:
				Integer num = Data.mathPop();
				Data._ObjectPtrs.put(num.toString(), Data.AssignPtr);
				Data.AssignPtr++;
				break;
			case 2:
				String arg = Data.argPop();
				Data._ObjectPtrs.put(arg.toString(), Data.AssignPtr);
				Data.AssignPtr++;
				break;
			case 3:
				Integer ctrl = Data.ctrlPop();
				Data._ObjectPtrs.put(ctrl.toString(), Data.AssignPtr);
				Data.AssignPtr++;
				break;
		}
	}
}
