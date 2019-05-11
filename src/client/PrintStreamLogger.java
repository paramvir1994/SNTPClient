package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;

public class PrintStreamLogger {
	private String logfileName = null;

	public PrintStreamLogger() {
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		logfileName = "C:/ntplogs/MyLogFile_" + format.format(Calendar.getInstance().getTime()) + ".log";
		
	}
	
	public void startPrintStream() throws FileNotFoundException {
		PrintStream printStream = new PrintStream(new File(logfileName));
		System.setOut(printStream);
	}

}
