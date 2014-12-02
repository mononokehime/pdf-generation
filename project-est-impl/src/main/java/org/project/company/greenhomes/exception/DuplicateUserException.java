package org.project.company.greenhomes.exception;

/**
 * Exception for finding an existing user
 *
 * @author fmacdermot
 */
public class DuplicateUserException extends Exception {
	/**
	 * serialVersionUID - class version identifier
	 */
	static final long serialVersionUID = 1L;

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 * Constructs a {@link DuplicateUserException} class
	 * without passing any message
	 */
	public DuplicateUserException () {
		super();
	}

	/**
	 * Constructs a new {@link DuplicateUserException} by
	 * passing a message
	 *
	 * @param msg
	 */
	public DuplicateUserException (String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@link DuplicateUserException} class by
	 * passing a message and a cause as parameters
	 *
	 * @param msg
	 * @param cause
	 */
	public DuplicateUserException (String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a {@link DuplicateUserException} class by
	 * passing a cause of the exception.
	 *
	 * @param cause
	 */
	public DuplicateUserException (Throwable cause) {
		super(cause);
	}

}
