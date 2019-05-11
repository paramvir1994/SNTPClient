package client;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.Calendar;
import java.util.logging.*;

class ResultsLogger {
	private final Logger logger = Logger.getLogger(ResultsLogger.class.getName());
	private FileHandler fh = null;

	public ResultsLogger() {
		// just to make our log file nicer :)
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		try {
			fh = new FileHandler("C:/ntplogs/MyLogFile_" + format.format(Calendar.getInstance().getTime()) + ".log");
		} catch (Exception e) {
			e.printStackTrace();
		}

		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
	}

	public void doLogging(PrintStream printStream) {
		logger.info(printStream.toString());
//		logger.severe("error message");
//		logger.fine("fine message"); // won't show because to high level of logging
	}
}