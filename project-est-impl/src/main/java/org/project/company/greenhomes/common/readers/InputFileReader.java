package org.project.company.greenhomes.common.readers;

import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.exception.InvalidDataException;

import java.io.File;
import java.io.IOException;

/**
 * Interface for uploading file classes.
 *
 *
 */
public interface InputFileReader {

	/**
	 * Loads the data file from the third party.
	 *
	 * @param fileHandle the reference to the file on the file system
	 * @return
	 * @throws IOException
	 * @throws InvalidDataException
	 */
	UploadSummary processFile (File fileHandle) throws IOException, InvalidDataException;

	/**
	 * Reads the file from the file system. This is the one read by the timer job
	 */
	void readFileFromFileSystem ();

}
