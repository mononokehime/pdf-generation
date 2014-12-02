package org.project.company.greenhomes.service;

import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.domain.entity.User;

/**
 * Class that bundles up all the email sending options
 *
 *
 */
public interface MailService {

	/**
	 * Sends an email to admin to indicate the successful upload of a file. Text is in the email config file
	 *
	 * @param summary
	 */
	void sendLandRegistrySuccessEmail (UploadSummary summary);

	/**
	 * Sends an email to admin to indicate the failed upload of a file. Text is in the email config file
	 *
	 * @param summary
	 */
	void sendLandRegistryFailEmail (String message);

	/**
	 * Email to let the user know their password has been changed
	 *
	 * @param user
	 * @param newPwd
	 */
	void sendResetPasswordSuccessEmail (User user, String newPwd);

	void sendLandMarkSuccessEmail (UploadSummary summary);

	void sendLandMarkFailEmail (String message);

	void sendFtpCompletedEmail (String message);
}
