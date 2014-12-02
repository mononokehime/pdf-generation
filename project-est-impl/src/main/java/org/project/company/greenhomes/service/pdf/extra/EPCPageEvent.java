package org.project.company.greenhomes.service.pdf.extra;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.service.pdf.templates.PdfLineDrawer;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Events that will happen on every single pdf page
 *
 *
 */
public class EPCPageEvent extends PdfPageEventHelper {

	private String imagesFolder;
	private String epcURL;

	public String getEpcURL () {
		return epcURL;
	}

	public void setEpcURL (String epcURL) {
		this.epcURL = epcURL;
	}

	/*
	 * Runs for each page
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onStartPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onEndPage (PdfWriter writer, Document document) {
		try {
			//document.add(getTurqoiseLine(writer.getDirectContent()));
			// add the footer info
			// write the logo on all pages except page two.
			if (writer.getPageNumber() != 2) {
				addLogo(writer, document);
			}
			getTurqoiseLine(writer.getDirectContent());
			setContactLogo(writer.getDirectContent());
			document.add(getActOn());
		} catch (BadElementException e) {
			throw new RuntimeException("Unable to create page event elements!", e);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unable to create page event elements!", e);
		} catch (DocumentException e) {
			throw new RuntimeException("Unable to create page event elements!", e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create page event elements!", e);
		} catch (ApplicationException e) {
			throw new RuntimeException("Unable to create page event elements!", e);
		}
	}

	/**
	 * Method to add the logo to the top of the page. Not part of a
	 * page event as not all pages require it. Note that the logo is a graphic
	 * but the text below it has been substituted for {@link BaseFont.HELVETICA_BOLD}
	 *
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ApplicationException
	 */
	private void addLogo (PdfWriter writer, Document document)
			throws DocumentException, MalformedURLException, IOException, ApplicationException {
		Image image = Image.getInstance(imagesFolder + PdfConstants.EST_LOGO);
		image.setAbsolutePosition(43, 750);
		//scale 24f for the logo from hec
		image.scalePercent(24f);
		document.add(image);
		//		PdfContentByte bytes = writer.getDirectContent();
		//		bytes.saveState();
		//		bytes.beginText();
		//		// the quality of the image is poor so better to do text in real writing
		//		// from here added to try and fix image quality of text
		////		String text = "energy";
		////		bytes.saveState();
		////		bytes.beginText();
		////		bytes.setTextMatrix(43, 753);
		////		bytes.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 15);
		////		bytes.setColorFill(PdfColorFactory.getBlueTextColor());
		////		bytes.showText(text);
		////		text =  " saving trust";
		////		bytes.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 15);
		////		bytes.setColorFill(PdfColorFactory.getBlueTextColor());
		////		bytes.showText(text);
		////
		////		text =  "\u00AE";
		////		float width = bytes.getEffectiveStringWidth("energy saving trust", false);
		////		bytes.setTextMatrix(43+width+5, 760);
		////		bytes.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 6);
		////		bytes.setColorFill(PdfColorFactory.getBlueTextColor());
		////		bytes.showText(text);
		//		// finish here
		//		String text = "Here to help everyone save energy in the home";
		//		bytes.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 11);
		//		bytes.setColorFill(PdfColorFactory.getBlueTextColor());
		//		bytes.setTextMatrix(43, 735);
		//		bytes.showText(text);
		//		bytes.fill();
		//		bytes.endText();
		//bytes.restoreState();
	}

	private void getTurqoiseLine (PdfContentByte cb) throws MalformedURLException, IOException, DocumentException {
		PdfLineDrawer drawer = new PdfLineDrawer(cb);
		drawer.drawTurqoiseLine();
	}

	private void setContactLogo (PdfContentByte cbUnder)
			throws MalformedURLException, IOException, DocumentException, ApplicationException {
		BaseFont bf = PdfFontFactory.getFrutiger45Light();
		BaseFont bfBold = PdfFontFactory.getFrutiger65Bold();
		//s
		cbUnder.setColorFill(PdfColorFactory.getBlueTextColor());

		// we need to know how wide the url text will be in order to make sure the box is 
		// the correct size
		float width = bfBold.getWidthPoint(epcURL, 11f);
		// x,y,width,height,round
		cbUnder.roundRectangle(48, 43, width + 60, 52, 10);
		cbUnder.fill();
		cbUnder.beginText();
		cbUnder.setColorFill(Color.WHITE);
		//cbUnder.set
		cbUnder.setFontAndSize(bfBold, 35);
		// x followed by y
		cbUnder.setTextMatrix(53, 62);
		String text = "*";
		cbUnder.showText(text);

		cbUnder.setFontAndSize(bf, 11);

		cbUnder.setTextMatrix(76, 80);
		text = "Save energy today.";
		cbUnder.showText(text);
		cbUnder.setTextMatrix(76, 66);
		text = "Visit ";
		cbUnder.showText(text);
		cbUnder.setFontAndSize(bfBold, 11);
		text = epcURL; //"energysavingtrust.org.uk/epc";
		cbUnder.showText(text);

		cbUnder.setFontAndSize(bf, 11);
		cbUnder.setTextMatrix(76, 53);
		text = "or call ";
		cbUnder.showText(text);
		text = "0800 512 012";
		cbUnder.setFontAndSize(bfBold, 11);
		cbUnder.showText(text);
		cbUnder.endText();
		cbUnder.fill();
		//		Image image = Image.getInstance(imagesFolder+PdfConstants.CONTACT_LOGO);
		//		image.setAbsolutePosition(43f, 43f);
		//		image.scalePercent(60);
		//		return image;
	}

	private Image getActOn () throws BadElementException, MalformedURLException, IOException {
		Image image = Image.getInstance(imagesFolder + PdfConstants.ACT_ON);
		image.setAbsolutePosition(PdfConstants.ACT_ON_IMAGE_X, 43f);
		image.scalePercent(10f);
		return image;
	}

	public String getImagesFolder () {
		return imagesFolder;
	}

	public void setImagesFolder (String imagesFolder) {
		this.imagesFolder = imagesFolder;
	}
}
