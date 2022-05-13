import java.io.File;
import java.io.FileWriter;
import java.util.*;

@SuppressWarnings("resource")
// Standard Library for COSOL
public class stdlib {
	// Functions for matching the Label-Calls
	public static void tryCallLabel(String labelname) {
		switch (labelname) {
			case "WriteFile":
				writeToFile();
				break;
			case "ReadFile":
				readFromFile();
				break;
			case "DeleteFile":
				deleteFile();
				break;
			case "RunThread":
				startThread();
				break;
			case "pointerof":
				pointerof();
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
	 * createThread() -> creates a new thread with the specified label
	 * WIP!!
	 */
	
	private static void startThread() {
		new Thread(() -> {
		    cosol.interpret(Data._Labels.get(Data.strPop()));
		}).start();
	}
	
	/* Pointer Section
	 * pointerof() -> gets the pointer of the top value on the String Stack and pushes it onto the Math Stack
	 */
	
	private static void pointerof() {
		try {
			String labelname = Data._Labels.get(Data.strPop());
			labelname.split(":");
		}
		catch (Exception ex) {
			System.out.println("{?}");
		}
	}
}
