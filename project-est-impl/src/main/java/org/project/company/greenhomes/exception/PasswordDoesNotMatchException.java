package org.project.company.greenhomes.exception;

/**
 * Exception for the  accounts functionality - invalid password
 *
 * @author fmacdermot
 */
public class PasswordDoesNotMatchException extends Exception {
	/**
	 * serialVersionUID - class version identifier
	 */
	static final long serialVersionUID = 1L;

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 * Constructs a {@link org.project.company.greenhomes.exception.PasswordDoesNotMatchException} class
	 * without passing any message
	 */
	public PasswordDoesNotMatchException () {
		super();
	}

	/**
	 * Constructs a new {@link org.project.company.greenhomes.exception.PasswordDoesNotMatchException} by
	 * passing a message
	 *
	 * @param msg
	 */
	public PasswordDoesNotMatchException (String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@link org.project.company.greenhomes.exception.PasswordDoesNotMatchException} class by
	 * passing a message and a cause as parameters
	 *
	 * @param msg
	 * @param cause
	 */
	public PasswordDoesNotMatchException (String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a {@link org.project.company.greenhomes.exception.PasswordDoesNotMatchException} class by
	 * passing a cause of the exception.
	 *
	 * @param cause
	 */
	public PasswordDoesNotMatchException (Throwable cause) {
		super(cause);
	}

}
