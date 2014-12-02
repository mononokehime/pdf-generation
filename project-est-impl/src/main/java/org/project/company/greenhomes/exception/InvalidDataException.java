package org.project.company.greenhomes.exception;

/**
 * Exception for not finding any data from a query that should return data
 *
 * @author fmacdermot
 */
public class InvalidDataException extends Exception {
	/**
	 * serialVersionUID - class version identifier
	 */
	static final long serialVersionUID = 1L;

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 * Constructs a {@link InvalidDataException} class
	 * without passing any message
	 */
	public InvalidDataException () {
		super();
	}

	/**
	 * Constructs a new {@link InvalidDataException} by
	 * passing a message
	 *
	 * @param msg
	 */
	public InvalidDataException (String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@link InvalidDataException} class by
	 * passing a message and a cause as parameters
	 *
	 * @param msg
	 * @param cause
	 */
	public InvalidDataException (String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a {@link InvalidDataException} class by
	 * passing a cause of the exception.
	 *
	 * @param cause
	 */
	public InvalidDataException (Throwable cause) {
		super(cause);
	}

}
