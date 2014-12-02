package org.project.company.greenhomes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.domain.entity.User;

/**
 * Implementation of the {@link MailService}
 *
 *
 */
public class MailServiceImpl implements MailService {
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	/**
	 * Spring mail sender
	 */
	private MailSender mailSender;
	/**
	 * Land reg successful message
	 */
	private MailMessage landRegistrySuccessfulUpload;
	/**
	 * Land reg failure message
	 */
	private MailMessage landRegistryFailureUpload;
	/**
	 * Landmark failure message
	 */
	private MailMessage landMarkFailureUpload;
	/**
	 * Landmark successful message
	 */
	private MailMessage landMarkSuccessfulUpload;
	/**
	 * ftp  message
	 */
	private MailMessage ftpComplete;

	private MailMessage passwordResetMessage;
	public MailServiceImpl (final MailSender mailSender) {
		if (null == mailSender) {
			throw new IllegalArgumentException("mailSender cannot be null.");
		}
		this.mailSender = mailSender;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.MailService#sendLandmarkSuccessEmail(org.project.company.greenhomes.domain.entity.UploadSummary)
	 */
	public void sendLandRegistrySuccessEmail (UploadSummary summary) {
		SimpleMailMessage message = new SimpleMailMessage(
				(SimpleMailMessage)landRegistrySuccessfulUpload);
		String text = message.getText();
		text = StringUtils.replace(text, "%COMPLETION_TIME%", summary.getEndTime() + "");
		text = StringUtils.replace(text, "%RECORDS_PROCESSED%", summary.getNumberOfRows() + "");
		text = StringUtils.replace(text, "%FAILURES%", summary.getErrorCount() + "");
		text = StringUtils.replace(text, "%FILE_RENAME%", summary.getFileRenameSucceeded() + "");
		message.setText(text);
		sendMessage(message);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.MailService#sendLandmarkFailEmail(java.lang.String)
	 */
	public void sendLandRegistryFailEmail (String message) {
		SimpleMailMessage mess = new SimpleMailMessage(
				(SimpleMailMessage)landRegistryFailureUpload);
		String text = mess.getText();
		text = StringUtils.replace(text, "%MESSAGE%", message);
		mess.setText(text);
		sendMessage(mess);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.MailService#sendResetPasswordSuccessEmail(org.project.company.greenhomes.domain.entity.User)
	 */
	public void sendResetPasswordSuccessEmail (User user, String newPwd) {
		SimpleMailMessage message = new SimpleMailMessage(
				(SimpleMailMessage)passwordResetMessage);
		String text = message.getText();
		text = StringUtils.replace(text, "%USER%",
				user.getFirstName() + " " + user.getFamilyName() + " (" + user.getUserName() + ")");
		text = StringUtils.trimWhitespace(text);
		message.setText(text + " " + newPwd + ".");
		message.setTo(user.getFirstName() + " " + user.getFamilyName() + " <" + user.getEmailAddress() + ">");
		sendMessage(message);
	}

	public void sendLandMarkFailEmail (String message) {
		SimpleMailMessage mess = new SimpleMailMessage(
				(SimpleMailMessage)landMarkFailureUpload);
		String text = mess.getText();
		text = StringUtils.replace(text, "%MESSAGE%", message);
		mess.setText(text);
		sendMessage(mess);
	}

	public void sendLandMarkSuccessEmail (UploadSummary summary) {
		SimpleMailMessage message = new SimpleMailMessage(
				(SimpleMailMessage)landMarkSuccessfulUpload);
		String text = message.getText();
		text = StringUtils.replace(text, "%COMPLETION_TIME%", summary.getEndTime() + "");
		text = StringUtils.replace(text, "%RECORDS_PROCESSED%", summary.getNumberOfRows() + "");
		text = StringUtils.replace(text, "%FAILURES%", summary.getErrorCount() + "");
		text = StringUtils.replace(text, "%FILE_RENAME%", summary.getFileRenameSucceeded() + "");
		message.setText(text);
		sendMessage(message);
	}

	public void sendFtpCompletedEmail (String message) {
		SimpleMailMessage mess = new SimpleMailMessage(
				(SimpleMailMessage)ftpComplete);
		mess.setText(mess.getText() + message);
		sendMessage(mess);
	}

	private void sendMessage (SimpleMailMessage message) {
		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error("Unable to send email message", e);
		}
	}

	public void setLandMarkFailureUpload (MailMessage landMarkFailureUpload) {
		this.landMarkFailureUpload = landMarkFailureUpload;
	}

	public void setLandMarkSuccessfulUpload (MailMessage landMarkSuccessfulUpload) {
		this.landMarkSuccessfulUpload = landMarkSuccessfulUpload;
	}

	public void setLandRegistrySuccessfulUpload (
			MailMessage landRegistrySuccessfulUpload) {
		this.landRegistrySuccessfulUpload = landRegistrySuccessfulUpload;
	}

	public void setLandRegistryFailureUpload (MailMessage landRegistryFailureUpload) {
		this.landRegistryFailureUpload = landRegistryFailureUpload;
	}

	public void setPasswordResetMessage (MailMessage passwordResetMessage) {
		this.passwordResetMessage = passwordResetMessage;
	}

	public void setFtpComplete (MailMessage ftpComplete) {
		this.ftpComplete = ftpComplete;
	}

}
