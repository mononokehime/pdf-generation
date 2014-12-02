package org.project.company.greenhomes.exception;

/**
 * Exception for finding an existing property
 *
 * @author fmacdermot
 */
public class DuplicatePropertyException extends Exception {
	/**
	 * serialVersionUID - class version identifier
	 */
	static final long serialVersionUID = 1L;

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 * Constructs a {@link DuplicatePropertyException} class
	 * without passing any message
	 */
	public DuplicatePropertyException () {
		super();
	}

	/**
	 * Constructs a new {@link DuplicatePropertyException} by
	 * passing a message
	 *
	 * @param msg
	 */
	public DuplicatePropertyException (String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@link DuplicatePropertyException} class by
	 * passing a message and a cause as parameters
	 *
	 * @param msg
	 * @param cause
	 */
	public DuplicatePropertyException (String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a {@link DuplicatePropertyException} class by
	 * passing a cause of the exception.
	 *
	 * @param cause
	 */
	public DuplicatePropertyException (Throwable cause) {
		super(cause);
	}

}
