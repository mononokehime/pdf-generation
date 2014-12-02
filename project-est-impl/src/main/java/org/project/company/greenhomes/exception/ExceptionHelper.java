package org.project.company.greenhomes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Just contains a method to print the stack trace out as a string.
 * Used in a couple of places so put it here.
 *
 *
 */
public class ExceptionHelper {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ExceptionHelper.class);

	/**
	 * Turns a stack trace to a string and prepends with a time stamp to identify the exception.
	 *
	 * @param th
	 * @return
	 */
	public static String exceptionToString (Throwable th) {
		String stTrace = null;
		try {
			StringWriter sout = new StringWriter();
			sout.append("Exception reference is: [" + System.currentTimeMillis() + "]");
			PrintWriter out = new PrintWriter(sout);
			th.printStackTrace(out);
			out.close();
			sout.close();
			stTrace = sout.toString();
			sout = null;
			out = null;
		} catch (Exception e) {
			// really doomed here!
			log.error("eek", e);
			stTrace = "";
		}
		return stTrace;
	}

}
