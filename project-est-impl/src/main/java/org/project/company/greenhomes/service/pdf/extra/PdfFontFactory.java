package org.project.company.greenhomes.service.pdf.extra;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.company.greenhomes.exception.ApplicationException;

import java.io.IOException;

/**
 * Convenience class for returning Font objects.
 *
 *
 */
public class PdfFontFactory {
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PdfFontFactory.class);
	private static BaseFont frutiger45Light;

	private static BaseFont frutiger55;

	private static BaseFont frutiger65Bold;

	public static Font getLogoTextFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 10, 0, PdfColorFactory.getBlueTextColor());
	}

	/**
	 * The font for the heading at the very beginning of the letter
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getHeadlineTextFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 12, 0, PdfColorFactory.getBlueTextColor());
	}

	/**
	 * The font for continuing over leaf text
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getContinueOverleafTextFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 11);
	}

	/**
	 * The font for the bullet headings in the measures section.
	 * measures you can etc etc
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getMeasureBulletTextFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 10);
	}

	/**
	 * The font for the text for each measure (below the bullet headings)
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getMeasureTextFont () throws ApplicationException {
		return new Font(getFrutiger45Light(), 10);
	}

	/**
	 * The font for most of the text
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getStandardTextFont () throws ApplicationException {
		return new Font(getFrutiger45Light(), 10);
	}

	/**
	 * The font for most of the text but for subscript
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getStandardSubscriptTextFont () throws ApplicationException {
		return new Font(getFrutiger45Light(), 10 - PdfConstants.CO2_SUBSCRIPT_Y_OFFSET);
	}

	/**
	 * This is the part your home was rated
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getRatingTextFont () throws ApplicationException {
		return new Font(getFrutiger45Light(), 10, Font.NORMAL, PdfColorFactory.getBlueTextColor());
	}

	/**
	 * This is the actual rating in a bolder text
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getRatingHighlightFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 10, Font.NORMAL, PdfColorFactory.getBlueTextColor());
	}

	/**
	 * The font just above the a-g energy graph
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getPerformanceTextFont () throws ApplicationException {
		return new Font(getFrutiger45Light(), 10);
	}

	/**
	 * The font for the customer address
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getCustomerAddressFont () throws ApplicationException {
		return new Font(getFrutiger45Light(), 11);
	}

	/**
	 * The font for the estac at the top right
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getESTACAddressFont () throws ApplicationException {
		return new Font(getFrutiger55(), 9, 0, PdfColorFactory.getBlueTextColor());
	}

	/**
	 * The font inside the ratings graph (current and potential energy use)
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getEstimatedEmissionsTextFont () throws ApplicationException {
		return new Font(getFrutiger45Light(), 9);
	}

	/**
	 * The headings for each of the individual ratings (current/potential)
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getEstimatedEmissionsHeadlineFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 12, Font.NORMAL, PdfColorFactory.getBlueTextColor());
	}

	/**
	 * The headings for each of the individual ratings (current/potential)
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getEstimatedEmissionsSubscriptHeadlineFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 12 - PdfConstants.CO2_SUBSCRIPT_Y_OFFSET, Font.NORMAL,
				PdfColorFactory.getBlueTextColor());
	}

	/**
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getTurquoiseTextFont () throws ApplicationException {
		return new Font(getFrutiger65Bold(), 10, Font.NORMAL, PdfColorFactory.getTurquoiseColor());
	}

	/**
	 * Text for the blue box at the bottom left of each page
	 *
	 * @return
	 * @throws ApplicationException
	 */
	public static Font getContactFont () throws ApplicationException {
		return new Font(getFrutiger55(), 12, Font.NORMAL, PdfColorFactory.getWhiteColor());
	}

	/**
	 * Method to return this font. Returns helvetica if unable to create
	 *
	 * @return
	 * @throws ApplicationException when all attempts to create a font fail
	 */
	public static BaseFont getFrutiger55 () throws ApplicationException {
		if (null == frutiger55) {
			try {
				frutiger55 = BaseFont.createFont("FrutigerLTStd-Roman.otf", BaseFont.CP1252, BaseFont.EMBEDDED);
			} catch (DocumentException e) {
				// problem here so log and default to helvetica
				log.error("Unable to get frutiger 55. Try to return last chance font.", e);
				return getLastChanceFont();
			} catch (IOException e) {
				// problem here so log and default to helvetica
				log.error("Unable to get frutiger 55. Try to return last chance font.", e);
				return getLastChanceFont();
			}
		}
		return frutiger55;
	}

	/**
	 * Method to return this font. Returns helvetica if unable to create
	 *
	 * @return
	 * @throws ApplicationException when all attempts to create a font fail
	 */
	public static BaseFont getFrutiger45Light () throws ApplicationException {
		if (null == frutiger45Light) {
			try {
				frutiger45Light = BaseFont.createFont("FrutigerLTStd-Light.otf", BaseFont.CP1252, BaseFont.EMBEDDED);
			} catch (DocumentException e) {
				// problem here so log and default to helvetica
				log.error("Unable to get frutiger 45. Try to return last chance font.", e);
				return getLastChanceFont();
			} catch (IOException e) {
				// problem here so log and default to helvetica
				log.error("Unable to get frutiger 45. Try to return last chance font.", e);
				return getLastChanceFont();
			}
		}
		return frutiger45Light;
	}

	/**
	 * Method to return this font. Returns helvetica if unable to create
	 *
	 * @return
	 * @throws ApplicationException when all attempts to create a font fail
	 */
	public static BaseFont getFrutiger65Bold () throws ApplicationException {
		if (null == frutiger65Bold) {
			try {
				frutiger65Bold = BaseFont.createFont("FrutigerLTStd-Bold.otf", BaseFont.CP1252, BaseFont.EMBEDDED);
			} catch (DocumentException e) {
				// problem here so log and default to helvetica
				log.error("Unable to get frutiger 65. Try to return last chance font.", e);
				return getLastChanceFont();
			} catch (IOException e) {
				// problem here so log and default to helvetica
				log.error("Unable to get frutiger 65. Try to return last chance font.", e);
				return getLastChanceFont();
			}
		}
		return frutiger65Bold;
	}

	public static float getFrutiger65BoldWidthInPoints (String text, int fontSize) throws ApplicationException {
		return getFrutiger65Bold().getWidthPoint(text, fontSize);
	}

	private static BaseFont getLastChanceFont () throws ApplicationException {
		try {
			return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			log.error("Unable to create even helvetica font. Unable to continue.", e);
		} catch (IOException e) {
			log.error("Unable to create even helvetica font. Unable to continue.", e);
		}
		throw new ApplicationException("Unable to get any font, even helvetica. End processing.");
	}
}
