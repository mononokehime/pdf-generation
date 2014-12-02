package org.project.company.greenhomes.exception;

/**
 * Exception for not finding any data from a query that should return data
 *
 * @author fmacdermot
 */
public class NoMatchingDataException extends Exception {
	/**
	 * serialVersionUID - class version identifier
	 */
	static final long serialVersionUID = 1L;

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 * Constructs a {@link NoMatchingDataException} class
	 * without passing any message
	 */
	public NoMatchingDataException () {
		super();
	}

	/**
	 * Constructs a new {@link NoMatchingDataException} by
	 * passing a message
	 *
	 * @param msg
	 */
	public NoMatchingDataException (String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@link NoMatchingDataException} class by
	 * passing a message and a cause as parameters
	 *
	 * @param msg
	 * @param cause
	 */
	public NoMatchingDataException (String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a {@link NoMatchingDataException} class by
	 * passing a cause of the exception.
	 *
	 * @param cause
	 */
	public NoMatchingDataException (Throwable cause) {
		super(cause);
	}

}
