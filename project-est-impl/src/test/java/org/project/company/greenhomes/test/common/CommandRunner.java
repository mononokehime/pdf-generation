package org.project.company.greenhomes.test.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility class to run commands from the command line
 */
public class CommandRunner {

	static final int OUTPUT_BUFFER_SIZE = 1024;

	static final int BUFFER_SIZE = 512;

	/**
	 * Runs a command with arguments passed as an array
	 */
	public static int runCommand (File location, String command, String[] args) {

		StringBuffer buffer = new StringBuffer(command);

		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				String s = args[i];
				buffer.append(s).append(" ");
			}
			buffer.delete(buffer.length() - 1, buffer.length());
		}

		String commandString = buffer.toString();
		return runCommand(location, commandString);

	}

	/**
	 * Runs command with arguments passed as a single String
	 *
	 * @param commandString the full command String
	 * @return the process exit value
	 */
	public static int runCommand (File location, String commandString) {

		int exitValue = -1;
		Process p = null;
		BufferedInputStream err = null;
		BufferedInputStream in = null;

		try {

			System.out.println("Running command: " + commandString);
			p = Runtime.getRuntime().exec(commandString, null, location);

			err = new BufferedInputStream(p.getErrorStream());
			in = new BufferedInputStream(p.getInputStream());
			Streamer errPumper = new Streamer(err, System.err);
			Streamer outPumper = new Streamer(in, System.out);

			Thread errThread = new Thread(errPumper);
			Thread outThread = new Thread(outPumper);
			errThread.start();
			outThread.start();

			p.waitFor();
			exitValue = p.exitValue();

			// Wait until the complete error stream has been read
			errThread.join();
			outThread.join();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			cleanup(p);
			cleanup(err);
			cleanup(in);
		}

		return exitValue;
	}

	private static void cleanup (Process p) {

		if (p != null) {
			p.destroy();
		}

	}

	private static void cleanup (BufferedInputStream is) {

		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Class to read the contents to the output stream specified
	 */
	private static class Streamer implements Runnable {

		private BufferedInputStream stream;

		private boolean end = false;

		private boolean stop = false;

		private int SLEEP_TIME = 5;

		private OutputStream out;

		public Streamer (BufferedInputStream is, OutputStream out) {
			this.stream = is;
			this.out = out;
		}

		/**
		 * Reads from stream buffer and writes to another until the input stream
		 * comes to an end
		 */
		public void pump () throws IOException {
			byte[] buf = new byte[BUFFER_SIZE];
			if (!end) {
				int bytesRead = stream.read(buf, 0, BUFFER_SIZE);
				if (bytesRead > 0) {
					out.write(buf, 0, bytesRead);
				} else if (bytesRead == -1) {
					end = true;
				}
			}
		}

		public void run () {
			try {
				while (!end) {
					pump();
					Thread.sleep(SLEEP_TIME);
				}
			} catch (Exception e) {
			}
		}
	}

}