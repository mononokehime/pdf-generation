package org.project.company.greenhomes.exception;

/**
 * Generic Exception
 *
 * @author fmacdermot
 */
public class ApplicationException extends Exception {
	/**
	 * serialVersionUID - class version identifier
	 */
	static final long serialVersionUID = 1L;

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 * Constructs a {@link ApplicationException} class
	 * without passing any message
	 */
	public ApplicationException () {
		super();
	}

	/**
	 * Constructs a new {@link ApplicationException} by
	 * passing a message
	 *
	 * @param msg
	 */
	public ApplicationException (String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@link ApplicationException} class by
	 * passing a message and a cause as parameters
	 *
	 * @param msg
	 * @param cause
	 */
	public ApplicationException (String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a {@link ApplicationException} class by
	 * passing a cause of the exception.
	 *
	 * @param cause
	 */
	public ApplicationException (Throwable cause) {
		super(cause);
	}

}
