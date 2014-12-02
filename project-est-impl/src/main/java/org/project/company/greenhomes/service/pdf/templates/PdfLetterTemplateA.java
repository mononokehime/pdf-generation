/**
 *
 */
package org.project.company.greenhomes.service.pdf.templates;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.project.company.greenhomes.common.enums.MeasureTypeConstants;
import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.filefilters.PdfFileFilter;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.common.util.EnergyRatings;
import org.project.company.greenhomes.common.util.EnergyRatings.EnergyRating;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.entity.PropertyMeasure;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.pdf.extra.EPCPageEvent;
import org.project.company.greenhomes.service.pdf.extra.PdfConstants;
import org.project.company.greenhomes.service.pdf.extra.PdfFontFactory;
import org.project.company.greenhomes.service.pdf.extra.PdfLetterTemplateHelper;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class PdfLetterTemplateA implements PdfLetterTemplate {

	private static final int MINIMUM_DIMENSION_BEFORE_NEW_PAGE = 190;
	/*
	 * For some reason pulling the text from the xml file corrupts the utf for pound and inverted
	 * comma so we need to replace them.
	 */
	private static final String REPLACEMENT_MARKER_POUND_SIGN = "$pound-sign$";
	private static final String REPLACEMENT_MARKER_INVERTED_COMMA_SIGN = "$inverted-commas$";
	private static final String REPLACEMENT_MARKER_POTENTIAL_SAVING = "$potential-saving-text$";
	private PdfTemplateAText pdfTemplateAText;
	private String imagesFolder;
	private String pdfOutputDirectory;
	private Document document;
	private PdfWriter writer;
	private String epcURL;
	/*
	 * defaults to true
	 */
	private Boolean changeCaseTown = new Boolean(true);
	/*
	 * defaults to true
	 */
	private Boolean changeCaseAddress = new Boolean(true);
	private boolean newPage = false;

	/**
	 * This adds the customer and estac address information and also the energy ratings graph to the
	 * top of the first page.
	 *
	 * @param schedule
	 * @param address
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws InvalidDataException
	 * @throws ApplicationException
	 */
	private void addHeaderInfo (Schedule schedule, PropertyAddress address)
			throws MalformedURLException, IOException, DocumentException, InvalidDataException, ApplicationException {

		PropertyEPC epc = (PropertyEPC)address;
		PdfPTable table = createHeaderTable(schedule, address, document.getPageSize().getWidth());

		table.writeSelectedRows(0, -1, PdfTemplateAConstants.LEFT_MARGIN - 3, PdfTemplateAConstants.TOP_MARGIN + 60,
				writer.getDirectContent());
		// now we need to add the ratings part and the box around it
		// get the address text

		String text = changeCaseAddress ?
				WordUtils.capitalizeFully(StringUtils.trimToEmpty(address.getAddressLine2()) + " " + StringUtils
						.trimToEmpty(address.getAddressLine3())) :
				StringUtils.trimToEmpty(address.getAddressLine2()) + " " + StringUtils
						.trimToEmpty(address.getAddressLine3());
		PdfBuildRatingGraph me = new PdfBuildRatingGraph(writer.getDirectContentUnder(), writer.getDirectContent(),
				epc.getEnergyRatingCurrent(), epc.getEnergyRatingPotential(), text);
		me.createEfficiencyGraph();
	}

	/**
	 * This section writes the actual text of the letter on page 1 including the signature part at the
	 * bottom
	 *
	 * @param address
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws InvalidDataException
	 * @throws ApplicationException
	 */
	private void addLetterBody (PropertyAddress address)
			throws MalformedURLException, IOException, DocumentException, InvalidDataException, ApplicationException {

		PdfContentByte cb = writer.getDirectContent();
		ColumnText ct = new ColumnText(cb);
		ct.setSimpleColumn(PdfTemplateAConstants.LEFT_MARGIN, 100, // lower left coordinate
				PageSize.A4.getWidth() - 43, PageSize.A4.getHeight() - 320, // upper right
				18, Element.ALIGN_JUSTIFIED); //leading and alignment

		PropertyEPC epc = (PropertyEPC)address;
		// add the dear
		StringBuffer text = new StringBuffer("Dear resident");
		ct.addElement(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text.toString(), 267));
		// now the headline

		text = new StringBuffer(StringUtils.trim(pdfTemplateAText.getLetterHeadline()));
		// seems to be coming back with loads of white space so strip

		// get the possible saving text

		if (null == epc.getNamedAttribute(PropertyAttributeNames.TYPICAL_SAVING)) {
			String message = "Unable to find value for " + PropertyAttributeNames.TYPICAL_SAVING.getValue();
			throw new InvalidDataException(message);
		}

		ct.addElement(PdfLetterTemplateHelper.getParagraphWithSpacingAndAlign(PdfFontFactory.getHeadlineTextFont(),
				text.toString().replace(REPLACEMENT_MARKER_POTENTIAL_SAVING,
						"\u00A3" + epc.getNamedAttribute(PropertyAttributeNames.TYPICAL_SAVING).getValue()), 10,
				Element.ALIGN_CENTER));

		// first paragraph
		text = new StringBuffer(pdfTemplateAText.getParagraph1().trim());
		ct.addElement(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text.toString(), 5));

		// second paragraph
		text = new StringBuffer(pdfTemplateAText.getParagraph2().trim());

		ct.addElement(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text.toString(), 5));
		text = new StringBuffer(pdfTemplateAText.getRatingText().trim());
		// get the user rating
		EnergyRating r = EnergyRatings.getRating(epc.getEnergyRatingCurrent());
		//text = text.replace("replace-me", r.getRating());
		// this bit needs to be chunked as adding just an extra bit
		Phrase phrase = new Phrase(text.toString(), PdfFontFactory.getRatingTextFont());

		Chunk chunk = new Chunk(" " + r.getRating(), PdfFontFactory.getRatingHighlightFont());

		phrase.add(chunk);
		Paragraph paragraph = new Paragraph(phrase);
		paragraph.setSpacingBefore(10);
		paragraph.setAlignment(Element.ALIGN_CENTER);
		ct.addElement(paragraph);

		text = new StringBuffer(pdfTemplateAText.getParagraph3a().trim().replace(REPLACEMENT_MARKER_POTENTIAL_SAVING,
				"\u00A3" + epc.getNamedAttribute(PropertyAttributeNames.TYPICAL_SAVING).getValue()));
		if (null == epc.getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT)) {
			String message = "Unable to find value for " + PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT.getValue();
			throw new InvalidDataException(message);
		}
		String co2Cost = epc.getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT).getValue();
		if (null == epc.getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL)) {
			String message =
					"Unable to find value for " + PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL.getValue();
			throw new InvalidDataException(message);
		}
		String co2CostPot = epc.getNamedAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL).getValue();
		Float cost = new Float(co2Cost);
		Float costPot = new Float(co2CostPot);
		float co2saving = cost.floatValue() - costPot.floatValue();

		// now format the number
		DecimalFormat format = new DecimalFormat("#.#");
		// now subtract to get the co2 thing

		Paragraph par = PdfLetterTemplateHelper.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(),
				text.toString().replace("carbon-saving-text", format.format(co2saving) + ""), 5);
		Chunk ch = new Chunk("2", PdfFontFactory.getStandardSubscriptTextFont());
		ch.setTextRise(-PdfConstants.CO2_SUBSCRIPT_Y_OFFSET);
		par.add(ch);
		ch = new Chunk(pdfTemplateAText.getParagraph3b().trim(), PdfFontFactory.getStandardTextFont());
		par.add(ch);
		ct.addElement(par);
		//document.add(par);

		//ct.addElement(PdfLetterTemplateHelper.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text, 5));

		epcURL = epcURL.trim();
		text = new StringBuffer(pdfTemplateAText.getParagraph4().trim());

		ct.addElement(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text + " " + epcURL, 10));
		text = new StringBuffer(pdfTemplateAText.getSignOff().trim());
		ct.addElement(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text.toString(), 10));

		ct.addElement(new Paragraph("\n"));
		//Image sig = getSignature(epc.getCentre().getCentreCode());
		Image sig = getSignature();
		ct.addElement(sig);

		text = new StringBuffer("To be changed");
		ct.addElement(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text.toString(), 10));

		text = new StringBuffer("job title");//epc.getCentre().getContactJobTitle();

		ct.addElement(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getStandardTextFont(), text.toString(), 5));
		//cb
		cb.saveState();

		cb.beginText();
		text = new StringBuffer("Continued overleaf");
		// set the first part of text
		cb.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 11);
		cb.setTextMatrix(457, 122);
		cb.showText(text.toString());
		cb.setColorFill(Color.BLACK);
		//
		//		cb.setFontAndSize(PdfFontFactory.getFrutiger45Light(), 10);
		//		cb.setTextMatrix(props.getX()+7, props.getY()+3);
		//		cb.showText(text);
		cb.endText();
		cb.restoreState();

		ct.go();

	}

	/* (non-Javadoc)
	 * @see org.project.company.greenhomes.service.pdf.templates.PdfLetterTemplate#getWriter()
	 */
	private PdfWriter getWriter (Document document, PropertyAddress address)
			throws FileNotFoundException, DocumentException {
		PropertyEPC epc = (PropertyEPC)address;
		// file name gives us unique identifiers
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
				pdfOutputDirectory + epc.getScheduleId() + "_" + epc.getAddressKey() + PdfFileFilter.extension));
		// event for every page

		EPCPageEvent event = new EPCPageEvent();
		event.setImagesFolder(imagesFolder);

		event.setEpcURL(epcURL);
		writer.setPageEvent(event);
		return writer;
	}

	/* (non-Javadoc)
	 * @see org.project.company.greenhomes.service.pdf.templates.PdfLetterTemplate#getWriter()
	 */
	private PdfWriter getInMemoryWriter (ByteArrayOutputStream bao) throws DocumentException {

		PdfWriter writer = PdfWriter.getInstance(document, bao);
		// event for every page
		EPCPageEvent event = new EPCPageEvent();
		event.setImagesFolder(imagesFolder);
		event.setEpcURL(epcURL);
		writer.setPageEvent(event);
		return writer;
	}

	private PdfPTable createHeaderTable (Schedule schedule, PropertyAddress address, float width)
			throws BadElementException, MalformedURLException, IOException, ApplicationException {
		PropertyEPC epc = (PropertyEPC)address;

		float[] columnDefinitionSize = { 61, 39 };
		PdfPTable table = new PdfPTable(columnDefinitionSize);
		table.getDefaultCell().setPadding(0);
		table.setTotalWidth(width - 86);
		table.setLockedWidth(true);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setFixedHeight(78);
		// col 1 row 1
		table.addCell(cell);
		// row 1 column 2
		PdfPCell secondCell = buildESTACAddressCell(epc);

		table.addCell(secondCell);
		//		// column 1 row 2 cell
		PdfPCell thirdCell = buildCustomerAddress(schedule, epc);

		table.addCell(thirdCell);
		//		// column 2 row 2
		PdfPCell fourthCell = new PdfPCell();
		fourthCell.setPaddingTop(10);
		fourthCell.setBorder(0);
		fourthCell.setNoWrap(true);
		fourthCell.setPaddingLeft(PdfTemplateAConstants.COLUMN_TWO_LEFT_PAD);
		Phrase phrase = new Phrase("Your Energy Performance Certificate number:",
				PdfFontFactory.getPerformanceTextFont());
		fourthCell.addElement(phrase);
		phrase = new Phrase(10, epc.getRrn() + "\n", PdfFontFactory.getPerformanceTextFont());
		fourthCell.addElement(phrase);
		phrase = new Phrase(20, "Access your certificate at epcregister.com\n",
				PdfFontFactory.getPerformanceTextFont());
		fourthCell.addElement(phrase);

		table.addCell(fourthCell);

		return table;

	}

	private PdfPCell buildCustomerAddress (Schedule schedule, PropertyEPC epc) throws ApplicationException {
		PdfPCell thirdCell = new PdfPCell();
		thirdCell.setBorder(0);
		//thirdCell.setPaddingLeft(0);
		//thirdCell.setPaddingTop(25);
		//	thirdCell.setPaddingRight(PdfTemplateAConstants.COLUMN_ONE_RIGHT_PAD);
		thirdCell.setNoWrap(true);
		// add the address stuff for the customer
		//Paragraph add = new Paragraph(10);

		Phrase phrase = new Phrase(12, pdfTemplateAText.getGreeting().trim(), PdfFontFactory.getCustomerAddressFont());
		thirdCell.addElement(phrase);

		// address 1,2 and three should be on same line
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(epc.getAddressLine1())) {
			sb.append(epc.getAddressLine1().trim());
		}
		// add a comma if address line two exists
		if (StringUtils.isNotBlank(epc.getAddressLine2())) {
			// only need this if address 1 has a value
			if (StringUtils.isNotBlank(epc.getAddressLine1())) {
				sb.append(", ");
			}
			sb.append(epc.getAddressLine2().trim());
			sb.append(",");
		}
		if (StringUtils.isNotBlank(epc.getAddressLine3())) {
			sb.append(" " + epc.getAddressLine3().trim());
		}
		String address = changeCaseAddress ? WordUtils.capitalizeFully(sb.toString()) : sb.toString();
		phrase = new Phrase(12, address, PdfFontFactory.getCustomerAddressFont());
		thirdCell.addElement(phrase);

		if (null != epc.getTown()) {
			String town = changeCaseTown ? WordUtils.capitalizeFully(epc.getTown().trim()) : epc.getTown().trim();
			phrase = new Phrase(12, town, PdfFontFactory.getCustomerAddressFont());
			thirdCell.addElement(phrase);
		}

		if (null != epc.getPostcodeOutcode() && null != epc.getPostcodeIncode()) {
			phrase = new Phrase(12, epc.getPostcodeOutcode() + " " + epc.getPostcodeIncode(),
					PdfFontFactory.getCustomerAddressFont());
			thirdCell.addElement(phrase);
		}
		// we need to get the address of the schedule for the time stamp
		String today = DateFormatter.getFormattedStringForPDF(schedule.getStartDate());
		phrase = new Phrase(22, today, PdfFontFactory.getCustomerAddressFont());
		thirdCell.addElement(phrase);
		return thirdCell;
	}

	private PdfPCell buildESTACAddressCell (PropertyEPC epc) throws ApplicationException {
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPaddingLeft(PdfTemplateAConstants.COLUMN_TWO_LEFT_PAD);
		secondCell.setBorder(0);
		secondCell.setPaddingTop(0);
		secondCell.setNoWrap(true);
		Phrase chunk = new Phrase(0, epc.getCentre().getCentreName() + "\n", PdfFontFactory.getESTACAddressFont());
		secondCell.addElement(chunk);

		chunk = new Phrase(epc.getCentre().getAddress1() + "\n", PdfFontFactory.getESTACAddressFont());
		secondCell.addElement(chunk);
		if (null != epc.getCentre().getAddress2()) {
			chunk = new Phrase(epc.getCentre().getAddress2() + "\n", PdfFontFactory.getESTACAddressFont());
			secondCell.addElement(chunk);
		}
		if (null != epc.getCentre().getAddress3()) {
			chunk = new Phrase(epc.getCentre().getAddress3() + "\n", PdfFontFactory.getESTACAddressFont());
			secondCell.addElement(chunk);
		}
		if (null != epc.getCentre().getAddress4()) {
			chunk = new Phrase(epc.getCentre().getAddress4() + "\n", PdfFontFactory.getESTACAddressFont());
			secondCell.addElement(chunk);
		}
		// if we have 5 lines of address then we need
		// the post code on the same line as the town
		boolean hasAddress5 = false;
		if (null != epc.getCentre().getAddress5()) {
			chunk = new Phrase(epc.getCentre().getAddress5() + ", " + epc.getCentre().getPostcode(),
					PdfFontFactory.getESTACAddressFont());
			secondCell.addElement(chunk);
			hasAddress5 = true;
		}
		if (null != epc.getCentre().getPostcode()) {
			// can skip this if populated already
			if (!hasAddress5) {
				chunk = new Phrase(epc.getCentre().getPostcode(), PdfFontFactory.getESTACAddressFont());
				secondCell.addElement(chunk);
			}
		}
		return secondCell;
	}

	private Image getSignature () throws BadElementException, MalformedURLException, IOException {
		//Image image = Image.getInstance(imagesFolder+"signatures/"+imageId+".jpg");
		// use central signature
		Image image = Image.getInstance(imagesFolder + "signature.jpg");
		image.scaleAbsoluteHeight(26f);
		image.scaleAbsoluteWidth(100f);
		return image;
	}

	/**
	 * adds all the estimated graphing at the top of page two.
	 *
	 * @param address
	 * @throws DocumentException
	 * @throws IOException
	 * @throws InvalidDataException
	 * @throws ApplicationException
	 */
	private void addEstimatedGraphs (PropertyAddress address)
			throws DocumentException, IOException, InvalidDataException, ApplicationException {
		document.newPage();
		String text = pdfTemplateAText.getEstimatedHeadlinePart1().trim();
		Paragraph par = PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getEstimatedEmissionsHeadlineFont(), text, -7);
		Chunk ch = new Chunk("2", PdfFontFactory.getEstimatedEmissionsSubscriptHeadlineFont());
		ch.setTextRise(-PdfConstants.CO2_SUBSCRIPT_Y_OFFSET);
		par.add(ch);
		text = pdfTemplateAText.getEstimatedHeadlinePart2().trim();
		ch = new Chunk(" " + text, PdfFontFactory.getEstimatedEmissionsHeadlineFont());
		par.add(ch);
		document.add(par);

		//document.add(PdfLetterTemplateHelper.getParagraphWithSpacing(PdfFontFactory.getEstimatedEmissionsHeadlineFont(), text, -7));
		PdfLineDrawer draw = new PdfLineDrawer(writer.getDirectContent());
		// draw 3 lines
		draw.drawLongGreyDottedLine(PdfConstants.LONG_GREY_LINE_Y_TOP);
		draw.drawLongGreyDottedLine(PdfConstants.LONG_GREY_LINE_Y_MIDDLE);
		//draw.drawLongGreyDottedLine(PdfConstants.LONG_GREY_LINE_Y_BOTTOM);
		PdfBuildEstimatedGraph graph = new PdfBuildEstimatedGraph(writer.getDirectContent(),
				address.getPropertyAddressAttributeSet());
		graph.createEstimatedGraph();
		//text = "Heating";
		//document.add(PdfLetterTemplateHelper.getParagraphWithSpacing(PdfFontFactory.getTurquoiseTextFont(), text, 25));
		text = pdfTemplateAText.getEstimatedText().trim();
		document.add(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getEstimatedEmissionsTextFont(), text, 120));
		text = pdfTemplateAText.getMeasuresHeadline().trim();
		document.add(PdfLetterTemplateHelper
				.getParagraphWithSpacing(PdfFontFactory.getEstimatedEmissionsHeadlineFont(), text, 12));
		// lower cost

	}

	/**
	 * Adds all the measures to the page.
	 *
	 * @param writer
	 * @param address
	 * @throws DocumentException
	 * @throws ApplicationException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private void addMeasures (PropertyAddress address)
			throws DocumentException, ApplicationException, MalformedURLException, IOException {
		PropertyEPC epc = (PropertyEPC)address;

		// get the low ones first
		List<PropertyMeasure> measures = epc.getNamedMeasureType(MeasureTypeConstants.LOW_COST_MEASURE);
		PdfContentByte bytes = writer.getDirectContent();
		// this is the starting point
		// as each measure heading is slightly different we have to add these manually rather than iterating
		// through which would be easier
		// arguably this could use ColumnText class, but I had issues with
		// the margins when a new page was created - some of the text was disappearing at the top
		// as the ColumnText.go(true) method seemed to be writing the text rather than evaluating
		// rather disappointingly the injector was escaping \u00A3 so printed that on page -

		if (!measures.isEmpty()) {
			document.add(getMeasuresHeadline(
					pdfTemplateAText.getLowCostMeasures().trim().replace(REPLACEMENT_MARKER_POUND_SIGN, "\u00A3")
							.replace(REPLACEMENT_MARKER_INVERTED_COMMA_SIGN, "\u0027"), 20));
			for (Iterator<PropertyMeasure> i = measures.iterator(); i.hasNext(); ) {

				addMeasure(i.next(), i.hasNext(), bytes);
			}
		}
		//		// move on to the next one
		measures = epc.getNamedMeasureType(MeasureTypeConstants.HIGH_COST_MEASURE);
		if (!measures.isEmpty()) {
			if (testForNewPage()) {
				addNextPageText(bytes);
			}
			if (newPage) {
				addLineFeedForNewPage();
			}
			document.add(getMeasuresHeadline(
					pdfTemplateAText.getHighCostMeasures().trim().replace(REPLACEMENT_MARKER_POUND_SIGN, "\u00A3")
							.replace(REPLACEMENT_MARKER_INVERTED_COMMA_SIGN, "\u0027"), 5));
			for (Iterator<PropertyMeasure> i = measures.iterator(); i.hasNext(); ) {

				addMeasure(i.next(), i.hasNext(), bytes);
			}
		}
		if (newPage) {
			addLineFeedForNewPage();
		}
		PdfLineDrawer draw = new PdfLineDrawer(bytes);
		// draw 3 lines
		draw.drawLongGreyDottedLine(writer.getVerticalPosition(false) - 15);
		Paragraph par = new Paragraph("\n");
		document.add(par);

		measures = epc.getNamedMeasureType(MeasureTypeConstants.OTHER_TYPE_MEASURE);
		if (!measures.isEmpty()) {
			Chunk ch = new Chunk("2", PdfFontFactory.getStandardSubscriptTextFont());
			ch.setTextRise(-PdfConstants.CO2_SUBSCRIPT_Y_OFFSET);
			if (testForNewPage()) {
				addNextPageText(bytes);
			}
			if (newPage) {
				addLineFeedForNewPage();
			}
			par = getMeasuresHeadline(pdfTemplateAText.getFurtherMeasuresPart1().trim(), 5);
			par.add(ch);
			ch = new Chunk(" " + pdfTemplateAText.getFurtherMeasuresPart2().trim(),
					PdfFontFactory.getTurquoiseTextFont());
			par.add(ch);
			document.add(par);

			for (Iterator<PropertyMeasure> i = measures.iterator(); i.hasNext(); ) {

				addMeasure(i.next(), i.hasNext(), bytes);
			}
		}
	}

	/**
	 * Adds the measure headline and description to the document
	 *
	 * @param measure
	 * @param writer
	 * @param bytes
	 * @param document
	 * @throws ApplicationException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void addMeasure (PropertyMeasure measure, Boolean hasNext, PdfContentByte bytes)
			throws ApplicationException, DocumentException, MalformedURLException, IOException {
		com.lowagie.text.List list = getListItem(measure.getHeading());
		String youCouldSaveText = "";
		if (measure.getCategoryId() != MeasureTypeConstants.OTHER_TYPE_MEASURE) {
			youCouldSaveText = " You could save \u00A3" + measure.getTypicalSaving() + ".";
		}
		com.lowagie.text.ListItem subList = new ListItem(12l, measure.getDescription() + youCouldSaveText,
				PdfFontFactory.getMeasureTextFont());
		subList.setListSymbol(new Chunk(""));
		subList.setIndentationLeft(5l);
		list.add(subList);
		// add to the document
		if (newPage) {
			addLineFeedForNewPage();
		}
		document.add(list);

		// for each one of these check to see if we can add
		// only do this if more to come, or will leave
		// continued overleaf on final page
		if (hasNext) {
			if (testForNewPage()) {
				addNextPageText(bytes);
			}
		}

	}

	private void addNextPageText (PdfContentByte bytes) throws ApplicationException {
		newPage = true;
		String text = "";
		bytes.saveState();
		bytes.beginText();
		if (writer.getPageNumber() % 2 == 0) {
			text = "Continued on next page";
			bytes.setTextMatrix(432, 122);
		} else {
			text = "Continued overleaf";
			bytes.setTextMatrix(457, 122);
		}

		// set the headline for this bit
		bytes.setFontAndSize(PdfFontFactory.getFrutiger65Bold(), 11);

		bytes.showText(text);
		bytes.endText();
		// for this need to add at exact position					
		bytes.restoreState();
		document.newPage();
	}

	private void addLineFeedForNewPage () throws DocumentException {
		newPage = false;
		// need to move down the page a bit to move below the logo
		Paragraph par = new Paragraph("\n\n\n");
		document.add(par);
	}

	private Boolean testForNewPage () {
		return writer.getVerticalPosition(false) < MINIMUM_DIMENSION_BEFORE_NEW_PAGE;
	}

	/**
	 * Returns a list heading with a bullet point
	 *
	 * @param text
	 * @return
	 * @throws ApplicationException
	 */
	private com.lowagie.text.List getListItem (String text) throws ApplicationException {
		com.lowagie.text.List list = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED, 5l);
		list.setListSymbol("\u2022");
		list.setSymbolIndent(8l);
		list.setIndentationLeft(1);
		list.add(new ListItem(text, PdfFontFactory.getMeasureBulletTextFont()));
		return list;
	}

	/**
	 * Adds a headline for the measure, ie low cost high cost or other measure
	 *
	 * @param document
	 * @param text
	 * @param spacing
	 * @return
	 * @throws DocumentException
	 * @throws ApplicationException
	 */
	private Paragraph getMeasuresHeadline (String text, float spacing) throws DocumentException, ApplicationException {
		return PdfLetterTemplateHelper.getParagraphWithSpacing(PdfFontFactory.getTurquoiseTextFont(), text, spacing);
	}

	public void generate (final PropertyAddress address, final Schedule schedule)
			throws DocumentException, MalformedURLException, IOException, InvalidDataException, ApplicationException {
		try {
			// first of all check the center signature is available
			//checkResources(address);
			// the footer is in the page event document
			this.document = getDocument();

			this.writer = getWriter(document, address);

			// now open the document
			document.open();

			// add the header
			addHeaderInfo(schedule, address);

			// now add the logo
			//addLogo();
			// now add the text
			addLetterBody(address);

			// add the graphs that include all the measurements of how they can improve
			addEstimatedGraphs(address);

			addMeasures(address);

			//document.close();
		} finally {
			// close off if an exception is thrown
			// double finally just in case the document close throws and exception!
			try {
				if (document.isOpen()) {
					document.close();
				}
			} finally {
				if (null != writer) {
					writer.close();
				}
			}
		}
	}

	//private static final Logger log = LoggerFactory.getLogger(PdfLetterTemplateA.class);
	public ByteArrayOutputStream generateInMemory (final PropertyAddress address, final Schedule schedule)
			throws DocumentException, MalformedURLException, IOException, InvalidDataException, ApplicationException {

		try {
			// first of all check the centre signature is available
			//checkResources(address);		
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			// the footer is in the page event document
			this.document = getDocument();
			// get the writer
			this.writer = getInMemoryWriter(bao);
			// open the document
			document.open();
			// add the header
			addHeaderInfo(schedule, address);
			// now add the logo
			//	addLogo();
			// now add the text
			addLetterBody(address);
			addEstimatedGraphs(address);
			addMeasures(address);
			//document.close();
			return bao;
		} finally {
			// close off if an exception is thrown
			// double finally just in case the document close throws and exception!
			// double finally just in case the document close throws and exception!
			try {
				if (document.isOpen()) {
					document.close();
				}
			} finally {
				writer.close();
			}
		}
	}

	/**
	 * Gets a an A4 document to write to
	 *
	 * @return
	 */
	private Document getDocument () {
		return PdfLetterTemplateHelper.getDocument();
	}

	public void setEpcURL (String epcURL) {
		this.epcURL = epcURL;
	}

	public void setImagesFolder (String imagesFolder) {
		this.imagesFolder = imagesFolder;
	}

	public void setPdfOutputDirectory (String pdfOutputDirectory) {
		this.pdfOutputDirectory = pdfOutputDirectory;
	}

	public void setPdfTemplateAText (PdfTemplateAText pdfTemplateAText) {
		this.pdfTemplateAText = pdfTemplateAText;
	}

	public void setChangeCaseTown (Boolean changeCaseTown) {
		this.changeCaseTown = changeCaseTown;
	}

	public void setChangeCaseAddress (Boolean changeCaseAddress) {
		this.changeCaseAddress = changeCaseAddress;
	}
}
