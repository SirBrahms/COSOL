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
			FileWriter writer = new FileWriter(cosol.strPop());
			writer.write(cosol.strPop());
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
			File fileToRead = new File(cosol.strPop());
			Scanner reader = new Scanner(fileToRead);
			while (reader.hasNextLine()) {
				cosol.StrStck.add(reader.nextLine());
			}
		}
		catch (Exception ex) {
			System.out.println("stdlib: fileReaderFault");
			System.exit(0);
		}
	}
	
	private static void deleteFile() {
		File fileToDelete = new File(cosol.strPop());
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
		    cosol.interpret(cosol._Labels.get(cosol.strPop()));
		}).start();
	}
}
