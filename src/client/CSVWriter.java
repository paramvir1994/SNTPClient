package client;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CSVWriter {
	private String fileName = null;
	private FileWriter writer = null;

	public CSVWriter() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		fileName = "C:/ntplogs/MyLogFile_" + format.format(Calendar.getInstance().getTime()) + ".csv";
		writer = new FileWriter(fileName);
		write("Ntp Server");
		newColoumn();
		write("Round-trip delay");
		newColoumn();
		write("Local clock offset");
		newColoumn();
		write("Local time");
		newColoumn();
		write("Corrected time");
		newRow();
	}

	public void write(String text) throws IOException {
		writer.append(text);
	}
	
	public void newColoumn() throws IOException {
		writer.append(",");
	}

	public void newRow() throws IOException {
		writer.append("\n");
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		writer.close();
	}
	
	public void flush() throws IOException {
		writer.flush();
	}
}
