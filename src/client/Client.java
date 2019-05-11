package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Client {

	private static final int PORT = 123;
	private static final String SERVER = "us.pool.ntp.org";
	private static PrintStreamLogger log = null;
	private static CSVWriter writer = null;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		writer = new CSVWriter();
		log = new PrintStreamLogger();
		log.startPrintStream();
		
		Date date = new Date();
		Timer timer = new Timer();
		int begin = 0;
		int timeInterval = 10000;
		timer.schedule(new TimerTask() {
			public static final long HOUR = 3600 * 1000; // in milli-seconds.
			long time = date.getTime();
			long temp = 0;

			@Override
			public void run() {
				// call the method
				try {
					Date newdate = new Date();
					temp = newdate.getTime();
					callServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (temp >= (time + 12 * HOUR)) {
					timer.cancel();
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, begin, timeInterval);
	}

	public static void callServer() throws IOException {
		final DatagramSocket socket = new DatagramSocket();
		final InetAddress address = InetAddress.getByName(SERVER);
		final byte[] buffer = new NtpMessage().toByteArray();

		socket.setSoTimeout(5000);
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
		socket.send(packet);
		packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		// Immediately record the incoming timestamp
		double destinationTimestamp = NtpMessage.now();
		NtpMessage msg = new NtpMessage(packet.getData());

		// Formula for delay according to the RFC2030 errata
		double roundTripDelay = (destinationTimestamp - msg.originateTimestamp)
				- (msg.transmitTimestamp - msg.receiveTimestamp);

		// The amount the server is ahead of the client
		double localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp)
				+ (msg.transmitTimestamp - destinationTimestamp)) / 2;

		socket.close();
		
		
		// Display response		
		System.out.format("NTP server: %s%n", address);
		System.out.printf("Round-trip delay:   %+9.2f ms%n", 1000 * roundTripDelay);
		System.out.printf("Local clock offset: %+9.2f ms%n", 1000 * localClockOffset);

		long now = System.currentTimeMillis(); // milliseconds 1x10e-3 seconds
		long cor = now + Math.round(1000.0 * localClockOffset);
		System.out.printf("Local time:      %1$ta, %1$td %1$tb %1$tY, %1$tI:%1$tm:%1$tS.%1$tL %1$tp %1$tZ%n", now);
		System.out.printf("Corrected time:  %1$ta, %1$td %1$tb %1$tY, %1$tI:%1$tm:%1$tS.%1$tL %1$tp %1$tZ%n", cor);
		System.out.println("----------------------------------------------------------");
		
		writer.write(String.format("%s", address));
		writer.newColoumn();
		writer.write(String.format("%+9.2f ms", 1000 * roundTripDelay));
		writer.newColoumn();
		writer.write(String.format("%+9.2f ms", 1000 * localClockOffset));
		writer.newColoumn();
		writer.write(String.format("%1$ta %1$td %1$tb %1$tY %1$tI:%1$tm:%1$tS.%1$tL %1$tp %1$tZ", now));
		writer.newColoumn();
		writer.write(String.format("%1$ta %1$td %1$tb %1$tY %1$tI:%1$tm:%1$tS.%1$tL %1$tp %1$tZ", cor));
		writer.newRow();
	}

}
